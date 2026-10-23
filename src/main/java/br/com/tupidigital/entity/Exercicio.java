package br.com.tupidigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exercicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licao_id", nullable = false)
    private Licao licao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(nullable = false, length = 50)
    private String tipo; // e.g., MULTIPLA_ESCOLHA, TRADUCAO

    @Column(name = "resposta_correta", nullable = false)
    private String respostaCorreta;

    @Column(name = "pontuacao_xp", nullable = false)
    private Integer pontuacaoXp;

    @Column(name = "ordem_index", nullable = false)
    private Integer ordemIndex;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exercicio_opcoes", joinColumns = @JoinColumn(name = "exercicio_id"))
    @Column(name = "opcao")
    private List<String> opcoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
