package br.com.tupidigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "respostas_arena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class RespostaArena {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaArena sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private ParticipanteArena participante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;

    @Column(name = "opcao_escolhida")
    private String opcaoEscolhida;

    @Column(nullable = false)
    @Builder.Default
    private Boolean acertou = false;

    @Column(name = "tempo_resposta_ms")
    private Integer tempoRespostaMs;

    @Column(name = "pontos_ganhos", nullable = false)
    @Builder.Default
    private Integer pontosGanhos = 0;

    @CreationTimestamp
    @Column(name = "respondido_em", nullable = false, updatable = false)
    private LocalDateTime respondidoEm;
}

