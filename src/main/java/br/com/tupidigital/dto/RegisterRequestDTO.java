package br.com.tupidigital.dto;

import br.com.tupidigital.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank @Size(min = 3, max = 255) String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String senha,
        Perfil perfil, // optional, if null default to USER
        Integer xpInicial, // optional, for lazy registration
        java.util.UUID licaoConcluidaId // optional, for lazy registration
) {
}
