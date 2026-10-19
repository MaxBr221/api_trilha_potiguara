package br.com.tupidigital.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioPerfilDTO(
        UUID id,
        String nome,
        String email,
        Integer xp,
        Integer sequenciaAtual,
        String perfil,
        String fotoPerfil,
        LocalDateTime criadoEm
) {
    public UsuarioPerfilDTO(UUID id, String nome, String email, Integer xp, Integer sequenciaAtual, String perfil, String fotoPerfil) {
        this(id, nome, email, xp, sequenciaAtual, perfil, fotoPerfil, null);
    }
}

