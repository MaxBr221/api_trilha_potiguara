package br.com.tupidigital.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class GeminiPayloads {

    // Request enviado para o Gemini API
    public record Request(List<Content> contents, GenerationConfig generationConfig) {}
    public record Content(List<Part> parts) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Part(String text, InlineData inlineData) {
        public Part(String text) {
            this(text, null);
        }
        public Part(InlineData inlineData) {
            this(null, inlineData);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record InlineData(String mimeType, String data) {}

    public record GenerationConfig(String responseMimeType, Double temperature) {}

    // Resposta padrão da API do Gemini
    public record Response(List<Candidate> candidates) {}
    public record Candidate(Content content) {}

    // Resposta estruturada JSON da avaliação de grafia (2.2)
    public record FeedbackAvaliacao(
        boolean aceita,
        String feedbackPedagogico
    ) {}

    // Resposta estruturada JSON da dica (2.4)
    public record DicaResponse(
        String dica
    ) {}

    // Resposta estruturada JSON da avaliação de pronúncia / áudio
    public record FeedbackPronuncia(
        boolean aceita,
        Integer precisao,
        String transcricaoAproximada,
        String feedbackPedagogico
    ) {}
}

