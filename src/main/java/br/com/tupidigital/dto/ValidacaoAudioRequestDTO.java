package br.com.tupidigital.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidacaoAudioRequestDTO(
    @NotBlank(message = "O áudio em Base64 não pode estar vazio")
    String audioBase64,
    String mimeType
) {}
