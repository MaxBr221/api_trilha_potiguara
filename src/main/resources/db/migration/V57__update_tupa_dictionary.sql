-- ==========================================
-- Atualização do Dicionário: tupã -> Deus e amãsununga -> trovão
-- ==========================================

UPDATE conteudos_linguisticos 
SET traducao_ptbr = 'Deus', 
    atualizado_em = CURRENT_TIMESTAMP 
WHERE lower(palavra_tupi) = 'tupã';

-- Adicionar amãsununga caso não exista
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) 
VALUES (gen_random_uuid(), 'amãsununga', 'trovão', 'amãsununga', 'PALAVRA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (lower(palavra_tupi)) DO UPDATE 
SET traducao_ptbr = 'trovão', atualizado_em = CURRENT_TIMESTAMP;

-- Atualizar qualquer exercício que tenha a resposta incorreta antiga, se houver
UPDATE exercicios 
SET resposta_correta = 'Deus', 
    atualizado_em = CURRENT_TIMESTAMP 
WHERE resposta_correta = 'trovão amãsununga' OR resposta_correta = 'trovão, divindade, Deus';

-- Se tivermos "O que significa a palavra "tupã" em português?"
UPDATE exercicios
SET enunciado = 'O que significa a palavra "tupã" em português?'
WHERE enunciado = 'O que significa a palavra "tupã" em português?';
