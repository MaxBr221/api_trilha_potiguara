package br.com.tupidigital.repository;

import br.com.tupidigital.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExercicioRepository extends JpaRepository<Exercicio, UUID> {
    List<Exercicio> findByLicaoIdOrderByOrdemIndexAsc(UUID licaoId);

    @Query("SELECT e FROM Exercicio e WHERE e.licao.modulo.id IN :moduloIds")
    List<Exercicio> findByModuloIds(@Param("moduloIds") List<UUID> moduloIds);

    @Query("SELECT e FROM Exercicio e WHERE e.licao.modulo.id = :moduloId")
    List<Exercicio> findByModuloId(@Param("moduloId") UUID moduloId);

    @Query("SELECT count(e) FROM Exercicio e WHERE e.licao.modulo.id = :moduloId")
    long countByModuloId(@Param("moduloId") UUID moduloId);
}
