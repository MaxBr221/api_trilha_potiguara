package br.com.tupidigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @Column(nullable = false)
    private Integer xp = 0;

    @Column(name = "sequencia_atual", nullable = false)
    private Integer sequenciaAtual = 0;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @Column(name = "ultima_atividade")
    private LocalDate ultimaAtividade;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "amigos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private java.util.Set<Usuario> amigos = new java.util.HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.perfil.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Construtores ---
    public Usuario() {
    }

    public Usuario(UUID id, String nome, String email, String senha, Perfil perfil, Integer xp, Integer sequenciaAtual, LocalDate ultimaAtividade, LocalDateTime criadoEm, LocalDateTime atualizadoEm, String fotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.xp = xp;
        this.sequenciaAtual = sequenciaAtual;
        this.ultimaAtividade = ultimaAtividade;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.fotoPerfil = fotoPerfil;
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public Integer getXp() { return xp; }
    public void setXp(Integer xp) { this.xp = xp; }

    public Integer getSequenciaAtual() { return sequenciaAtual; }
    public void setSequenciaAtual(Integer sequenciaAtual) { this.sequenciaAtual = sequenciaAtual; }

    public LocalDate getUltimaAtividade() { return ultimaAtividade; }
    public void setUltimaAtividade(LocalDate ultimaAtividade) { this.ultimaAtividade = ultimaAtividade; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public java.util.Set<Usuario> getAmigos() { return amigos; }
    public void setAmigos(java.util.Set<Usuario> amigos) { this.amigos = amigos; }

    public void adicionarAmigo(Usuario amigo) {
        this.amigos.add(amigo);
    }

    public void removerAmigo(Usuario amigo) {
        this.amigos.remove(amigo);
    }
}
