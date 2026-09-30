package br.com.tupidigital.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaDTO(
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email está num formato inválido") String email
) {}
