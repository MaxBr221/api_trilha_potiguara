package br.com.tupidigital.repository;

import br.com.tupidigital.entity.SalaArena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SalaArenaRepository extends JpaRepository<SalaArena, UUID> {
    Optional<SalaArena> findByCodigoPin(String codigoPin);
    boolean existsByCodigoPin(String codigoPin);
}

