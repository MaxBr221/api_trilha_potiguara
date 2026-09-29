-- ==========================================
-- V52: Ajustar definitivamente Boa Noite na Lição 1
-- ==========================================

-- 1. Remove qualquer palavra "Boa noite" errada da Lição 1 (ex: Pituna porang / Pituna)
DELETE FROM licao_conteudo 
WHERE licao_id = '44444444-4444-4444-4444-444444444441'
AND conteudo_id IN (
    SELECT id FROM conteudos_linguisticos 
    WHERE palavra_tupi = 'Pituna porang / Pituna' OR lower(traducao_ptbr) LIKE '%boa noite%'
);

-- 2. Garantir que 'Tîa nde Pytuna' existe no banco
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES (gen_random_uuid(), 'Tîa nde Pytuna', 'Boa noite', 'Tîa nde Pytuna', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 3. Vincular exclusivamente 'Tîa nde Pytuna' à Lição 1
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444441', id
FROM conteudos_linguisticos
WHERE palavra_tupi = 'Tîa nde Pytuna'
LIMIT 1
ON CONFLICT DO NOTHING;
