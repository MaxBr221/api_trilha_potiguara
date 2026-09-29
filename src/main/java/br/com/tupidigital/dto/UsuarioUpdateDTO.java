package br.com.tupidigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @NotBlank @Size(min = 3, max = 255) String nome,
        String fotoPerfil
) {}
