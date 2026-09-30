package br.com.tupidigital.dto;

import br.com.tupidigital.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank @Size(min = 3, max = 255) 
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]*$", message = "O nome deve conter apenas letras e espaços")
        String nome,
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "must be a well-formed email address") String email,
        @NotBlank @Size(min = 6) String senha,
        Perfil perfil, // optional, if null default to USER
        Integer xpInicial, // optional, for lazy registration
        java.util.UUID licaoConcluidaId // optional, for lazy registration
) {
}
