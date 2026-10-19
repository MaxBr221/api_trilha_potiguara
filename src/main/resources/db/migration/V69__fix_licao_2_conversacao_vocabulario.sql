-- V69: Corrigir vocabulário da Lição 2 (Despedidas e Respostas) da Trilha de Conversação e Pronúncia

-- 1. Remove os vínculos incorretos anteriores da Lição 2
DELETE FROM licao_conteudo 
WHERE licao_id = '66666666-6666-6666-6666-666666660004';

-- 2. Insere/atualiza os termos exatos ensinados na Lição 2
INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em)
VALUES 
('55555555-5555-5555-5555-555555555561', 'Tereo', 'Adeus / Vá com Deus', 'te-re-o', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('55555555-5555-5555-5555-555555555562', 'Ereîubépe?', 'Você vem? / Tudo bem?', 'e-re-îu-bé-pe', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('55555555-5555-5555-5555-555555555563', 'Aîur', 'Eu venho / Estou bem', 'a-îur', 'EXPRESSAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO UPDATE SET 
    palavra_tupi = EXCLUDED.palavra_tupi,
    traducao_ptbr = EXCLUDED.traducao_ptbr,
    fonetica = EXCLUDED.fonetica;

-- 3. Vincula os conteúdos corretos à Lição 2
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555561'), -- Tereo
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555562'), -- Ereîubépe?
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555563')  -- Aîur
ON CONFLICT DO NOTHING;
