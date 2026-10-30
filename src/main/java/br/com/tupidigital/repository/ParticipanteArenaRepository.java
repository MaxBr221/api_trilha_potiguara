package br.com.tupidigital.repository;

import br.com.tupidigital.entity.ParticipanteArena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipanteArenaRepository extends JpaRepository<ParticipanteArena, UUID> {
    List<ParticipanteArena> findBySalaIdOrderByPontuacaoTotalDescAcertosDesc(UUID salaId);
    Optional<ParticipanteArena> findBySalaIdAndApelido(UUID salaId, String apelido);
    Optional<ParticipanteArena> findBySalaIdAndUsuarioId(UUID salaId, UUID usuarioId);
    long countBySalaId(UUID salaId);
}

