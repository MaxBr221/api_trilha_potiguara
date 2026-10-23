package br.com.tupidigital.service;

import br.com.tupidigital.dto.DicaExercicioResponseDTO;
import br.com.tupidigital.dto.ExercicioResponseDTO;
import br.com.tupidigital.dto.ValidacaoAudioResponseDTO;
import br.com.tupidigital.dto.ValidacaoRespostaRequestDTO;
import br.com.tupidigital.dto.ValidacaoRespostaResponseDTO;
import br.com.tupidigital.entity.Exercicio;
import br.com.tupidigital.entity.Usuario;
import br.com.tupidigital.repository.ExercicioRepository;
import br.com.tupidigital.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.tupidigital.repository.ProgressoUsuarioExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service
public class ExercicioService {

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProgressoUsuarioExercicioRepository progressoUsuarioExercicioRepository;

    @Autowired
    private GeminiService geminiService;

    // Cache em memória: garante respostas em 0ms para a apresentação e blinda a cota do Gemini
    private final Map<UUID, String> cacheDicas = new ConcurrentHashMap<>();
    private final Map<String, br.com.tupidigital.dto.GeminiPayloads.FeedbackAvaliacao> cacheAvaliacao = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    public List<ExercicioResponseDTO> listarExerciciosPorLicao(UUID licaoId) {
        return exercicioRepository.findByLicaoIdOrderByOrdemIndexAsc(licaoId).stream()
                .map(ExercicioResponseDTO::new)
                .toList();
    }

    public ValidacaoRespostaResponseDTO validarResposta(UUID exercicioId, ValidacaoRespostaRequestDTO request) {
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        // Sanitização básica contra tentativas de injeção de HTML/Scripts simples
        String respostaUsuario = request.respostaUsuario() != null ? request.respostaUsuario().replaceAll("<[^>]*>", "").trim() : "";
        respostaUsuario = HtmlUtils.htmlUnescape(respostaUsuario);
        String respostaCorreta = exercicio.getRespostaCorreta().trim();

        String rUserClean = removerAcentosEPontuacao(respostaUsuario);
        String rCorrClean = removerAcentosEPontuacao(respostaCorreta);

        boolean correta = false;
        String feedback = null;

        if (respostaUsuario.equalsIgnoreCase(respostaCorreta)) {
            // 1. Exato perfeito
            correta = true;
            feedback = "Excelente! Você acertou em cheio a resposta: \"" + respostaCorreta + "\".";
        } else if (rUserClean.equalsIgnoreCase(rCorrClean)) {
            // 2. Tolerância a Acentos e Caracteres Especiais (Estilo Duolingo)
            correta = true;
            feedback = "Correto! Mas fique atento aos acentos e caracteres especiais: a grafia padrão é \"" + respostaCorreta + "\".";
        } else if (rUserClean.length() >= 3 && calcularDistanciaLevenshtein(rUserClean, rCorrClean) == 1) {
            // 3. Tolerância a Erro de Digitação / Typo de 1 letra (Estilo Duolingo)
            correta = true;
            feedback = "Você teve um pequeno deslize de digitação! Você escreveu \"" + respostaUsuario + "\", mas a grafia padrão é \"" + respostaCorreta + "\". Fique atento!";
        } else if ("DIGITACAO".equalsIgnoreCase(exercicio.getTipo()) || "TRADUCAO".equalsIgnoreCase(exercicio.getTipo()) || "PRONUNCIA".equalsIgnoreCase(exercicio.getTipo())) {
            // 4. Casos mais complexos avaliados pedagogicamente pelo Gemini (2.2)
            // NOTA: Chamada ao Gemini é executada FORA de transação de banco para nunca travar conexões do HikariCP!
            String cacheKey = exercicioId.toString() + ":" + rUserClean;
            var avaliacaoIA = cacheAvaliacao.get(cacheKey);
            if (avaliacaoIA == null) {
                avaliacaoIA = geminiService.avaliarTentativa(exercicio.getEnunciado(), respostaCorreta, respostaUsuario);
                if (avaliacaoIA != null) {
                    cacheAvaliacao.put(cacheKey, avaliacaoIA);
                }
            }
            if (avaliacaoIA != null) {
                correta = avaliacaoIA.aceita();
                feedback = avaliacaoIA.feedbackPedagogico();
                if (feedback != null) {
                    feedback = HtmlUtils.htmlUnescape(feedback).trim();
                }
            }
        } else {
            feedback = "Não foi dessa vez. A resposta correta é: \"" + respostaCorreta + "\". Continue praticando!";
        }

        // Salva progresso em transação curta e isolada (apenas ~2ms)
        Integer xpGanho = registrarProgressoUsuario(exercicioId, correta, exercicio.getPontuacaoXp());
        
        return new ValidacaoRespostaResponseDTO(correta, xpGanho, exercicio.getRespostaCorreta(), feedback);
    }

