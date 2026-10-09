package br.com.tupidigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @NotBlank @Size(min = 3, max = 255) 
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]*$", message = "O nome deve conter apenas letras e espaços")
        String nome,
        
        @Size(max = 1048576, message = "A foto de perfil é muito grande (máximo 1MB)")
        String fotoPerfil
) {}
