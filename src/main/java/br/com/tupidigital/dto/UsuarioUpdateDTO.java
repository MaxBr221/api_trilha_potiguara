package br.com.tupidigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @Size(min = 3, max = 255) 
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]*$", message = "O nome deve conter apenas letras e espaços")
        String nome,
        
        @Size(max = 5242880, message = "A foto de perfil é muito grande (máximo 5MB)")
        @Pattern(regexp = "^$|^data:image/(jpeg|png|webp);base64,[A-Za-z0-9+/=]+$",
                message = "A foto de perfil deve ser uma imagem Base64 válida (jpeg, png ou webp)")
        String fotoPerfil
) {}
