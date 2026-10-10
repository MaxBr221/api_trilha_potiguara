-- ==========================================
-- V61: Adição de dicas faltantes e remoção de exercícios inválidos
-- ==========================================

-- 1. Inserir palavras faltantes no dicionário (conteudos_linguisticos) se não existirem
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) VALUES
('b0000000-0000-0000-0000-000000000001', 'Oîepé', 'Um', 'Oîepé', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000002', 'Mokõi', 'Dois', 'Mokõi', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000003', 'Mosapyr', 'Três', 'Mosapyr', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000004', 'Irundy', 'Quatro', 'Irundy', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000005', 'Ambo', 'Cinco', 'Ambo', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('b0000000-0000-0000-0000-000000000006', 'Pytã', 'Vermelho', 'Pytã', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000007', 'Tinga', 'Branco', 'Tinga', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000008', 'Un', 'Preto', 'Un', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000009', 'Yuba', 'Amarelo', 'Yuba', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000010', 'Oby', 'Verde', 'Oby', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('b0000000-0000-0000-0000-000000000011', 'Ta''yra', 'Filho', 'Ta''yra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000012', 'Taîyra', 'Filha', 'Taîyra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('b0000000-0000-0000-0000-000000000013', 'Ybyrá', 'Árvore', 'Ybyrá', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000014', 'Mandi''oka', 'Mandioca', 'Mandi''oka', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000015', 'Abati', 'Milho', 'Abati', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000016', 'Pakoba', 'Banana', 'Pakoba', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- 2. Vincular as palavras às Lições

-- Lição: M2 Números L1 (44444444-4444-4444-4444-444444444443)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444443', id FROM conteudos_linguisticos 
WHERE palavra_tupi IN ('Oîepé', 'Mokõi', 'Mosapyr', 'Irundy', 'Ambo')
ON CONFLICT DO NOTHING;

-- Lição: M3 Cores L1 (b3e8ab21-8eb1-4d14-8f2d-8e470125e130)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT 'b3e8ab21-8eb1-4d14-8f2d-8e470125e130', id FROM conteudos_linguisticos 
WHERE palavra_tupi IN ('Pytã', 'Tinga', 'Un', 'Yuba', 'Oby')
ON CONFLICT DO NOTHING;

-- Lição: M4 Família L1 (d7b4e9f9-e3b9-4a0b-93cd-5d63f0d5a3ef)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT 'd7b4e9f9-e3b9-4a0b-93cd-5d63f0d5a3ef', id FROM conteudos_linguisticos 
WHERE palavra_tupi IN ('Ta''yra', 'Taîyra')
ON CONFLICT DO NOTHING;

-- Lição: M1 Natureza L2 (44444444-4444-4444-4444-444444444445)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444445', id FROM conteudos_linguisticos 
WHERE palavra_tupi IN ('Ybyrá', 'Mandi''oka', 'Abati', 'Pakoba')
ON CONFLICT DO NOTHING;

-- 3. Remover exercícios com palavras inválidas ("kunumĩ", "kunha'taĩ", "cunhã", "kunumĩ mirĩ")
-- (As opções atreladas a estes exercícios serão removidas via CASCADE)
DELETE FROM exercicios WHERE id IN (
  '4f0bca31-eaf7-4e35-9d8d-d4a0ffc13898',
  '903c4cff-db32-4a9d-9018-e75666c336ab',
  '4bbfca04-e165-48ff-85c4-6de1e171f93a',
  '420be7ff-2137-418f-a13f-74a76ebeca6d',
  '1029e760-b391-4fc9-b3fc-3a788419a893',
  '308bb7b8-e039-406d-8fb3-df700f4b4c28',
  'e9ea5b01-2abb-4a6a-9a9a-6ab12d99e90d',
  'c0d14319-4aef-41a9-8d38-10489da51d95',
  '5705a492-7d7d-4dfd-9fa9-cdd45dd631bf',
  '43e17dd1-0fb4-4f5a-9b4a-8ff053667022'
);
