-- V65: Criação da nova trilha 'Desafio de Escrita' e movimentação do módulo de escrita para ela

-- 1. Cria a nova Trilha de Escrita
INSERT INTO trilhas (id, nome, descricao, status, icon, cor_base, slug, image_url, criado_em, atualizado_em)
VALUES (
    '22222222-2222-2222-2222-222222222225',
    'Desafio de Escrita',
    'Pratique a digitação e a escrita correta das palavras em Tupi com auxílio do tutor inteligente.',
    'PUBLISHED',
    '✍️',
    'emerald',
    'desafio-de-escrita',
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 2. Move o Módulo 5 da Trilha Básica para ser o Módulo 1 da Trilha de Escrita
UPDATE modulos
SET trilha_id = '22222222-2222-2222-2222-222222222225',
    ordem_index = 1,
    titulo = 'Módulo 1: Desafio de Escrita',
    atualizado_em = CURRENT_TIMESTAMP
WHERE id = '55555555-5555-5555-5555-555555550005';
