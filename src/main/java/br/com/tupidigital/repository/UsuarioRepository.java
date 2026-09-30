package br.com.tupidigital.repository;

import br.com.tupidigital.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    UserDetails findByEmail(String email);
    boolean existsByEmail(String email);

    java.util.List<Usuario> findTop20ByIdNotOrderByXpDesc(UUID id);

    @org.springframework.data.jpa.repository.Query("SELECT u FROM Usuario u WHERE u.id != :id AND (LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%')))")
    java.util.List<Usuario> buscarPorNomeOuEmail(@org.springframework.data.repository.query.Param("termo") String termo, @org.springframework.data.repository.query.Param("id") UUID id, org.springframework.data.domain.Pageable pageable);

}
