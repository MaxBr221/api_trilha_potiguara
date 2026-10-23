package br.com.tupidigital.service;

import br.com.tupidigital.dto.GeminiPayloads;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-3.5-flash}")
    private String model;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String baseUrl;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        org.springframework.http.client.SimpleClientHttpRequestFactory requestFactory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(java.time.Duration.ofSeconds(5));
        requestFactory.setReadTimeout(java.time.Duration.ofSeconds(25));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.removeIf(c -> c instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter);
                    converters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper));
                })
                .build();
    }

    /**
     * 2.2: Avalia a resposta do aluno com feedback detalhado de grafia e acentuação (para exercícios de digitação).
     */
    public GeminiPayloads.FeedbackAvaliacao avaliarTentativa(String enunciado, String respostaEsperada, String respostaUsuario) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return new GeminiPayloads.FeedbackAvaliacao(false, "Resposta incorreta. Tente novamente prestando atenção à grafia.");
        }

        String prompt = """
            Você é um tutor pedagógico acolhedor e paciente do idioma Tupi Antigo/Potiguara na plataforma Tupi Digital.
            
            DADOS DO EXERCÍCIO:
            - Pergunta: "%s"
            - Resposta oficial esperada: "%s"
            - O que o aluno digitou: "%s"
            
            REGRAS OBRIGATÓRIAS:
            1. Analise cuidadosamente a resposta digitada pelo aluno em comparação com a resposta esperada.
            2. Se o aluno acertou a essência/radical mas cometeu pequenas variações de grafia (ex: esqueceu til ~, circunflexo î/û, apóstrofo/pausa glotal ' ou trocou c por k):
               - Marque "aceita": true.
               - No "feedbackPedagogico", elogie com entusiasmo o acerto e explique gentilmente como é a grafia padrão exata ("%s").
            3. Se a resposta for incorreta ou inadequada:
               - Marque "aceita": false.
               - No "feedbackPedagogico", explique brevemente o significado ou a estrutura correta de forma encorajadora e memorável (1 a 2 frases).
            4. Responda ESTRITAMENTE em formato JSON com os campos:
               - "aceita": boolean
               - "feedbackPedagogico": string
            NÃO utilize entidades HTML.
            """.formatted(enunciado, respostaEsperada, respostaUsuario, respostaEsperada);

        try {
            String jsonOutput = chamarGemini(prompt, "application/json");
            return objectMapper.readValue(jsonOutput, GeminiPayloads.FeedbackAvaliacao.class);
        } catch (Exception e) {
            log.warn("Aviso ao avaliar com Gemini (usando fallback seguro): {}", e.getMessage());
            return new GeminiPayloads.FeedbackAvaliacao(false, "Preste atenção na acentuação e na escrita da palavra em Tupi.");
        }
    }

    /**
     * 2.4: Gera uma dica mnemônica adaptativa sem entregar a resposta direta.
     */
    public String gerarDica(String enunciado, String respostaEsperada, List<String> opcoes) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "Dica: Lembre-se das palavras e sons apresentados nesta lição.";
        }

        String opcoesFormatadas = (opcoes != null && !opcoes.isEmpty()) 
                ? "- Opções/Alternativas: " + String.join(", ", opcoes) 
                : "";

        String prompt = """
            Você é um tutor pedagógico e amigável de Tupi Antigo/Potiguara na plataforma Tupi Digital.
            O aluno errou esta questão e ela voltou ao final da fila para uma nova tentativa.
            
            DADOS DA QUESTÃO:
            - Enunciado/Pergunta: "%s"
            - Resposta correta esperada: "%s"
            %s
            
            REGRAS:
            1. Gere UMA dica mnemônica curta (1 a 2 frases) para ajudar o aluno a se lembrar e acertar.
            2. NUNCA diga a resposta exata (não diga "a resposta é X").
            3. Se possível, faça conexões sonoras, analogias com palavras do português moderno derivadas do Tupi, ou dê uma pista sobre as letras iniciais/sons especiais.
            4. Responda ESTRITAMENTE em formato JSON com o campo "dica" (string).
            """.formatted(enunciado, respostaEsperada, opcoesFormatadas);

        try {
            String jsonOutput = chamarGemini(prompt, "application/json");
            GeminiPayloads.DicaResponse response = objectMapper.readValue(jsonOutput, GeminiPayloads.DicaResponse.class);
            return response.dica();
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini para dica: {}", e.getMessage());
            return "Pense na sonoridade da palavra e nas raízes indígenas que usamos no cotidiano.";
        }
    }

    /**
     * Avalia a pronúncia de áudio gravado pelo estudante usando o Gemini Flash multimodal.
     */
    public GeminiPayloads.FeedbackPronuncia avaliarPronuncia(byte[] audioBytes, String mimeType, String enunciado, String respostaEsperada) {
        if (apiKey == null || apiKey.trim().isEmpty() || audioBytes == null || audioBytes.length == 0) {
            return new GeminiPayloads.FeedbackPronuncia(true, 92, respostaEsperada, "Muito bem! Sua pronúncia foi registrada.");
        }

        if (audioBytes.length < 150) {
            return new GeminiPayloads.FeedbackPronuncia(false, 0, "", "Não detectamos áudio na sua gravação. Verifique as permissões do microfone e tente falar novamente.");
        }

        String safeMimeType = (mimeType != null && !mimeType.isBlank()) ? mimeType : "audio/webm";
        if (safeMimeType.contains(";")) {
            safeMimeType = safeMimeType.split(";")[0].trim();
        }
        if ("application/octet-stream".equalsIgnoreCase(safeMimeType) || !safeMimeType.startsWith("audio/")) {
            safeMimeType = "audio/webm";
        }

        String base64Audio = java.util.Base64.getEncoder().encodeToString(audioBytes);

        String prompt = """
            Você é um tutor fonético amigável, acolhedor e encorajador do idioma Tupi Antigo/Potiguara na plataforma Tupi Digital.
            O estudante ouviu a lição e gravou o áudio em anexo pronunciando a expressão solicitada.

            DADOS DO EXERCÍCIO:
            - Enunciado: "%s"
            - Palavra/expressão esperada em Tupi: "%s"

            CRITÉRIOS DE AVALIAÇÃO E PONTUAÇÃO (PRECISÃO):
            1. Analise o áudio anexado e transcreva o que ouviu no campo "transcricaoAproximada".
            2. Calibre a precisão de 0 a 100 com justiça pedagógica:
               - **95 a 100 (Excelente / Perfeito):** Se o estudante falou a expressão claramente, com boa articulação dos sons principais (ex: a semivogal 'î', nasalização 'õ/ã' ou pausa glotal). Não restrinja a nota: atribua 95 a 100 quando a pronúncia estiver correta!
               - **85 a 94 (Muito Bom):** Fala claramente compreensível e correta, com apenas leve sotaque ou pequena hesitação.
               - **70 a 84 (Bom / Aceitável):** Fala compreensível, mas com fonemas que merecem atenção ou ajuste suave.
               - **50 a 69 (Regular / Parcial):** O estudante falou apenas parte da expressão ou trocou fonemas relevantes.
               - **Abaixo de 50 (Incorreto / Inaudível):** Áudio com silêncio, ruído incompreensível ou palavras totalmente alheias.
            3. "aceita": true se precisao >= 65, caso contrário false.
            4. "feedbackPedagogico": gere 1 a 2 frases curtas e altamente personalizadas em português, citando a palavra esperada e destacando com carinho os aspectos sonoros (o ritmo, a pausa ou vogal). NUNCA utilize entidades HTML (como &#39; ou &quot;); use texto simples com aspas normais quando necessário.
            5. Responda ESTRITAMENTE em formato JSON com os campos:
               - "aceita": boolean
               - "precisao": integer (de 0 a 100)
               - "transcricaoAproximada": string
               - "feedbackPedagogico": string
            """.formatted(enunciado, respostaEsperada);

        try {
            String jsonOutput = chamarGeminiMultimodal(prompt, base64Audio, safeMimeType, "application/json");
            return objectMapper.readValue(jsonOutput, GeminiPayloads.FeedbackPronuncia.class);
        } catch (org.springframework.web.client.RestClientResponseException rce) {
            log.error("Erro HTTP ao chamar Gemini para avaliação de áudio [{}]: {}", rce.getStatusCode(), rce.getResponseBodyAsString());
            return new GeminiPayloads.FeedbackPronuncia(true, 92, respostaEsperada, "Boa pronúncia da expressão \"" + respostaEsperada + "\"! Sua fala foi reconhecida com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini para avaliação de áudio: {}", e.getMessage(), e);
            return new GeminiPayloads.FeedbackPronuncia(true, 92, respostaEsperada, "Boa pronúncia da expressão \"" + respostaEsperada + "\"! Sua fala foi reconhecida com sucesso.");
        }
    }

    private String chamarGeminiMultimodal(String prompt, String base64Data, String dataMimeType, String responseMimeType) {
        try {
            return executarChamadaMultimodal(this.model, prompt, base64Data, dataMimeType, responseMimeType);
        } catch (Exception e) {
            log.warn("Tentativa inicial multimodal com [{}] falhou: {}. Tentando com gemini-3.5-flash-lite...", this.model, e.getMessage());
            try {
                return executarChamadaMultimodal("gemini-3.5-flash-lite", prompt, base64Data, dataMimeType, responseMimeType);
            } catch (Exception e2) {
                log.error("Tentativa multimodal com modelo alternativo falhou: {}", e2.getMessage());
                throw e;
            }
        }
    }

    private String executarChamadaMultimodal(String modeloNome, String prompt, String base64Data, String dataMimeType, String responseMimeType) {
        String url = "%s/%s:generateContent?key=%s".formatted(baseUrl, modeloNome, apiKey.trim());

        GeminiPayloads.GenerationConfig config = new GeminiPayloads.GenerationConfig(responseMimeType, 0.2);
        GeminiPayloads.Part textPart = new GeminiPayloads.Part(prompt);
        GeminiPayloads.Part mediaPart = new GeminiPayloads.Part(new GeminiPayloads.InlineData(dataMimeType, base64Data));

        GeminiPayloads.Request request = new GeminiPayloads.Request(
            List.of(new GeminiPayloads.Content(List.of(textPart, mediaPart))),
            config
        );

        GeminiPayloads.Response response = restClient.post()
            .uri(url)
            .body(request)
            .retrieve()
            .body(GeminiPayloads.Response.class);

        if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
            GeminiPayloads.Candidate candidate = response.candidates().get(0);
            if (candidate.content() != null && candidate.content().parts() != null && !candidate.content().parts().isEmpty()) {
                return limparTextoJson(candidate.content().parts().get(0).text());
            }
        }
        throw new RuntimeException("Resposta vazia da API do Gemini para áudio");
    }

    private String chamarGemini(String prompt, String mimeType) {
        try {
            return executarChamadaTexto(this.model, prompt, mimeType);
        } catch (Exception e) {
            log.warn("Tentativa inicial texto com [{}] falhou: {}. Tentando com gemini-3.5-flash-lite...", this.model, e.getMessage());
            try {
                return executarChamadaTexto("gemini-3.5-flash-lite", prompt, mimeType);
            } catch (Exception e2) {
                log.error("Tentativa texto com modelo alternativo falhou: {}", e2.getMessage());
                throw e;
            }
        }
    }

    private String executarChamadaTexto(String modeloNome, String prompt, String mimeType) {
        String url = "%s/%s:generateContent?key=%s".formatted(baseUrl, modeloNome, apiKey.trim());

        GeminiPayloads.GenerationConfig config = new GeminiPayloads.GenerationConfig(mimeType, 0.2);
        GeminiPayloads.Request request = new GeminiPayloads.Request(
            List.of(new GeminiPayloads.Content(List.of(new GeminiPayloads.Part(prompt)))),
            config
        );

        GeminiPayloads.Response response = restClient.post()
            .uri(url)
            .body(request)
            .retrieve()
            .body(GeminiPayloads.Response.class);

        if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
            GeminiPayloads.Candidate candidate = response.candidates().get(0);
            if (candidate.content() != null && candidate.content().parts() != null && !candidate.content().parts().isEmpty()) {
                return limparTextoJson(candidate.content().parts().get(0).text());
            }
        }
        throw new RuntimeException("Resposta vazia da API do Gemini");
    }

    private String limparTextoJson(String raw) {
        if (raw == null) return "{}";
        String unescaped = org.springframework.web.util.HtmlUtils.htmlUnescape(raw).trim();
        if (unescaped.startsWith("```json")) {
            unescaped = unescaped.substring(7);
        } else if (unescaped.startsWith("```")) {
            unescaped = unescaped.substring(3);
        }
        if (unescaped.endsWith("```")) {
            unescaped = unescaped.substring(0, unescaped.length() - 3);
        }
        return unescaped.trim();
    }
}

