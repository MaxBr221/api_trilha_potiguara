package br.com.tupidigital.dto;

import java.util.List;
import java.util.UUID;

public class ArenaDTOs {

    public record TrilhaArenaDTO(
        UUID id,
        String nome,
        String icon,
        String corBase,
        List<ModuloArenaDTO> modulos
    ) {}

    public record ModuloArenaDTO(
        UUID id,
        String titulo,
        String descricao,
        int totalQuestoes
    ) {}

    public record CriarArenaRequestDTO(
        List<UUID> moduloIds,
        Integer quantidadeQuestoes,
        Integer tempoPorPergunta,
        String titulo
    ) {}

    public record CriarArenaResponseDTO(
        UUID salaId,
        String codigoPin,
        String titulo,
        int totalQuestoes,
        int tempoPorPergunta,
        String status
    ) {}

    public record EntrarArenaRequestDTO(
        String codigoPin,
        String apelido
    ) {}

    public record EntrarArenaResponseDTO(
        UUID salaId,
        UUID participanteId,
        String codigoPin,
        String apelido,
        String statusSala
    ) {}

    public record ResponderArenaRequestDTO(
        UUID participanteId,
        String opcaoEscolhida,
        Integer tempoRespostaMs
    ) {}

    public record ResponderArenaResponseDTO(
        boolean registrado,
        int totalRespostasNestaQuestao,
        String mensagem
    ) {}

    public record ParticipanteRankingDTO(
        UUID id,
        String apelido,
        int pontuacaoTotal,
        int acertos,
        int posicao
    ) {}

    public record ParticipanteStatusDTO(
        UUID id,
        String apelido,
        int pontuacaoTotal,
        int acertos,
        int posicao,
        boolean jaRespondeuEstaQuestao,
        Boolean acertouQuestaoAnterior,
        Integer pontosGanhosQuestaoAnterior
    ) {}

    public record EstadoArenaResponseDTO(
        UUID salaId,
        String codigoPin,
        String titulo,
        String status,
        int totalQuestoes,
        int perguntaAtualIndex,
        int tempoPerguntaSegundos,
        Integer tempoRestanteSegundos,
        String enunciado,
        List<String> opcoes,
        String respostaCorreta,
        java.util.Map<String, Long> contagemRespostasPorOpcao,
        String fonetica,
        String traducaoPtBr,
        int totalParticipantes,
        int totalRespostasRecebidas,
        List<ParticipanteRankingDTO> ranking,
        ParticipanteStatusDTO meuStatus
    ) {}
}

