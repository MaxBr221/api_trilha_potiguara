-- ==========================================
-- V47: Corrigir Lição 2 do Módulo 7 e misturar exercícios
-- ==========================================

-- 1. Limpar os exercícios atuais da Lição 2 do Módulo 7 ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2')
DELETE FROM exercicio_opcoes WHERE exercicio_id IN (
    SELECT id FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2'
);
DELETE FROM exercicio_history WHERE exercicio_id IN (
    SELECT id FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2'
);
DELETE FROM exercicios WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2';
DELETE FROM licao_conteudo WHERE licao_id = 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2';

-- 2. Adicionar os conteúdos no banco
-- kybyra já existe no Módulo 7, mas vamos garantir e reassociar
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', '6e92d3c4-6d3c-4998-87ed-93c89150c9fe');

-- Inserir rendyra (irmã)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES ('c34a2e5f-14ab-45cd-a912-32a5bcd478fe', 'rendyra', 'irmã', 'rendyra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'c34a2e5f-14ab-45cd-a912-32a5bcd478fe');

-- Inserir amõia (avô)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES ('b47c9a2d-35bc-48de-b678-43d6cde589ac', 'amõia', 'avô', 'amõia', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'b47c9a2d-35bc-48de-b678-43d6cde589ac');

-- Inserir aryîa (avó)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES ('e81f5b4a-26cd-49ef-c789-54e7def690bd', 'aryîa', 'avó', 'aryîa', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES ('bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'e81f5b4a-26cd-49ef-c789-54e7def690bd');

-- 3. Inserir exercícios
-- Ex 1 (PT -> Tupi): irmão
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('11111111-1111-1111-1111-111111111111', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "irmão (de m.)" em Tupi?', 'MULTIPLA_ESCOLHA', 'kybyra', 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('11111111-1111-1111-1111-111111111111', 'kybyra'), ('11111111-1111-1111-1111-111111111111', 'rendyra'), ('11111111-1111-1111-1111-111111111111', 'amõia'), ('11111111-1111-1111-1111-111111111111', 'aryîa');

-- Ex 2 (PT -> Tupi): irmã
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('22222222-2222-2222-2222-222222222222', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "irmã" em Tupi?', 'MULTIPLA_ESCOLHA', 'rendyra', 10, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('22222222-2222-2222-2222-222222222222', 'kybyra'), ('22222222-2222-2222-2222-222222222222', 'rendyra'), ('22222222-2222-2222-2222-222222222222', 'amõia'), ('22222222-2222-2222-2222-222222222222', 'aryîa');

-- Ex 3 (PT -> Tupi): avô
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('33333333-3333-3333-3333-333333333333', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "avô" em Tupi?', 'MULTIPLA_ESCOLHA', 'amõia', 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('33333333-3333-3333-3333-333333333333', 'kybyra'), ('33333333-3333-3333-3333-333333333333', 'rendyra'), ('33333333-3333-3333-3333-333333333333', 'amõia'), ('33333333-3333-3333-3333-333333333333', 'aryîa');

-- Ex 4 (PT -> Tupi): avó
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('44444444-4444-4444-4444-444444444444', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'Como se diz "avó" em Tupi?', 'MULTIPLA_ESCOLHA', 'aryîa', 10, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('44444444-4444-4444-4444-444444444444', 'kybyra'), ('44444444-4444-4444-4444-444444444444', 'rendyra'), ('44444444-4444-4444-4444-444444444444', 'amõia'), ('44444444-4444-4444-4444-444444444444', 'aryîa');

-- Ex 5 (Tupi -> PT): irmão
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('55555555-5555-5555-5555-555555555555', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "kybyra" em português?', 'MULTIPLA_ESCOLHA', 'irmão (de m.)', 10, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('55555555-5555-5555-5555-555555555555', 'irmão (de m.)'), ('55555555-5555-5555-5555-555555555555', 'irmã'), ('55555555-5555-5555-5555-555555555555', 'avô'), ('55555555-5555-5555-5555-555555555555', 'avó');

-- Ex 6 (Tupi -> PT): irmã
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('66666666-6666-6666-6666-666666666666', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "rendyra" em português?', 'MULTIPLA_ESCOLHA', 'irmã', 10, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('66666666-6666-6666-6666-666666666666', 'irmão (de m.)'), ('66666666-6666-6666-6666-666666666666', 'irmã'), ('66666666-6666-6666-6666-666666666666', 'avô'), ('66666666-6666-6666-6666-666666666666', 'avó');

-- Ex 7 (Tupi -> PT): avô
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('77777777-7777-7777-7777-777777777777', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "amõia" em português?', 'MULTIPLA_ESCOLHA', 'avô', 10, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('77777777-7777-7777-7777-777777777777', 'irmão (de m.)'), ('77777777-7777-7777-7777-777777777777', 'irmã'), ('77777777-7777-7777-7777-777777777777', 'avô'), ('77777777-7777-7777-7777-777777777777', 'avó');

-- Ex 8 (Tupi -> PT): avó
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) 
VALUES ('88888888-8888-8888-8888-888888888888', 'bbbbc0e9-b190-4d86-94c5-d39282cd1fb2', 'O que significa a palavra "aryîa" em português?', 'MULTIPLA_ESCOLHA', 'avó', 10, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES ('88888888-8888-8888-8888-888888888888', 'irmão (de m.)'), ('88888888-8888-8888-8888-888888888888', 'irmã'), ('88888888-8888-8888-8888-888888888888', 'avô'), ('88888888-8888-8888-8888-888888888888', 'avó');

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
