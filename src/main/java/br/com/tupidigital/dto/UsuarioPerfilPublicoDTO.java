package br.com.tupidigital.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioPerfilPublicoDTO(
        UUID id,
        String nome,
        Integer xp,
        Integer sequenciaAtual,
        String fotoPerfil,
        int totalAmigos,
        boolean isAmigo,
        LocalDateTime criadoEm
) {
    public UsuarioPerfilPublicoDTO(UUID id, String nome, Integer xp, Integer sequenciaAtual, String fotoPerfil, int totalAmigos, boolean isAmigo) {
        this(id, nome, xp, sequenciaAtual, fotoPerfil, totalAmigos, isAmigo, null);
    }
}

