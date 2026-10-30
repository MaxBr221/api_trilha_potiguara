package br.com.tupidigital.repository;

import br.com.tupidigital.entity.RespostaArena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RespostaArenaRepository extends JpaRepository<RespostaArena, UUID> {
    List<RespostaArena> findBySalaIdAndExercicioId(UUID salaId, UUID exercicioId);
    Optional<RespostaArena> findByParticipanteIdAndExercicioId(UUID participanteId, UUID exercicioId);
    long countBySalaIdAndExercicioId(UUID salaId, UUID exercicioId);
}

