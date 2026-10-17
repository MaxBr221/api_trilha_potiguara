-- 1. Corrige ícones e cores base das conquistas existentes
UPDATE conquistas
SET icone = 'PenaIcon', cor_base = 'emerald', meta_licoes = 1
WHERE titulo ILIKE '%Primeiro Passo%';

UPDATE conquistas
SET icone = 'ArcoFlechaIcon', cor_base = 'blue', meta_xp = 1000
WHERE titulo ILIKE '%Guerreiro Tupi%';

UPDATE conquistas
SET icone = 'FogueiraIcon', cor_base = 'amber', meta_ofensiva = 7
WHERE titulo ILIKE '%Semana Perfeita%';

UPDATE conquistas
SET icone = 'CocarIcon', cor_base = 'purple', meta_licoes = 50
WHERE titulo ILIKE '%Poliglota%';

UPDATE conquistas
SET icone = 'Flame', cor_base = 'orange', meta_ofensiva = 30
WHERE titulo ILIKE '%Mês de Fogo%' OR titulo ILIKE '%Mes de Fogo%';

UPDATE conquistas
SET icone = 'Shield', cor_base = 'indigo', meta_xp = 5000
WHERE titulo ILIKE '%Guardião Tupi%' OR titulo ILIKE '%Guardiao Tupi%';

UPDATE conquistas
SET icone = 'Crown', cor_base = 'amber', meta_licoes = 100
WHERE titulo ILIKE '%Mestre da Fala%';

-- 2. Insere novas conquistas reais (incluindo a meta de 2.000 XP)
INSERT INTO conquistas (id, titulo, descricao, icone, cor_base, meta_xp, meta_licoes, meta_ofensiva, criado_em)
VALUES
(
    gen_random_uuid(),
    'Despertar Tupi',
    'Acumule seus primeiros 500 XP na jornada de aprendizado.',
    'Zap',
    'emerald',
    500,
    0,
    0,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Sábio da Aldeia',
    'Acumule 2.000 XP completando lições e exercícios.',
    'Sparkles',
    'amber',
    2000,
    0,
    0,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Espírito Ancestral',
    'Alcance a marca lendária de 10.000 XP.',
    'Award',
    'rose',
    10000,
    0,
    0,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Centelha Viva',
    'Mantenha uma ofensiva de 3 dias seguidos.',
    'Flame',
    'amber',
    0,
    0,
    3,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Fogueira Firme',
    'Mantenha uma ofensiva de 14 dias seguidos.',
    'FogueiraIcon',
    'orange',
    0,
    0,
    14,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Explorador da Trilha',
    'Conclua 10 lições de aprendizado.',
    'Compass',
    'blue',
    0,
    10,
    0,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Caçador do Saber',
    'Conclua 25 lições com sucesso.',
    'Target',
    'teal',
    0,
    25,
    0,
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;