    public ValidacaoAudioResponseDTO validarAudio(UUID exercicioId, byte[] audioBytes, String mimeType) {
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        String respostaCorreta = exercicio.getRespostaCorreta().trim();

        if (audioBytes == null || audioBytes.length < 150) {
            return new ValidacaoAudioResponseDTO(
                    false,
                    0,
                    0,
                    respostaCorreta,
                    "",
                    "O microfone não capturou a sua fala. Por favor, verifique as permissões do microfone e tente gravar novamente."
            );
        }
        
        // Chamada de áudio ao Gemini executada FORA de transação para evitar pool starvation
        var avaliacaoIA = geminiService.avaliarPronuncia(audioBytes, mimeType, exercicio.getEnunciado(), respostaCorreta);

        boolean correta = avaliacaoIA.aceita() || (avaliacaoIA.precisao() != null && avaliacaoIA.precisao() >= 65);
        int precisao = avaliacaoIA.precisao() != null ? avaliacaoIA.precisao() : (correta ? 92 : 35);
        String feedback = avaliacaoIA.feedbackPedagogico();
        if (feedback != null) {
            feedback = HtmlUtils.htmlUnescape(feedback).trim();
        }
        String transcricao = avaliacaoIA.transcricaoAproximada();
        if (transcricao != null) {
            transcricao = HtmlUtils.htmlUnescape(transcricao).trim();
        }

        // Salva progresso em transação curta e isolada
        Integer xpGanho = registrarProgressoUsuario(exercicioId, correta, exercicio.getPontuacaoXp());

        return new ValidacaoAudioResponseDTO(correta, precisao, xpGanho, respostaCorreta, transcricao, feedback);
    }

    @Transactional
    public Integer registrarProgressoUsuario(UUID exercicioId, boolean correta, Integer pontuacaoXp) {
        if (!correta) {
            return 0;
        }

        Integer xpPadrao = (pontuacaoXp != null) ? pontuacaoXp : 0;

        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Se for visitante anônimo fazendo o teste inicial antes de criar conta,
        // retorna a pontuação ganha para que o front-end acumule o XP (ex: 30 XP) e envie no cadastro
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return xpPadrao;
        }

        String email = auth.getName();
        org.springframework.security.core.userdetails.UserDetails userDetails = usuarioRepository.findByEmail(email);
        if (userDetails == null) {
            return xpPadrao;
        }

        Usuario usuario = (Usuario) userDetails;
        Exercicio exercicio = exercicioRepository.findById(exercicioId).orElse(null);
        if (exercicio == null) {
            return xpPadrao;
        }

        Integer xpGanho = xpPadrao;

        if (correta) {
            boolean jaAcertou = progressoUsuarioExercicioRepository.existsByUsuarioIdAndExercicioIdAndAcertouTrue(usuario.getId(), exercicio.getId());
            if (!jaAcertou) {
                usuario.setXp(usuario.getXp() + xpGanho);
                usuarioRepository.save(usuario);
            } else {
                xpGanho = 0;
            }
        }

        br.com.tupidigital.entity.ProgressoUsuarioExercicio progressoExercicio = new br.com.tupidigital.entity.ProgressoUsuarioExercicio();
        progressoExercicio.setUsuario(usuario);
        progressoExercicio.setExercicio(exercicio);
        progressoExercicio.setAcertou(correta);
        progressoExercicio.setCriadoEm(java.time.LocalDateTime.now());
        progressoUsuarioExercicioRepository.save(progressoExercicio);

        return xpGanho;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public DicaExercicioResponseDTO obterDica(UUID exercicioId) {
        // 1. Retorno instantâneo do cache em memória (0ms, zero chamadas ao banco e zero consumo de cota)
        String dicaCacheada = cacheDicas.get(exercicioId);
        if (dicaCacheada != null) {
            return new DicaExercicioResponseDTO(dicaCacheada);
        }

        // 2. Busca rápida dos dados do exercício
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        String enunciado = exercicio.getEnunciado();
        String respostaCorreta = exercicio.getRespostaCorreta();
        List<String> opcoes = exercicio.getOpcoes() != null ? List.copyOf(exercicio.getOpcoes()) : List.of();

        // 3. Gera a dica via IA
        String dica = geminiService.gerarDica(enunciado, respostaCorreta, opcoes);

        // 4. Salva no cache para que todos os outros alunos recebam instantaneamente
        if (dica != null && !dica.isBlank()) {
            cacheDicas.put(exercicioId, dica);
        }

        return new DicaExercicioResponseDTO(dica);
    }

    private String removerAcentosEPontuacao(String str) {
        if (str == null) return "";
        String normalized = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "")
                         .replace("'", "")
                         .replace("’", "")
                         .replace("&#39;", "")
                         .replace("&apos;", "")
                         .replaceAll("\\s+", " ")
                         .trim()
                         .toLowerCase();
    }

    private int calcularDistanciaLevenshtein(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
