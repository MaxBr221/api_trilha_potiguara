-- V68: Adição da Trilha de Prática Oral e Conversação (Múltipla Escolha + Digitação + Pronúncia de Áudio)
-- Esta trilha reúne os 3 formatos de exercícios em lições dinâmicas com avaliação inteligente

-- 1. Cria a nova Trilha
INSERT INTO trilhas (id, nome, descricao, status, icon, cor_base, slug, image_url, criado_em, atualizado_em)
VALUES (
    '22222222-2222-2222-2222-222222222226',
    'Prática Oral e Conversação',
    'Aprenda ouvindo, falando e escrevendo em Tupi com auxílio do tutor inteligente.',
    'PUBLISHED',
    '🎙️',
    'amber',
    'pratica-oral-e-conversacao',
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 2. Módulo 1: Saudações e Encontros
INSERT INTO modulos (id, titulo, descricao, ordem_index, trilha_id, criado_em, atualizado_em)
VALUES (
    '55555555-5555-5555-5555-555555550006',
    'Módulo 1: Saudações e Encontros',
    'Pratique saudações com exercícios de múltipla escolha, digitação e pronúncia no microfone.',
    1,
    '22222222-2222-2222-2222-222222222226',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 2.1 Lição 1: Cumprimentos do Dia (Módulo 1)
INSERT INTO licoes (id, titulo, descricao, ordem_index, modulo_id, criado_em, atualizado_em)
VALUES (
    '66666666-6666-6666-6666-666666660003',
    'Lição 1: Cumprimentos do Dia',
    'Reconheça, fale e digite as saudações básicas em Tupi.',
    1,
    '55555555-5555-5555-5555-555555550006',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Vincular vocabulário à Lição 1
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES
('66666666-6666-6666-6666-666666660003', '55555555-5555-5555-5555-555555555551'), -- Tîa nde Ko'ema
('66666666-6666-6666-6666-666666660003', '55555555-5555-5555-5555-555555555552')  -- Tîa nde Karuka
ON CONFLICT DO NOTHING;

-- Exercícios da Lição 1 (Híbridos: Múltipla Escolha + Pronúncia + Digitação)
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) VALUES
('77777777-7777-7777-7777-777777770011', '66666666-6666-6666-6666-666666660003', 'Como se diz "Bom dia" em Tupi?', 'MULTIPLA_ESCOLHA', 'Tîa nde Ko''ema', 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770012', '66666666-6666-6666-6666-666666660003', 'Fale no microfone a saudação de bom dia: "Tîa nde Ko''ema"', 'PRONUNCIA', 'Tîa nde Ko''ema', 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770013', '66666666-6666-6666-6666-666666660003', 'Escreva em Tupi a expressão para "Boa tarde":', 'DIGITACAO', 'Tîa nde Karuka', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770014', '66666666-6666-6666-6666-666666660003', 'O que significa a saudação "Tîa nde Karuka"?', 'MULTIPLA_ESCOLHA', 'Boa tarde', 10, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770015', '66666666-6666-6666-6666-666666660003', 'Fale no microfone a saudação de boa tarde: "Tîa nde Karuka"', 'PRONUNCIA', 'Tîa nde Karuka', 15, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Opções dos exercícios de Múltipla Escolha da Lição 1
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES
('77777777-7777-7777-7777-777777770011', 'Tîa nde Ko''ema'),
('77777777-7777-7777-7777-777777770011', 'Tereo'),
('77777777-7777-7777-7777-777777770011', 'Aîur'),
('77777777-7777-7777-7777-777777770011', 'Taba'),
('77777777-7777-7777-7777-777777770014', 'Boa tarde'),
('77777777-7777-7777-7777-777777770014', 'Bom dia'),
('77777777-7777-7777-7777-777777770014', 'Boa noite'),
('77777777-7777-7777-7777-777777770014', 'Até amanhã');

-- 2.2 Lição 2: Despedidas e Respostas (Módulo 1)
INSERT INTO licoes (id, titulo, descricao, ordem_index, modulo_id, criado_em, atualizado_em)
VALUES (
    '66666666-6666-6666-6666-666666660004',
    'Lição 2: Despedidas e Respostas',
    'Fale no microfone e fixe as respostas de cordialidade.',
    2,
    '55555555-5555-5555-5555-555555550006',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Vincular vocabulário à Lição 2
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555553'), -- Tereo
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555554'), -- Ereîubépe?
('66666666-6666-6666-6666-666666660004', '55555555-5555-5555-5555-555555555555')  -- Aîur
ON CONFLICT DO NOTHING;

-- Exercícios da Lição 2
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) VALUES
('77777777-7777-7777-7777-777777770021', '66666666-6666-6666-6666-666666660004', 'Qual é a forma de dizer "Adeus / Vá com Deus"?', 'MULTIPLA_ESCOLHA', 'Tereo', 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770022', '66666666-6666-6666-6666-666666660004', 'Pronuncie no microfone a despedida: "Tereo"', 'PRONUNCIA', 'Tereo', 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770023', '66666666-6666-6666-6666-666666660004', 'Escreva em Tupi a palavra de despedida correspondente a "Adeus":', 'DIGITACAO', 'Tereo', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770024', '66666666-6666-6666-6666-666666660004', 'O que significa a pergunta "Ereîubépe?"?', 'MULTIPLA_ESCOLHA', 'Você vem? / Tudo bem?', 10, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770025', '66666666-6666-6666-6666-666666660004', 'Pronuncie no microfone a resposta "Aîur" (Eu venho / Estou bem):', 'PRONUNCIA', 'Aîur', 15, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Opções dos exercícios de Múltipla Escolha da Lição 2
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES
('77777777-7777-7777-7777-777777770021', 'Tereo'),
('77777777-7777-7777-7777-777777770021', 'Aîur'),
('77777777-7777-7777-7777-777777770021', 'Ereîubépe?'),
('77777777-7777-7777-7777-777777770021', 'Ko''ema'),
('77777777-7777-7777-7777-777777770024', 'Você vem? / Tudo bem?'),
('77777777-7777-7777-7777-777777770024', 'Para onde vai?'),
('77777777-7777-7777-7777-777777770024', 'Quem é você?'),
('77777777-7777-7777-7777-777777770024', 'Qual o seu nome?');

-- 3. Módulo 2: Natureza e Sons da Floresta
INSERT INTO modulos (id, titulo, descricao, ordem_index, trilha_id, criado_em, atualizado_em)
VALUES (
    '55555555-5555-5555-5555-555555550007',
    'Módulo 2: Natureza e Sons da Floresta',
    'Aprenda nomes de animais, árvores e elementos naturais combinando escuta, fala e escrita.',
    2,
    '22222222-2222-2222-2222-222222222226',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 3.1 Lição 1: Animais e Elementos (Módulo 2)
INSERT INTO licoes (id, titulo, descricao, ordem_index, modulo_id, criado_em, atualizado_em)
VALUES (
    '66666666-6666-6666-6666-666666660005',
    'Lição 1: Animais e Elementos',
    'Pratique a pronúncia do peixe, da chuva e da aldeia.',
    1,
    '55555555-5555-5555-5555-555555550007',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Vincular vocabulário à Lição 1 do Módulo 2
INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES
('66666666-6666-6666-6666-666666660005', '539430f4-c817-472f-8e4a-5092ef531599'), -- pirá
('66666666-6666-6666-6666-666666660005', '481cb625-cc13-480a-80fa-410347b57809'), -- amana
('66666666-6666-6666-6666-666666660005', 'cd875650-bb35-493f-9919-683ed79b4adf'), -- ybyrá
('66666666-6666-6666-6666-666666660005', '16c5f5a2-30ff-44f9-bab0-d435fe95932d')  -- taba
ON CONFLICT DO NOTHING;

-- Exercícios da Lição 1 do Módulo 2
INSERT INTO exercicios (id, licao_id, enunciado, tipo, resposta_correta, pontuacao_xp, ordem_index, criado_em, atualizado_em) VALUES
('77777777-7777-7777-7777-777777770031', '66666666-6666-6666-6666-666666660005', 'Qual é o significado de "Pirá" em Tupi?', 'MULTIPLA_ESCOLHA', 'Peixe', 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770032', '66666666-6666-6666-6666-666666660005', 'Fale no microfone a palavra para peixe: "Pirá"', 'PRONUNCIA', 'Pirá', 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770033', '66666666-6666-6666-6666-666666660005', 'Escreva em Tupi a palavra para "Chuva":', 'DIGITACAO', 'amana', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770034', '66666666-6666-6666-6666-666666660005', 'Pronuncie com a vogal "y" a palavra para árvore: "Ybyrá"', 'PRONUNCIA', 'ybyrá', 15, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('77777777-7777-7777-7777-777777770035', '66666666-6666-6666-6666-666666660005', 'O que significa a palavra "Taba"?', 'MULTIPLA_ESCOLHA', 'Aldeia', 10, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Opções dos exercícios de Múltipla Escolha da Lição 1 do Módulo 2
INSERT INTO exercicio_opcoes (exercicio_id, opcao) VALUES
('77777777-7777-7777-7777-777777770031', 'Peixe'),
('77777777-7777-7777-7777-777777770031', 'Pássaro'),
('77777777-7777-7777-7777-777777770031', 'Árvore'),
('77777777-7777-7777-7777-777777770031', 'Cobra'),
('77777777-7777-7777-7777-777777770035', 'Aldeia'),
('77777777-7777-7777-7777-777777770035', 'Casa'),
('77777777-7777-7777-7777-777777770035', 'Rio'),
('77777777-7777-7777-7777-777777770035', 'Floresta');
