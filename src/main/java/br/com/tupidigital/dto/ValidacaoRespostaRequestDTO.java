package br.com.tupidigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValidacaoRespostaRequestDTO(
        @NotBlank(message = "A resposta não pode ser nula ou vazia")
        @Size(max = 255, message = "A resposta excede o tamanho máximo permitido")
        String respostaUsuario
) {
}
