package br.com.tupidigital.repository;

import br.com.tupidigital.entity.ProgressoUsuarioExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressoUsuarioExercicioRepository extends JpaRepository<ProgressoUsuarioExercicio, UUID> {
    long countByUsuarioId(UUID usuarioId);
    long countByUsuarioIdAndAcertouTrue(UUID usuarioId);
    boolean existsByUsuarioIdAndExercicioIdAndAcertouTrue(UUID usuarioId, UUID exercicioId);
}
