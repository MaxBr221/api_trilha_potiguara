package br.com.tupidigital.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaDTO(
    @NotBlank @Pattern(regexp = "^.+@.+\\..+$", message = "Email está num formato inválido") String email
) {}
