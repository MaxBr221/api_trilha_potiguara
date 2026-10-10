-- ==========================================
-- V61: Adição de dicas faltantes e remoção de exercícios inválidos
-- ==========================================

-- 1. Inserir palavras faltantes no dicionário (conteudos_linguisticos) se não existirem
-- Utilizamos WHERE NOT EXISTS para evitar violação do índice unique idx_conteudos_linguisticos_palavra_tupi_lower
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
SELECT * FROM (VALUES
  ('b0000000-0000-0000-0000-000000000001'::uuid, 'Oîepé', 'Um', 'Oîepé', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000002'::uuid, 'Mokõi', 'Dois', 'Mokõi', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000003'::uuid, 'Mosapyr', 'Três', 'Mosapyr', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000004'::uuid, 'Irundy', 'Quatro', 'Irundy', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000005'::uuid, 'Ambo', 'Cinco', 'Ambo', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  ('b0000000-0000-0000-0000-000000000006'::uuid, 'Pytã', 'Vermelho', 'Pytã', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000007'::uuid, 'Tinga', 'Branco', 'Tinga', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000008'::uuid, 'Un', 'Preto', 'Un', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000009'::uuid, 'Yuba', 'Amarelo', 'Yuba', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000010'::uuid, 'Oby', 'Verde', 'Oby', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  ('b0000000-0000-0000-0000-000000000011'::uuid, 'Ta''yra', 'Filho', 'Ta''yra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000012'::uuid, 'Taîyra', 'Filha', 'Taîyra', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  ('b0000000-0000-0000-0000-000000000013'::uuid, 'Ybyrá', 'Árvore', 'Ybyrá', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000014'::uuid, 'Mandi''oka', 'Mandioca', 'Mandi''oka', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000015'::uuid, 'Abati', 'Milho', 'Abati', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('b0000000-0000-0000-0000-000000000016'::uuid, 'Pakoba', 'Banana', 'Pakoba', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
) AS v(id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
WHERE NOT EXISTS (
  SELECT 1 FROM conteudos_linguisticos cl WHERE LOWER(cl.palavra_tupi) = LOWER(v.palavra_tupi)
);

-- 2. Vincular as palavras às Lições

-- Lição: M2 Números L1 (44444444-4444-4444-4444-444444444443)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444443', id FROM conteudos_linguisticos 
WHERE LOWER(palavra_tupi) IN (LOWER('Oîepé'), LOWER('Mokõi'), LOWER('Mosapyr'), LOWER('Irundy'), LOWER('Ambo'))
ON CONFLICT DO NOTHING;

-- Lição: M3 Cores L1 (b3e8ab21-8eb1-4d14-8f2d-8e470125e130)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT 'b3e8ab21-8eb1-4d14-8f2d-8e470125e130', id FROM conteudos_linguisticos 
WHERE LOWER(palavra_tupi) IN (LOWER('Pytã'), LOWER('Tinga'), LOWER('Un'), LOWER('Yuba'), LOWER('Oby'))
ON CONFLICT DO NOTHING;

-- Lição: M4 Família L1 (d7b4e9f9-e3b9-4a0b-93cd-5d63f0d5a3ef)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT 'd7b4e9f9-e3b9-4a0b-93cd-5d63f0d5a3ef', id FROM conteudos_linguisticos 
WHERE LOWER(palavra_tupi) IN (LOWER('Ta''yra'), LOWER('Taîyra'))
ON CONFLICT DO NOTHING;

-- Lição: M1 Natureza L2 (44444444-4444-4444-4444-444444444445)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444445', id FROM conteudos_linguisticos 
WHERE LOWER(palavra_tupi) IN (LOWER('Ybyrá'), LOWER('Mandi''oka'), LOWER('Abati'), LOWER('Pakoba'))
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
