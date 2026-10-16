package br.com.tupidigital.dto;

public record ValidacaoRespostaResponseDTO(
        boolean correta,
        Integer xpGanho,
        String respostaCorreta,
        String feedbackPedagogico
) {
    public ValidacaoRespostaResponseDTO(boolean correta, Integer xpGanho, String respostaCorreta) {
        this(correta, xpGanho, respostaCorreta, null);
    }
}
