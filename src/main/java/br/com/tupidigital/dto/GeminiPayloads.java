package br.com.tupidigital.dto;

import java.util.List;

public class GeminiPayloads {

    // Request enviado para o Gemini API
    public record Request(List<Content> contents, GenerationConfig generationConfig) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
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
}
