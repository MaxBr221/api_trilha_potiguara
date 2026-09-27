-- ==========================================
-- V47: Corrigir Lição 2 do Módulo 7 e misturar exercícios
-- ==========================================

-- 1. Limpar os exercícios atuais da Lição 2 do Módulo 7 ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2')
DELETE FROM exercicio_opcoes WHERE exercicio_id IN (
    SELECT id FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2'
);
DELETE FROM progresso_usuario_exercicio WHERE exercicio_id IN (
    SELECT id FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2'
);
DELETE FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2';
DELETE FROM licao_conteudo WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2';

-- 2. Adicionar os conteúdos no banco
-- Inserir kybyra (irmão)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
SELECT 'd15b2e7c-89fa-41bd-b6c8-54e7def690ac', 'kybyra', 'irmão (de homem)', 'kybyra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'kybyra');

INSERT INTO licao_conteudo (licao_id, conteudo_id) 
SELECT 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', id FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'kybyra' LIMIT 1;

-- Inserir rendyra (irmã)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
SELECT 'c34a2e5f-14ab-45cd-a912-32a5bcd478fe', 'rendyra', 'irmã', 'rendyra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'rendyra');

INSERT INTO licao_conteudo (licao_id, conteudo_id) 
SELECT 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', id FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'rendyra' LIMIT 1;

-- Inserir amõia (avô)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
SELECT 'b47c9a2d-35bc-48de-b678-43d6cde589ac', 'amõia', 'avô', 'amõia', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'amõia');

INSERT INTO licao_conteudo (licao_id, conteudo_id) 
SELECT 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', id FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'amõia' LIMIT 1;

-- Inserir aryîa (avó)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
SELECT 'e81f5b4a-26cd-49ef-c789-54e7def690bd', 'aryîa', 'avó', 'aryîa', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'aryîa');

INSERT INTO licao_conteudo (licao_id, conteudo_id) 
SELECT 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', id FROM conteudos_linguisticos WHERE lower(palavra_tupi) = 'aryîa' LIMIT 1;

