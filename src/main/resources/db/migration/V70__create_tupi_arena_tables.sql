-- V70: Criação das tabelas da funcionalidade 'Tupi Arena' (Batalha ao Vivo em Sala de Aula)

CREATE TABLE IF NOT EXISTS salas_arena (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_pin VARCHAR(6) NOT NULL UNIQUE,
    titulo VARCHAR(150),
    professor_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'LOBBY',
    pergunta_atual_index INT NOT NULL DEFAULT 0,
    tempo_pergunta_segundos INT NOT NULL DEFAULT 20,
    tempo_inicio_pergunta TIMESTAMP,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sala_arena_questoes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sala_id UUID NOT NULL REFERENCES salas_arena(id) ON DELETE CASCADE,
    exercicio_id UUID NOT NULL REFERENCES exercicios(id) ON DELETE CASCADE,
    ordem_index INT NOT NULL,
    CONSTRAINT uk_sala_exercicio UNIQUE (sala_id, exercicio_id)
);

CREATE TABLE IF NOT EXISTS participantes_arena (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sala_id UUID NOT NULL REFERENCES salas_arena(id) ON DELETE CASCADE,
    usuario_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
    apelido VARCHAR(100) NOT NULL,
    pontuacao_total INT NOT NULL DEFAULT 0,
    acertos INT NOT NULL DEFAULT 0,
    conectado BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_sala_apelido UNIQUE (sala_id, apelido)
);

CREATE TABLE IF NOT EXISTS respostas_arena (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sala_id UUID NOT NULL REFERENCES salas_arena(id) ON DELETE CASCADE,
    participante_id UUID NOT NULL REFERENCES participantes_arena(id) ON DELETE CASCADE,
    exercicio_id UUID NOT NULL REFERENCES exercicios(id) ON DELETE CASCADE,
    opcao_escolhida VARCHAR(255),
    acertou BOOLEAN NOT NULL DEFAULT FALSE,
    tempo_resposta_ms INT,
    pontos_ganhos INT NOT NULL DEFAULT 0,
    respondido_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_participante_exercicio UNIQUE (participante_id, exercicio_id)
);

CREATE INDEX IF NOT EXISTS idx_salas_arena_pin ON salas_arena(codigo_pin);
CREATE INDEX IF NOT EXISTS idx_participantes_arena_sala ON participantes_arena(sala_id);
CREATE INDEX IF NOT EXISTS idx_respostas_arena_sala ON respostas_arena(sala_id);
CREATE INDEX IF NOT EXISTS idx_sala_questoes_sala ON sala_arena_questoes(sala_id);

