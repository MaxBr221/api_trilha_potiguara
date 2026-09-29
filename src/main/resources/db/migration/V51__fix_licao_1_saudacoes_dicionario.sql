-- ==========================================
-- V51: Remover Onça e Adicionar Boa Noite
-- ==========================================

-- 1. Desvincular "Onça" (55555555-5555-5555-5555-555555555553) da Lição 1 (44444444-4444-4444-4444-444444444441)
DELETE FROM licao_conteudo 
WHERE licao_id = '44444444-4444-4444-4444-444444444441' 
AND conteudo_id = '55555555-5555-5555-5555-555555555553';

-- 2. Inserir a palavra "Tîa nde Pytuna" no dicionário (com UUID novo para não conflitar com a Onça)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES ('55555555-5555-5555-5555-555555555556', 'Tîa nde Pytuna', 'Boa noite', 'Tîa nde Pytuna', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 3. Vincular "Tîa nde Pytuna" à Lição 1
INSERT INTO licao_conteudo (licao_id, conteudo_id) 
VALUES ('44444444-4444-4444-4444-444444444441', '55555555-5555-5555-5555-555555555556')
ON CONFLICT DO NOTHING;
