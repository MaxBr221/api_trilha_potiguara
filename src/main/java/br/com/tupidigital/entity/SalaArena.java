package br.com.tupidigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "salas_arena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class SalaArena {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_pin", nullable = false, unique = true, length = 6)
    private String codigoPin;

    @Column(length = 150)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Usuario professor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatusArena status = StatusArena.LOBBY;

    @Column(name = "pergunta_atual_index", nullable = false)
    @Builder.Default
    private Integer perguntaAtualIndex = 0;

    @Column(name = "tempo_pergunta_segundos", nullable = false)
    @Builder.Default
    private Integer tempoPerguntaSegundos = 20;

    @Column(name = "tempo_inicio_pergunta")
    private LocalDateTime tempoInicioPergunta;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordemIndex ASC")
    @Builder.Default
    private List<SalaArenaQuestao> questoes = new ArrayList<>();

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("pontuacaoTotal DESC, acertos DESC")
    @Builder.Default
    private List<ParticipanteArena> participantes = new ArrayList<>();
}

