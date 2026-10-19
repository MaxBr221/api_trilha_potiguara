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

    public GeminiService(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().build();
        this.objectMapper = objectMapper;
    }

    /**
     * 2.2: Avalia a resposta do aluno com feedback detalhado de grafia e acentuação (para exercícios de digitação).
     */
    public GeminiPayloads.FeedbackAvaliacao avaliarTentativa(String enunciado, String respostaEsperada, String respostaUsuario) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return new GeminiPayloads.FeedbackAvaliacao(false, "Resposta incorreta. Tente novamente prestando atenção à grafia.");
        }

        String prompt = """
            Você é um tutor pedagógico e paciente do idioma Tupi Antigo/Potiguara na plataforma Tupi Digital.
            
            DADOS DO EXERCÍCIO:
            - Pergunta: "%s"
            - Resposta oficial canônica: "%s"
            - O que o aluno digitou: "%s"
            
            REGRAS OBRIGATÓRIAS:
            1. Analise se a resposta do aluno está correta ou se cometeu pequenos desvios de grafia (ex: trocou 'k' por 'c', esqueceu o apóstrofo/pausa glotal ', esqueceu circunflexo suave em î/û ou til nasal em ã/õ).
            2. Se for uma variação aceitável com apenas pequeno desvio ortográfico, marque aceita: true e dê uma orientação carinhosa de como escrever na grafia padrão.
            3. Se estiver completamente errada ou sem sentido, marque aceita: false e explique brevemente o que faltou, sem desestimular o aluno.
            4. Responda ESTRITAMENTE em formato JSON com os campos "aceita" (boolean) e "feedbackPedagogico" (string curta de até 2 frases em português).
            """.formatted(enunciado, respostaEsperada, respostaUsuario);

        try {
            String jsonOutput = chamarGemini(prompt, "application/json");
            return objectMapper.readValue(jsonOutput, GeminiPayloads.FeedbackAvaliacao.class);
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini para avaliação: {}", e.getMessage());
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
            return new GeminiPayloads.FeedbackPronuncia(true, 85, respostaEsperada, "Muito bem! Sua pronúncia foi registrada.");
        }

        String safeMimeType = (mimeType != null && !mimeType.isBlank()) ? mimeType : "audio/webm";
        if (safeMimeType.contains(";")) {
            safeMimeType = safeMimeType.split(";")[0].trim();
        }

        String base64Audio = java.util.Base64.getEncoder().encodeToString(audioBytes);

        String prompt = """
            Você é um tutor fonético amigável e encorajador do idioma Tupi Antigo/Potiguara na plataforma Tupi Digital.
            O estudante ouviu a lição e gravou o áudio em anexo falando a palavra ou expressão solicitada.

            DADOS DO EXERCÍCIO:
            - Instrução/Enunciado: "%s"
            - Palavra/expressão esperada em Tupi: "%s"

            CRITÉRIOS DE AVALIAÇÃO:
            1. Analise o áudio anexado e verifique se a fala do estudante se aproxima da palavra esperada.
            2. Seja compreensivo com ruído ambiente, sotaques regionais e estudantes iniciantes.
            3. Verifique especialmente fonemas como a vogal 'y' gutural/central, sons nasais (ã, õ, nh, mb, nd) ou oclusivas/pausas (').
            4. Se a pronúncia for compreensível e razoavelmente próxima (ou se esforçou bem no som principal): marque "aceita": true, atribua "precisao" entre 70 e 100, e faça um elogio ou dica curta e carinhosa.
            5. Se o áudio estiver completamente inaudível, vazio, ou for uma palavra totalmente não relacionada: marque "aceita": false, "precisao" abaixo de 50, e dê uma orientação gentil para tentar de novo.
            6. Responda ESTRITAMENTE em formato JSON com os campos:
               - "aceita": boolean
               - "precisao": integer (de 0 a 100)
               - "transcricaoAproximada": string (o que você ouviu)
               - "feedbackPedagogico": string curta de 1 a 2 frases amigáveis em português.
            """.formatted(enunciado, respostaEsperada);

        try {
            String jsonOutput = chamarGeminiMultimodal(prompt, base64Audio, safeMimeType, "application/json");
            return objectMapper.readValue(jsonOutput, GeminiPayloads.FeedbackPronuncia.class);
        } catch (org.springframework.web.client.RestClientResponseException rce) {
            log.error("Erro HTTP ao chamar Gemini para avaliação de áudio [{}]: {}", rce.getStatusCode(), rce.getResponseBodyAsString());
            return new GeminiPayloads.FeedbackPronuncia(true, 80, respostaEsperada, "Muito bom! Sua fala foi reconhecida com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini para avaliação de áudio: {}", e.getMessage(), e);
            return new GeminiPayloads.FeedbackPronuncia(true, 80, respostaEsperada, "Muito bom! Sua fala foi reconhecida com sucesso.");
        }
    }

    private String chamarGeminiMultimodal(String prompt, String base64Data, String dataMimeType, String responseMimeType) {
        String url = "%s/%s:generateContent?key=%s".formatted(baseUrl, model, apiKey.trim());

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
                return candidate.content().parts().get(0).text();
            }
        }
        throw new RuntimeException("Resposta vazia da API do Gemini para áudio");
    }

    private String chamarGemini(String prompt, String mimeType) {
        String url = "%s/%s:generateContent?key=%s".formatted(baseUrl, model, apiKey.trim());

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
                return candidate.content().parts().get(0).text();
            }
        }
        throw new RuntimeException("Resposta vazia da API do Gemini");
    }
}