-- 3. Inserir exercícios
-- Ex 1 (PT -> Tupi): irmão
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('d8a7c2b1-5e9f-43d2-a1b4-7f8e9c0d1b2a', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "irmão (de m.)" em Tupi?', 'MULTIPLA_ESCOLHA', 'kybyra', 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('d8a7c2b1-5e9f-43d2-a1b4-7f8e9c0d1b2a', 'kybyra'), ('d8a7c2b1-5e9f-43d2-a1b4-7f8e9c0d1b2a', 'rendyra'), ('d8a7c2b1-5e9f-43d2-a1b4-7f8e9c0d1b2a', 'amõia'), ('d8a7c2b1-5e9f-43d2-a1b4-7f8e9c0d1b2a', 'aryîa');

-- Ex 2 (PT -> Tupi): irmã
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('f4b3e2c1-6a8d-4b9e-b2c5-8d9e0f1a2b3c', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "irmã" em Tupi?', 'MULTIPLA_ESCOLHA', 'rendyra', 10, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('f4b3e2c1-6a8d-4b9e-b2c5-8d9e0f1a2b3c', 'kybyra'), ('f4b3e2c1-6a8d-4b9e-b2c5-8d9e0f1a2b3c', 'rendyra'), ('f4b3e2c1-6a8d-4b9e-b2c5-8d9e0f1a2b3c', 'amõia'), ('f4b3e2c1-6a8d-4b9e-b2c5-8d9e0f1a2b3c', 'aryîa');

-- Ex 3 (PT -> Tupi): avô
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "avô" em Tupi?', 'MULTIPLA_ESCOLHA', 'amõia', 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c', 'kybyra'), ('a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c', 'rendyra'), ('a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c', 'amõia'), ('a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c', 'aryîa');

-- Ex 4 (PT -> Tupi): avó
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('c3d4e5f6-a7b8-49c0-1d2e-3f4a5b6c7d8e', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "avó" em Tupi?', 'MULTIPLA_ESCOLHA', 'aryîa', 10, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('c3d4e5f6-a7b8-49c0-1d2e-3f4a5b6c7d8e', 'kybyra'), ('c3d4e5f6-a7b8-49c0-1d2e-3f4a5b6c7d8e', 'rendyra'), ('c3d4e5f6-a7b8-49c0-1d2e-3f4a5b6c7d8e', 'amõia'), ('c3d4e5f6-a7b8-49c0-1d2e-3f4a5b6c7d8e', 'aryîa');

-- Ex 5 (Tupi -> PT): irmão
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('e5f6a7b8-c9d0-41e2-f3a4-5b6c7d8e9f0a', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "kybyra" em português?', 'MULTIPLA_ESCOLHA', 'irmão (de m.)', 10, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('e5f6a7b8-c9d0-41e2-f3a4-5b6c7d8e9f0a', 'irmão (de m.)'), ('e5f6a7b8-c9d0-41e2-f3a4-5b6c7d8e9f0a', 'irmã'), ('e5f6a7b8-c9d0-41e2-f3a4-5b6c7d8e9f0a', 'avô'), ('e5f6a7b8-c9d0-41e2-f3a4-5b6c7d8e9f0a', 'avó');

-- Ex 6 (Tupi -> PT): irmã
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('1a2b3c4d-5e6f-47a8-9b0c-d1e2f3a4b5c6', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "rendyra" em português?', 'MULTIPLA_ESCOLHA', 'irmã', 10, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('1a2b3c4d-5e6f-47a8-9b0c-d1e2f3a4b5c6', 'irmão (de m.)'), ('1a2b3c4d-5e6f-47a8-9b0c-d1e2f3a4b5c6', 'irmã'), ('1a2b3c4d-5e6f-47a8-9b0c-d1e2f3a4b5c6', 'avô'), ('1a2b3c4d-5e6f-47a8-9b0c-d1e2f3a4b5c6', 'avó');

-- Ex 7 (Tupi -> PT): avô
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('2b3c4d5e-6f7a-48b9-c0d1-e2f3a4b5c6d7', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "amõia" em português?', 'MULTIPLA_ESCOLHA', 'avô', 10, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('2b3c4d5e-6f7a-48b9-c0d1-e2f3a4b5c6d7', 'irmão (de m.)'), ('2b3c4d5e-6f7a-48b9-c0d1-e2f3a4b5c6d7', 'irmã'), ('2b3c4d5e-6f7a-48b9-c0d1-e2f3a4b5c6d7', 'avô'), ('2b3c4d5e-6f7a-48b9-c0d1-e2f3a4b5c6d7', 'avó');

-- Ex 8 (Tupi -> PT): avó
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('3c4d5e6f-7a8b-49c0-d1e2-f3a4b5c6d7e8', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "aryîa" em português?', 'MULTIPLA_ESCOLHA', 'avó', 10, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('3c4d5e6f-7a8b-49c0-d1e2-f3a4b5c6d7e8', 'irmão (de m.)'), ('3c4d5e6f-7a8b-49c0-d1e2-f3a4b5c6d7e8', 'irmã'), ('3c4d5e6f-7a8b-49c0-d1e2-f3a4b5c6d7e8', 'avô'), ('3c4d5e6f-7a8b-49c0-d1e2-f3a4b5c6d7e8', 'avó');

-- 4. Misturar (embaralhar) TODOS os exercícios dos módulos >= 7
-- A reclamação do usuário foi "palavra sy em portugues dps palavra mãe em tupi". 
-- O problema é que enunciados semelhantes (mesma resposta ou conceito) caíam juntos.
-- Com a função row_number particionando pela resposta correta, intercalamos perguntas sobre o mesmo conceito.

WITH ranked AS (
    SELECT id, 
           licao_id,
           row_number() OVER (PARTITION BY licao_id, resposta_correta ORDER BY random()) as seq_resposta,
           random() as rnd
    FROM exercicios
    WHERE licao_id IN (
        SELECT l.id FROM licoes l
        JOIN modulos m ON l.modulo_id = m.id
        WHERE m.ordem_index >= 7
    )
),
shuffled AS (
    SELECT id, row_number() OVER (PARTITION BY licao_id ORDER BY seq_resposta, rnd) as new_ordem
    FROM ranked
)
UPDATE exercicios e
SET ordem_index = s.new_ordem
FROM shuffled s
WHERE e.id = s.id;
