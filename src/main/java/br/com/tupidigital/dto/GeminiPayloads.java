package br.com.tupidigital.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class GeminiPayloads {

    // Request enviado para o Gemini API
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(List<Content> contents, GenerationConfig generationConfig) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(List<Part> parts) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Part(String text, InlineData inlineData) {
        public Part(String text) {
            this(text, null);
        }
        public Part(InlineData inlineData) {
            this(null, inlineData);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record InlineData(String mimeType, String data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GenerationConfig(String responseMimeType, Double temperature) {}

    // Resposta padrão da API do Gemini
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<Candidate> candidates) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(Content content) {}

    // Resposta estruturada JSON da avaliação de grafia (2.2)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FeedbackAvaliacao(
        boolean aceita,
        String feedbackPedagogico
    ) {}

    // Resposta estruturada JSON da dica (2.4)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DicaResponse(
        String dica
    ) {}

    // Resposta estruturada JSON da avaliação de pronúncia / áudio
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FeedbackPronuncia(
        boolean aceita,
        Integer precisao,
        String transcricaoAproximada,
        String feedbackPedagogico
    ) {}
}

