-- ==========================================
-- V51: Correção do Dicionário da Lição 1
-- ==========================================

-- 1. Desvincular "Onça" (55555555-5555-5555-5555-555555555553) da Lição 1 (44444444-4444-4444-4444-444444444441)
-- Esse bug ocorreu no V23 devido a um copy-paste de UUID que conflitou com Onça inserida no V7.
DELETE FROM licao_conteudo 
WHERE licao_id = '44444444-4444-4444-4444-444444444441' 
AND conteudo_id = '55555555-5555-5555-5555-555555555553';

-- 2. Atualizar "Bom dia" para o valor correto
UPDATE conteudos_linguisticos 
SET palavra_tupi = 'Ko''ema', fonetica = 'Ko''ema', atualizado_em = CURRENT_TIMESTAMP
WHERE id = '55555555-5555-5555-5555-555555555551';

-- 3. Atualizar "Boa tarde" para o valor correto
UPDATE conteudos_linguisticos 
SET palavra_tupi = 'Kuarasy / Ka''aru', fonetica = 'Kuarasy / Ka''aru', atualizado_em = CURRENT_TIMESTAMP
WHERE id = '55555555-5555-5555-5555-555555555552';

-- 4. Inserir "Boa noite" corretamente (com um novo UUID livre de conflitos)
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES ('55555555-5555-5555-5555-555555555556', 'Pituna porang / Pituna', 'Boa noite', 'Pituna porang / Pituna', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO UPDATE SET palavra_tupi = EXCLUDED.palavra_tupi, fonetica = EXCLUDED.fonetica;

-- 5. Vincular a nova "Boa noite" à Lição 1
INSERT INTO licao_conteudo (licao_id, conteudo_id) 
VALUES ('44444444-4444-4444-4444-444444444441', '55555555-5555-5555-5555-555555555556')
ON CONFLICT DO NOTHING;
