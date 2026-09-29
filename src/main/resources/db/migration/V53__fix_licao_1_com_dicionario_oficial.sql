-- ==========================================
-- V53: Sincronizar Lição 1 com Dicionário Oficial
-- ==========================================

-- 1. Limpar TODAS as palavras vinculadas à Lição 1 atualmente
-- (Isso limpa a Onça, e também as palavras erradas que o QA sugeriu como ko'ema, Kuarasy, etc)
DELETE FROM licao_conteudo WHERE licao_id = '44444444-4444-4444-4444-444444444441';

-- 2. Vincular "Tîa nde koema" (Bom dia) - Oficial do V26
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444441', id
FROM conteudos_linguisticos
WHERE palavra_tupi = 'Tîa nde koema'
LIMIT 1
ON CONFLICT DO NOTHING;

-- 3. Vincular "Tîa nde Karuka" (Boa tarde) - Oficial do V26
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444441', id
FROM conteudos_linguisticos
WHERE palavra_tupi = 'Tîa nde Karuka'
LIMIT 1
ON CONFLICT DO NOTHING;

-- 4. Vincular "Tîa nde Pytuna" (Boa noite) - Oficial do V26
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444441', id
FROM conteudos_linguisticos
WHERE palavra_tupi = 'Tîa nde Pytuna'
LIMIT 1
ON CONFLICT DO NOTHING;
