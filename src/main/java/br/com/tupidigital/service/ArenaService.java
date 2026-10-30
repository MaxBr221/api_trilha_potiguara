package br.com.tupidigital.service;

import br.com.tupidigital.dto.ArenaDTOs.*;
import br.com.tupidigital.entity.*;
import br.com.tupidigital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArenaService {

    @Autowired
    private SalaArenaRepository salaArenaRepository;

    @Autowired
    private ParticipanteArenaRepository participanteArenaRepository;

    @Autowired
    private RespostaArenaRepository respostaArenaRepository;

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final SecureRandom random = new SecureRandom();

    private Usuario getAuthenticatedUsuario() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return (Usuario) usuarioRepository.findByEmail(auth.getName());
    }

    @Transactional(readOnly = true)
    public List<TrilhaArenaDTO> listarModulosDisponiveis() {
        List<Trilha> trilhas = trilhaRepository.findAll();
        List<TrilhaArenaDTO> resultado = new ArrayList<>();

        for (Trilha trilha : trilhas) {
            List<Modulo> modulos = moduloRepository.findByTrilhaIdOrderByOrdemIndexAsc(trilha.getId());
            List<ModuloArenaDTO> modulosDTO = new ArrayList<>();

            for (Modulo modulo : modulos) {
                long totalQuestoes = exercicioRepository.countByModuloId(modulo.getId());
                modulosDTO.add(new ModuloArenaDTO(
                        modulo.getId(),
                        modulo.getTitulo(),
                        modulo.getDescricao(),
                        (int) totalQuestoes
                ));
            }

            resultado.add(new TrilhaArenaDTO(
                    trilha.getId(),
                    trilha.getNome(),
                    trilha.getIcon(),
                    trilha.getCorBase(),
                    modulosDTO
            ));
        }

        return resultado;
    }

    public CriarArenaResponseDTO criarSala(CriarArenaRequestDTO dto) {
        if (dto.moduloIds() == null || dto.moduloIds().isEmpty()) {
            throw new IllegalArgumentException("Selecione pelo menos um módulo para criar a Arena.");
        }

        List<Exercicio> exerciciosEncontrados = exercicioRepository.findByModuloIds(dto.moduloIds());
        
        // Filtra exercícios que possuem opções de escolha válidas (mínimo 2 alternativas)
        List<Exercicio> questoesValidas = exerciciosEncontrados.stream()
                .filter(e -> e.getOpcoes() != null && e.getOpcoes().size() >= 2 && e.getRespostaCorreta() != null && !e.getRespostaCorreta().isBlank())
                .collect(Collectors.toList());

        if (questoesValidas.isEmpty()) {
            throw new IllegalStateException("Nenhuma questão com alternativas foi encontrada nos módulos selecionados.");
        }

        // Embaralha para tornar a partida dinâmica
        Collections.shuffle(questoesValidas, random);

        // Se o professor limitou a quantidade (ex: 10 ou 15), aplica o corte
        int quantidade = questoesValidas.size();
        if (dto.quantidadeQuestoes() != null && dto.quantidadeQuestoes() > 0 && dto.quantidadeQuestoes() < quantidade) {
            quantidade = dto.quantidadeQuestoes();
        }
        List<Exercicio> questoesFinais = questoesValidas.subList(0, quantidade);

        String pin = gerarPinUnico();
        int tempoPergunta = (dto.tempoPorPergunta() != null && dto.tempoPorPergunta() >= 10 && dto.tempoPorPergunta() <= 60)
                ? dto.tempoPorPergunta()
                : 20;

        String titulo = (dto.titulo() != null && !dto.titulo().isBlank())
                ? dto.titulo().trim()
                : "Tupi Arena #" + pin;

        Usuario professor = getAuthenticatedUsuario();

        SalaArena sala = SalaArena.builder()
                .codigoPin(pin)
                .titulo(titulo)
                .professor(professor)
                .status(StatusArena.LOBBY)
                .perguntaAtualIndex(0)
                .tempoPerguntaSegundos(tempoPergunta)
                .build();

        sala = salaArenaRepository.save(sala);

        List<SalaArenaQuestao> itensQuestoes = new ArrayList<>();
        for (int i = 0; i < questoesFinais.size(); i++) {
            SalaArenaQuestao item = SalaArenaQuestao.builder()
                    .sala(sala)
                    .exercicio(questoesFinais.get(i))
                    .ordemIndex(i + 1)
                    .build();
            itensQuestoes.add(item);
        }
        sala.setQuestoes(itensQuestoes);
        salaArenaRepository.save(sala);

        return new CriarArenaResponseDTO(
                sala.getId(),
                sala.getCodigoPin(),
                sala.getTitulo(),
                questoesFinais.size(),
                sala.getTempoPerguntaSegundos(),
                sala.getStatus().name()
        );
    }

    public EntrarArenaResponseDTO entrarSala(EntrarArenaRequestDTO dto) {
        if (dto.codigoPin() == null || dto.codigoPin().isBlank()) {
            throw new IllegalArgumentException("O código PIN da sala é obrigatório.");
        }

        String pin = dto.codigoPin().replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pin)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pin));

        if (sala.getStatus() == StatusArena.FINALIZADA) {
            throw new IllegalStateException("Esta partida da Tupi Arena já foi finalizada.");
        }

        String apelido = (dto.apelido() != null) ? dto.apelido().trim() : "";
        if (apelido.isEmpty()) {
            throw new IllegalArgumentException("Informe um apelido ou seu nome para entrar na Arena.");
        }

        Usuario usuarioLogado = getAuthenticatedUsuario();

        // Se o mesmo participante já entrou com esse apelido na sala, reutiliza (reconexão)
        Optional<ParticipanteArena> existente = participanteArenaRepository.findBySalaIdAndApelido(sala.getId(), apelido);
        ParticipanteArena participante;

        if (existente.isPresent()) {
            participante = existente.get();
            participante.setConectado(true);
            if (usuarioLogado != null && participante.getUsuario() == null) {
                participante.setUsuario(usuarioLogado);
            }
            participante = participanteArenaRepository.save(participante);
        } else {
            participante = ParticipanteArena.builder()
                    .sala(sala)
                    .usuario(usuarioLogado)
                    .apelido(apelido)
                    .pontuacaoTotal(0)
                    .acertos(0)
                    .conectado(true)
                    .build();
            participante = participanteArenaRepository.save(participante);
        }

        return new EntrarArenaResponseDTO(
                sala.getId(),
                participante.getId(),
                sala.getCodigoPin(),
                participante.getApelido(),
                sala.getStatus().name()
        );
    }

    public EstadoArenaResponseDTO avancarEtapa(String pin) {
        String pinLimpo = pin.replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pinLimpo)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pinLimpo));

        int totalQuestoes = (sala.getQuestoes() != null) ? sala.getQuestoes().size() : 0;

        switch (sala.getStatus()) {
            case LOBBY -> {
                if (totalQuestoes > 0) {
                    sala.setStatus(StatusArena.PERGUNTA_ATIVA);
                    sala.setPerguntaAtualIndex(1);
                    sala.setTempoInicioPergunta(LocalDateTime.now());
                } else {
                    throw new IllegalStateException("A sala não possui questões cadastradas.");
                }
            }
            case PERGUNTA_ATIVA -> {
                sala.setStatus(StatusArena.REVELACAO);
            }
            case REVELACAO -> {
                sala.setStatus(StatusArena.PLACAR);
            }
            case PLACAR -> {
                if (sala.getPerguntaAtualIndex() < totalQuestoes) {
                    sala.setPerguntaAtualIndex(sala.getPerguntaAtualIndex() + 1);
                    sala.setStatus(StatusArena.PERGUNTA_ATIVA);
                    sala.setTempoInicioPergunta(LocalDateTime.now());
                } else {
                    sala.setStatus(StatusArena.FINALIZADA);
                    creditarXpFinalistas(sala);
                }
            }
            case FINALIZADA -> {
                // Já concluída
            }
        }

        salaArenaRepository.save(sala);
        return obterEstado(pinLimpo, null);
    }

    public EstadoArenaResponseDTO pularParaProximaQuestao(String pin) {
        String pinLimpo = pin.replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pinLimpo)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pinLimpo));

        int totalQuestoes = (sala.getQuestoes() != null) ? sala.getQuestoes().size() : 0;

        if (sala.getStatus() == StatusArena.REVELACAO || sala.getStatus() == StatusArena.PLACAR) {
            if (sala.getPerguntaAtualIndex() < totalQuestoes) {
                sala.setPerguntaAtualIndex(sala.getPerguntaAtualIndex() + 1);
                sala.setStatus(StatusArena.PERGUNTA_ATIVA);
                sala.setTempoInicioPergunta(LocalDateTime.now());
            } else {
                sala.setStatus(StatusArena.FINALIZADA);
                creditarXpFinalistas(sala);
            }
            salaArenaRepository.save(sala);
        }

        return obterEstado(pinLimpo, null);
    }

    public ResponderArenaResponseDTO responder(String pin, ResponderArenaRequestDTO dto) {
        String pinLimpo = pin.replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pinLimpo)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pinLimpo));

        if (sala.getStatus() != StatusArena.PERGUNTA_ATIVA) {
            throw new IllegalStateException("Esta questão não está aberta para respostas no momento.");
        }

        // Validação de tempo limite: impede resposta se o cronômetro oficial já expirou (+ tolerância de 2s para latência)
        if (sala.getTempoInicioPergunta() != null && sala.getTempoPerguntaSegundos() != null) {
            long segundosDecorridos = Duration.between(sala.getTempoInicioPergunta(), LocalDateTime.now()).getSeconds();
            if (segundosDecorridos > sala.getTempoPerguntaSegundos() + 2) {
                throw new IllegalStateException("O tempo para responder esta questão já se esgotou.");
            }
        }

        ParticipanteArena participante = participanteArenaRepository.findById(dto.participanteId())
                .orElseThrow(() -> new IllegalArgumentException("Participante não encontrado"));

        if (!participante.getSala().getId().equals(sala.getId())) {
            throw new IllegalArgumentException("Participante não pertence a esta sala.");
        }

        int index = sala.getPerguntaAtualIndex() - 1;
        if (index < 0 || index >= sala.getQuestoes().size()) {
            throw new IllegalStateException("Índice de questão inválido.");
        }

        Exercicio exercicioAtual = sala.getQuestoes().get(index).getExercicio();

        // Verifica se já respondeu
        Optional<RespostaArena> respostaExistente = respostaArenaRepository
                .findByParticipanteIdAndExercicioId(participante.getId(), exercicioAtual.getId());

        if (respostaExistente.isPresent()) {
            long totalRespostas = respostaArenaRepository.countBySalaIdAndExercicioId(sala.getId(), exercicioAtual.getId());
            return new ResponderArenaResponseDTO(true, (int) totalRespostas, "Resposta já registrada.");
        }

        String opcao = (dto.opcaoEscolhida() != null) ? dto.opcaoEscolhida().trim() : "";
        boolean acertou = exercicioAtual.getRespostaCorreta() != null &&
                opcao.equalsIgnoreCase(exercicioAtual.getRespostaCorreta().trim());

        int pontosGanhos = 0;
        if (acertou) {
            // Pontuação estilo Kahoot: Base de 500 + Bônus de Velocidade de até 500
            int tempoMs = (dto.tempoRespostaMs() != null && dto.tempoRespostaMs() > 0)
                    ? dto.tempoRespostaMs()
                    : 10000;
            int tempoLimiteMs = sala.getTempoPerguntaSegundos() * 1000;
            double proporcaoTempo = Math.min(1.0, (double) tempoMs / (double) tempoLimiteMs);
            int bonus = Math.max(50, (int) (500 * (1.0 - proporcaoTempo)));
            pontosGanhos = 500 + bonus;

            participante.setAcertos(participante.getAcertos() + 1);
            participante.setPontuacaoTotal(participante.getPontuacaoTotal() + pontosGanhos);
        }

        RespostaArena resposta = RespostaArena.builder()
                .sala(sala)
                .participante(participante)
                .exercicio(exercicioAtual)
                .opcaoEscolhida(opcao)
                .acertou(acertou)
                .tempoRespostaMs(dto.tempoRespostaMs())
                .pontosGanhos(pontosGanhos)
                .build();

        respostaArenaRepository.save(resposta);
        participanteArenaRepository.save(participante);

        long totalRespostas = respostaArenaRepository.countBySalaIdAndExercicioId(sala.getId(), exercicioAtual.getId());
        return new ResponderArenaResponseDTO(true, (int) totalRespostas, "Resposta computada com sucesso!");
    }

    @Transactional(readOnly = true)
    public EstadoArenaResponseDTO obterEstado(String pin, UUID participanteId) {
        String pinLimpo = pin.replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pinLimpo)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pinLimpo));

        int totalQuestoes = (sala.getQuestoes() != null) ? sala.getQuestoes().size() : 0;
        int indexAtual = sala.getPerguntaAtualIndex();

        Integer tempoRestante = null;
        if (sala.getStatus() == StatusArena.PERGUNTA_ATIVA && sala.getTempoInicioPergunta() != null) {
            long decorrido = Duration.between(sala.getTempoInicioPergunta(), LocalDateTime.now()).toSeconds();
            tempoRestante = (int) Math.max(0, sala.getTempoPerguntaSegundos() - decorrido);
        }

        String enunciado = null;
        List<String> opcoes = null;
        String respostaCorreta = null;
        Map<String, Long> contagemRespostas = null;
        String fonetica = null;
        String traducaoPtBr = null;
        int totalRespostas = 0;

        Exercicio exercicioAtual = null;
        if (indexAtual > 0 && indexAtual <= totalQuestoes) {
            exercicioAtual = sala.getQuestoes().get(indexAtual - 1).getExercicio();
            enunciado = exercicioAtual.getEnunciado();
            opcoes = exercicioAtual.getOpcoes();

            totalRespostas = (int) respostaArenaRepository.countBySalaIdAndExercicioId(sala.getId(), exercicioAtual.getId());

            // Apenas revela gabarito e contagens se estiver em REVELACAO, PLACAR ou FINALIZADA
            if (sala.getStatus() == StatusArena.REVELACAO || sala.getStatus() == StatusArena.PLACAR || sala.getStatus() == StatusArena.FINALIZADA) {
                respostaCorreta = exercicioAtual.getRespostaCorreta();

                List<RespostaArena> respostas = respostaArenaRepository.findBySalaIdAndExercicioId(sala.getId(), exercicioAtual.getId());
                contagemRespostas = respostas.stream()
                        .filter(r -> r.getOpcaoEscolhida() != null)
                        .collect(Collectors.groupingBy(RespostaArena::getOpcaoEscolhida, Collectors.counting()));

                // Procura conteúdo linguístico associado para enriquecer com fonética e tradução
                if (exercicioAtual.getLicao() != null && exercicioAtual.getLicao().getConteudos() != null) {
                    String respLower = respostaCorreta != null ? respostaCorreta.toLowerCase().trim() : "";
                    String enunLower = enunciado != null ? enunciado.toLowerCase().trim() : "";

                    for (ConteudoLinguistico c : exercicioAtual.getLicao().getConteudos()) {
                        String palTupi = c.getPalavraTupi() != null ? c.getPalavraTupi().toLowerCase().trim() : "";
                        String tradPt = c.getTraducaoPtBr() != null ? c.getTraducaoPtBr().toLowerCase().trim() : "";

                        // Verifica correspondência direta de Tupi ou Português no enunciado ou na resposta
                        boolean matchTupi = !palTupi.isEmpty() && (respLower.equals(palTupi) || enunLower.contains("\"" + palTupi + "\"") || enunLower.contains(palTupi));
                        boolean matchPt = !tradPt.isEmpty() && (respLower.equals(tradPt) || enunLower.contains("\"" + tradPt + "\"") || respLower.contains(tradPt));

                        if (matchTupi || matchPt) {
                            fonetica = c.getFonetica();
                            traducaoPtBr = c.getTraducaoPtBr();
                            break;
                        }
                    }
                }
            }
        }

        List<ParticipanteArena> participantes = participanteArenaRepository.findBySalaIdOrderByPontuacaoTotalDescAcertosDesc(sala.getId());

        List<ParticipanteRankingDTO> ranking = new ArrayList<>();
        for (int i = 0; i < Math.min(10, participantes.size()); i++) {
            ParticipanteArena p = participantes.get(i);
            ranking.add(new ParticipanteRankingDTO(
                    p.getId(),
                    p.getApelido(),
                    p.getPontuacaoTotal(),
                    p.getAcertos(),
                    i + 1
            ));
        }

        ParticipanteStatusDTO meuStatus = null;
        if (participanteId != null) {
            for (int i = 0; i < participantes.size(); i++) {
                ParticipanteArena p = participantes.get(i);
                if (p.getId().equals(participanteId)) {
                    boolean jaRespondeu = false;
                    Boolean acertouAnterior = null;
                    Integer pontosGanhosAnterior = null;

                    if (exercicioAtual != null) {
                        Optional<RespostaArena> r = respostaArenaRepository.findByParticipanteIdAndExercicioId(p.getId(), exercicioAtual.getId());
                        if (r.isPresent()) {
                            jaRespondeu = true;
                            if (sala.getStatus() == StatusArena.REVELACAO || sala.getStatus() == StatusArena.PLACAR || sala.getStatus() == StatusArena.FINALIZADA) {
                                acertouAnterior = r.get().getAcertou();
                                pontosGanhosAnterior = r.get().getPontosGanhos();
                            }
                        }
                    }

                    meuStatus = new ParticipanteStatusDTO(
                            p.getId(),
                            p.getApelido(),
                            p.getPontuacaoTotal(),
                            p.getAcertos(),
                            i + 1,
                            jaRespondeu,
                            acertouAnterior,
                            pontosGanhosAnterior
                    );
                    break;
                }
            }
        }

        return new EstadoArenaResponseDTO(
                sala.getId(),
                sala.getCodigoPin(),
                sala.getTitulo(),
                sala.getStatus().name(),
                totalQuestoes,
                indexAtual,
                sala.getTempoPerguntaSegundos(),
                tempoRestante,
                enunciado,
                opcoes,
                respostaCorreta,
                contagemRespostas,
                fonetica,
                traducaoPtBr,
                participantes.size(),
                totalRespostas,
                ranking,
                meuStatus
        );
    }

    public EstadoArenaResponseDTO encerrarSala(String pin) {
        String pinLimpo = pin.replaceAll("\\s+", "").trim();
        SalaArena sala = salaArenaRepository.findByCodigoPin(pinLimpo)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com o PIN: " + pinLimpo));

        sala.setStatus(StatusArena.FINALIZADA);
        salaArenaRepository.save(sala);

        creditarXpFinalistas(sala);

        return obterEstado(pinLimpo, null);
    }

    private void creditarXpFinalistas(SalaArena sala) {
        List<ParticipanteArena> participantes = participanteArenaRepository.findBySalaIdOrderByPontuacaoTotalDescAcertosDesc(sala.getId());
        for (ParticipanteArena p : participantes) {
            if (p.getUsuario() != null && p.getAcertos() > 0) {
                Usuario u = p.getUsuario();
                int xpGanho = p.getAcertos() * 15; // 15 XP por questão acertada na Arena
                u.setXp(u.getXp() + xpGanho);
                usuarioRepository.save(u);
            }
        }
    }

    private String gerarPinUnico() {
        for (int i = 0; i < 50; i++) {
            int num = 100000 + random.nextInt(900000);
            String pin = String.valueOf(num);
            if (!salaArenaRepository.existsByCodigoPin(pin)) {
                return pin;
            }
        }
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

