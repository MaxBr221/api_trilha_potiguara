package br.com.tupidigital.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaDTO(
    @NotBlank String email
) {}
