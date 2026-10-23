package br.com.tupidigital.dto;

import br.com.tupidigital.entity.Exercicio;

import java.util.List;
import java.util.UUID;

public record ExercicioResponseDTO(
        UUID id,
        String enunciado,
        String tipo,
        List<String> opcoes,
        Integer pontuacaoXp,
        Integer ordemIndex
) {
    public ExercicioResponseDTO(Exercicio exercicio) {
        this(
                exercicio.getId(),
                exercicio.getEnunciado(),
                exercicio.getTipo(),
                exercicio.getOpcoes() != null ? new java.util.ArrayList<>(exercicio.getOpcoes()) : java.util.List.of(),
                exercicio.getPontuacaoXp(),
                exercicio.getOrdemIndex()
        );
    }
}
