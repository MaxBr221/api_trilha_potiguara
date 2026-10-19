package br.com.tupidigital.dto;

public record ValidacaoAudioResponseDTO(
    boolean correta,
    Integer precisao,
    Integer xpGanho,
    String respostaCorreta,
    String transcricaoAproximada,
    String feedbackPedagogico
) {
    public ValidacaoAudioResponseDTO(boolean correta, Integer precisao, Integer xpGanho, String respostaCorreta, String feedbackPedagogico) {
        this(correta, precisao, xpGanho, respostaCorreta, null, feedbackPedagogico);
    }
}
