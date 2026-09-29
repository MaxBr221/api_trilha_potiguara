package br.com.tupidigital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaDTO(
    @NotBlank @Email String email
) {}
