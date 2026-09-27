-- V49: Replace nonsense phrases in Modulo 12 with meaningful ones

DO $$ 
DECLARE
    v_modulo_id UUID;
    v_licao1_id UUID;
    v_licao2_id UUID;
    v_frase1_id UUID := gen_random_uuid();
    v_frase2_id UUID := gen_random_uuid();
    v_frase3_id UUID := gen_random_uuid();
    v_frase4_id UUID := gen_random_uuid();
    v_frase5_id UUID := gen_random_uuid();
    v_frase6_id UUID := gen_random_uuid();
    v_frase7_id UUID := gen_random_uuid();
    v_frase8_id UUID := gen_random_uuid();
    v_frase9_id UUID := gen_random_uuid();
    v_frase10_id UUID := gen_random_uuid();
BEGIN
    SELECT id INTO v_modulo_id FROM modulos WHERE titulo LIKE '%12%Formando Frases%';
    
    IF v_modulo_id IS NOT NULL THEN
        -- Find Licao 1
        SELECT id INTO v_licao1_id FROM licoes WHERE modulo_id = v_modulo_id AND titulo LIKE '%Lição 1%';
        -- Find Licao 2
        SELECT id INTO v_licao2_id FROM licoes WHERE modulo_id = v_modulo_id AND titulo LIKE '%Lição 2%';

        -- Delete existing exercises for these lessons
        IF v_licao1_id IS NOT NULL THEN
            DELETE FROM exercicio_opcoes WHERE exercicio_id IN (SELECT id FROM exercicios WHERE licao_id = v_licao1_id);
            DELETE FROM exercicios WHERE licao_id = v_licao1_id;
        END IF;

        IF v_licao2_id IS NOT NULL THEN
            DELETE FROM exercicio_opcoes WHERE exercicio_id IN (SELECT id FROM exercicios WHERE licao_id = v_licao2_id);
            DELETE FROM exercicios WHERE licao_id = v_licao2_id;
        END IF;

        -- Remove the old nonsense phrases 
        DELETE FROM conteudos_linguisticos WHERE palavra_tupi IN (
            'mba''etegûama îasekó', 'uru pab', 'ta''yra mene''õ', 'ro''y nhe''ẽrueru; nhe''engaíba',
            'îoatypeteka parab/a; pinim/a', '''yapyra pirian/a', 'tatobapy abaíb', '''ykûara marãtekoare''yma',
            'potĩ îub', 'pitangĩ kyrirĩ'
        );

        -- Insert new meaningful phrases
        INSERT INTO conteudos_linguisticos (id, palavra_tupi, traducao_ptbr, fonetica, tipo, criado_em, atualizado_em) VALUES
        (v_frase1_id, 'aba katu', 'homem bom', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase2_id, 'kunhã poranga', 'mulher bonita', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase3_id, '''y katu', 'água limpa', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase4_id, 'tata akub/a', 'fogo quente', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase5_id, 'ybaka oby', 'céu azul / verde', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase6_id, 'kunumĩ mirĩ', 'menino pequeno', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase7_id, 'yby katu', 'terra boa / fértil', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase8_id, 'so''o mirĩ', 'animal pequeno', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase9_id, 'pitangĩ poranga', 'bebê bonito', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        (v_frase10_id, 'oka eburusu', 'casa grande', NULL, 'FRASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

        -- Now recreate the exercises for Lição 1 (Phrases 1 to 5)
        IF v_licao1_id IS NOT NULL THEN
            -- aba katu
            WITH ex1 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase1_id, 'MULTIPLA_ESCOLHA', 'Como se diz "homem bom" em Tupi?', 'aba katu', 'Junte as palavras aba (homem) e katu (bom).', 1) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['aba katu', 'kunhã poranga', '''y katu', 'tata akub/a']) FROM ex1;
            WITH ex2 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase1_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "aba katu" em português?', 'homem bom', 'Lembre-se do significado de aba e katu.', 2) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['homem bom', 'mulher bonita', 'água limpa', 'fogo quente']) FROM ex2;

            -- kunhã poranga
            WITH ex3 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase2_id, 'MULTIPLA_ESCOLHA', 'Como se diz "mulher bonita" em Tupi?', 'kunhã poranga', 'Junte as palavras kunhã e poranga.', 3) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['aba katu', 'kunhã poranga', 'ybaka oby', 'kunumĩ mirĩ']) FROM ex3;
            WITH ex4 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase2_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "kunhã poranga" em português?', 'mulher bonita', 'Lembre-se do significado de kunhã e poranga.', 4) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['homem bom', 'mulher bonita', 'céu azul / verde', 'menino pequeno']) FROM ex4;

            -- 'y katu
            WITH ex5 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase3_id, 'MULTIPLA_ESCOLHA', 'Como se diz "água limpa" em Tupi?', '''y katu', 'Junte as palavras ''y e katu.', 5) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['''y katu', 'tata akub/a', 'yby katu', 'so''o mirĩ']) FROM ex5;
            WITH ex6 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase3_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "''y katu" em português?', 'água limpa', 'Lembre-se do significado de ''y e katu.', 6) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['água limpa', 'fogo quente', 'terra boa / fértil', 'animal pequeno']) FROM ex6;

            -- tata akub/a
            WITH ex7 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase4_id, 'MULTIPLA_ESCOLHA', 'Como se diz "fogo quente" em Tupi?', 'tata akub/a', 'Junte as palavras tata e akub/a.', 7) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['tata akub/a', 'ybaka oby', 'oka eburusu', 'aba katu']) FROM ex7;
            WITH ex8 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase4_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "tata akub/a" em português?', 'fogo quente', 'Lembre-se do significado de tata e akub/a.', 8) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['fogo quente', 'céu azul / verde', 'casa grande', 'homem bom']) FROM ex8;

            -- ybaka oby
            WITH ex9 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase5_id, 'MULTIPLA_ESCOLHA', 'Como se diz "céu azul / verde" em Tupi?', 'ybaka oby', 'Junte as palavras ybaka e oby.', 9) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['ybaka oby', 'kunumĩ mirĩ', 'pitangĩ poranga', '''y katu']) FROM ex9;
            WITH ex10 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao1_id, v_frase5_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "ybaka oby" em português?', 'céu azul / verde', 'Lembre-se do significado de ybaka e oby.', 10) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['céu azul / verde', 'menino pequeno', 'bebê bonito', 'água limpa']) FROM ex10;
        END IF;

        -- Now recreate the exercises for Lição 2 (Phrases 6 to 10)
        IF v_licao2_id IS NOT NULL THEN
            -- kunumĩ mirĩ
            WITH ex11 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase6_id, 'MULTIPLA_ESCOLHA', 'Como se diz "menino pequeno" em Tupi?', 'kunumĩ mirĩ', 'Junte as palavras kunumĩ e mirĩ.', 1) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['kunumĩ mirĩ', 'yby katu', 'so''o mirĩ', 'pitangĩ poranga']) FROM ex11;
            WITH ex12 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase6_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "kunumĩ mirĩ" em português?', 'menino pequeno', 'Lembre-se do significado de kunumĩ e mirĩ.', 2) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['menino pequeno', 'terra boa / fértil', 'animal pequeno', 'bebê bonito']) FROM ex12;

            -- yby katu
            WITH ex13 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase7_id, 'MULTIPLA_ESCOLHA', 'Como se diz "terra boa / fértil" em Tupi?', 'yby katu', 'Junte as palavras yby e katu.', 3) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['yby katu', 'oka eburusu', 'aba katu', 'kunhã poranga']) FROM ex13;
            WITH ex14 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase7_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "yby katu" em português?', 'terra boa / fértil', 'Lembre-se do significado de yby e katu.', 4) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['terra boa / fértil', 'casa grande', 'homem bom', 'mulher bonita']) FROM ex14;

            -- so'o mirĩ
            WITH ex15 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase8_id, 'MULTIPLA_ESCOLHA', 'Como se diz "animal pequeno" em Tupi?', 'so''o mirĩ', 'Junte as palavras so''o e mirĩ.', 5) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['so''o mirĩ', 'pitangĩ poranga', 'ybaka oby', 'tata akub/a']) FROM ex15;
            WITH ex16 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase8_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "so''o mirĩ" em português?', 'animal pequeno', 'Lembre-se do significado de so''o e mirĩ.', 6) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['animal pequeno', 'bebê bonito', 'céu azul / verde', 'fogo quente']) FROM ex16;

            -- pitangĩ poranga
            WITH ex17 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase9_id, 'MULTIPLA_ESCOLHA', 'Como se diz "bebê bonito" em Tupi?', 'pitangĩ poranga', 'Junte as palavras pitangĩ e poranga.', 7) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['pitangĩ poranga', 'oka eburusu', 'aba katu', '''y katu']) FROM ex17;
            WITH ex18 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase9_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "pitangĩ poranga" em português?', 'bebê bonito', 'Lembre-se do significado de pitangĩ e poranga.', 8) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['bebê bonito', 'casa grande', 'homem bom', 'água limpa']) FROM ex18;

            -- oka eburusu
            WITH ex19 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase10_id, 'MULTIPLA_ESCOLHA', 'Como se diz "casa grande" em Tupi?', 'oka eburusu', 'Junte as palavras oka e eburusu.', 9) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['oka eburusu', 'kunumĩ mirĩ', 'yby katu', 'so''o mirĩ']) FROM ex19;
            WITH ex20 AS (INSERT INTO exercicios (id, licao_id, conteudo_id, tipo, enunciado, resposta_correta, dica, ordem_index) VALUES (gen_random_uuid(), v_licao2_id, v_frase10_id, 'MULTIPLA_ESCOLHA', 'O que significa a frase "oka eburusu" em português?', 'casa grande', 'Lembre-se do significado de oka e eburusu.', 10) RETURNING id)
            INSERT INTO exercicio_opcoes (exercicio_id, opcao) SELECT id, unnest(ARRAY['casa grande', 'menino pequeno', 'terra boa / fértil', 'animal pequeno']) FROM ex20;
        END IF;
    END IF;
END $$;
