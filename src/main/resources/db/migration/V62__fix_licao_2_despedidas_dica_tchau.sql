-- ==========================================
-- V60: Corrigir Lição 2 (Despedidas) do Módulo 1 - Trilha Potiguara Básica
--  - Recriar a dica "Ne’ĩ, asó ikó" = "Tchau" (havia sumido da tela de dicas)
--  - Remover o "(Estou indo)" do enunciado do exercício de "Tchau"
-- ==========================================

-- 1. Garantir que "Ne’ĩ, asó ikó" (Tchau) exista no dicionário
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
SELECT '55555555-5555-5555-5555-555555555554', 'Ne’ĩ, asó ikó', 'Tchau', 'Ne’ĩ, asó ikó', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM conteudos_linguisticos WHERE palavra_tupi = 'Ne’ĩ, asó ikó')
ON CONFLICT (id) DO UPDATE
SET palavra_tupi = EXCLUDED.palavra_tupi,
    traducao_ptbr = EXCLUDED.traducao_ptbr,
    fonetica = EXCLUDED.fonetica,
    atualizado_em = CURRENT_TIMESTAMP;

UPDATE conteudos_linguisticos
SET traducao_ptbr = 'Tchau', atualizado_em = CURRENT_TIMESTAMP
WHERE palavra_tupi = 'Ne’ĩ, asó ikó';

-- 2. Vincular a dica à Lição 2 (Despedidas)
INSERT INTO licao_conteudo (licao_id, conteudo_id)
SELECT '44444444-4444-4444-4444-444444444442', id
FROM conteudos_linguisticos
WHERE palavra_tupi = 'Ne’ĩ, asó ikó'
LIMIT 1
ON CONFLICT DO NOTHING;

-- 3. Remover "(Estou indo)" do enunciado
UPDATE exercicios
SET enunciado = 'Como se diz "Tchau" em Potiguara?',
    atualizado_em = CURRENT_TIMESTAMP
WHERE id = '66666666-6666-6666-6666-666666666664';
