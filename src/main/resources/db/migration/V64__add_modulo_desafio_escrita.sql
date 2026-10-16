-- V64: Adiciona Módulo 5 (Desafio de Escrita e Digitação) na Trilha Básica
-- Este módulo introduz exercícios do tipo 'DIGITACAO' para testar a escrita livre e o avaliador pedagógico IA

-- 1. Criação do Módulo 5 na Trilha Potiguara Básica ('22222222-2222-2222-2222-222222222222')
INSERT INTO modulos (id, titulo, descricao, ordem_index, trilha_id, criado_em, atualizado_em) 
VALUES (
    '55555555-5555-5555-5555-555555550005', 
    'Módulo 5: Desafio de Escrita', 
    'Pratique a digitação e a escrita correta das palavras em Tupi com auxílio do tutor inteligente.', 
    5, 
    '22222222-2222-2222-2222-222222222222', 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 2. Lição 1: Alimentos e Animais (Prática de Grafia)
INSERT INTO licoes (id, titulo, descricao, ordem_index, modulo_id, criado_em, atualizado_em)
VALUES (
    '66666666-6666-6666-6666-666666660001',
    'Lição 1: Escrevendo Animais e Alimentos',
    'Escreva no teclado a grafia correta em Tupi para animais e alimentos conhecidos.',
    1,
    '55555555-5555-5555-5555-555555550005',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Vincular vocabulário à Lição 1 (para tela de Dicas/Revisão)
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES 
('66666666-6666-6666-6666-666666660001', 'ee21b80e-5924-42f8-ade1-1d78f70f9f5d'), -- mandi'oka
('66666666-6666-6666-6666-666666660001', '539430f4-c817-472f-8e4a-5092ef531599'), -- pirá
('66666666-6666-6666-6666-666666660001', 'ca8822c8-6e52-44a7-809b-50fdd5852e09'), -- akaîu
('66666666-6666-6666-6666-666666660001', 'b92b994a-6283-4315-8f65-22c0d8d8eda9'), -- tapi'ira
('66666666-6666-6666-6666-666666660001', '74930d8c-4010-4f15-b895-1b38f036d50a')  -- pakoba
ON CONFLICT DO NOTHING;

-- Exercícios de Digitação da Lição 1 (tipo = 'DIGITACAO', sem opcoes em exercicio_opcoes)
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) VALUES
('77777777-7777-7777-7777-777777770001', '66666666-6666-6666-6666-666666660001', 'Escreva em Tupi a palavra correspondente a "mandioca":', 'DIGITACAO', 'mandi''oka', 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770002', '66666666-6666-6666-6666-666666660001', 'Escreva em Tupi a palavra correspondente a "peixe":', 'DIGITACAO', 'pirá', 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770003', '66666666-6666-6666-6666-666666660001', 'Escreva em Tupi a palavra correspondente a "caju":', 'DIGITACAO', 'akaîu', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770004', '66666666-6666-6666-6666-666666660001', 'Escreva em Tupi a palavra correspondente a "anta":', 'DIGITACAO', 'tapi''ira', 15, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770005', '66666666-6666-6666-6666-666666660001', 'Escreva em Tupi a palavra correspondente a "banana":', 'DIGITACAO', 'pakoba', 15, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- 3. Lição 2: Natureza e Qualidades
INSERT INTO licoes (id, titulo, descricao, ordem_index, modulo_id, criado_em, atualizado_em)
VALUES (
    '66666666-6666-6666-6666-666666660002',
    'Lição 2: Escrevendo Elementos da Natureza',
    'Escreva palavras ligadas à natureza e ao cotidiano.',
    2,
    '55555555-5555-5555-5555-555555550005',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Vincular vocabulário à Lição 2
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES 
('66666666-6666-6666-6666-666666660002', '481cb625-cc13-480a-80fa-410347b57809'), -- amana
('66666666-6666-6666-6666-666666660002', '8c9e76c7-2a4d-41b4-aab6-c58f4cef91ea'), -- îasy
('66666666-6666-6666-6666-666666660002', '5ebce908-69b7-4813-8f22-a8facb15de35'), -- guaraci
('66666666-6666-6666-6666-666666660002', '1940a69f-c604-4080-9418-11b0c88a14b8'), -- porang
('66666666-6666-6666-6666-666666660002', '0c5db57f-219c-4a47-9f21-68ef8d6cc78d')  -- akanga
ON CONFLICT DO NOTHING;

-- Exercícios de Digitação da Lição 2
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) VALUES
('77777777-7777-7777-7777-777777770006', '66666666-6666-6666-6666-666666660002', 'Escreva em Tupi a palavra correspondente a "chuva":', 'DIGITACAO', 'amana', 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770007', '66666666-6666-6666-6666-666666660002', 'Escreva em Tupi a palavra correspondente a "lua":', 'DIGITACAO', 'îasy', 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770008', '66666666-6666-6666-6666-666666660002', 'Escreva em Tupi a palavra correspondente a "sol":', 'DIGITACAO', 'guaraci', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770009', '66666666-6666-6666-6666-666666660002', 'Escreva em Tupi a palavra correspondente a "bonito":', 'DIGITACAO', 'porang', 15, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770010', '66666666-6666-6666-6666-666666660002', 'Escreva em Tupi a palavra correspondente a "cabeça":', 'DIGITACAO', 'akanga', 15, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;
