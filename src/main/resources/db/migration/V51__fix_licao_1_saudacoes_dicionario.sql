-- ==========================================
-- V51: Correção do Dicionário da Lição 1
-- ==========================================

-- 1. Desvincular as palavras incorretas da Lição 1 (Tîa nde koema, Tîa nde Karuka, Onça)
DELETE FROM licao_conteudo 
WHERE licao_id = '44444444-4444-4444-4444-444444444441' 
AND conteudo_id IN (
    '55555555-5555-5555-5555-555555555551', -- Antigo Bom dia incorreto
    '55555555-5555-5555-5555-555555555552', -- Antigo Boa tarde incorreto
    '55555555-5555-5555-5555-555555555553'  -- Onça
);

-- 2. Garantir que as palavras corretas EXISTEM na tabela de dicionário.
-- Como pode haver restrições únicas, inserimos apenas se não existir.
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
SELECT gen_random_uuid(), 'Ko''ema', 'Bom dia', 'Ko''ema', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = lower('Ko''ema'));

INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
SELECT gen_random_uuid(), 'Kuarasy / Ka''aru', 'Boa tarde', 'Kuarasy / Ka''aru', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = lower('Kuarasy / Ka''aru'));

INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
SELECT gen_random_uuid(), 'Pituna porang / Pituna', 'Boa noite', 'Pituna porang / Pituna', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE lower(palavra_tupi) = lower('Pituna porang / Pituna'));

-- 3. Vincular essas três palavras corretas à Lição 1
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444441', id
FROM conteudos_linguisticos
WHERE lower(palavra_tupi) IN (lower('Ko''ema'), lower('Kuarasy / Ka''aru'), lower('Pituna porang / Pituna'))
ON CONFLICT DO NOTHING;
