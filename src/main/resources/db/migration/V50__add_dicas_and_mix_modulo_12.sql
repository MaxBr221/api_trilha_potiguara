-- V50: Add Module 12 phrases to licao_conteudo (for Dicas screen) and mix exercise order

DO $$ 
DECLARE
    v_modulo_id UUID;
    v_licao1_id UUID;
    v_licao2_id UUID;
    v_frase1_id UUID;
    v_frase2_id UUID;
    v_frase3_id UUID;
    v_frase4_id UUID;
    v_frase5_id UUID;
    v_frase6_id UUID;
    v_frase7_id UUID;
    v_frase8_id UUID;
    v_frase9_id UUID;
    v_frase10_id UUID;
BEGIN
    SELECT id INTO v_modulo_id FROM modulos WHERE titulo LIKE '%12%Formando Frases%';
    
    IF v_modulo_id IS NOT NULL THEN
        SELECT id INTO v_licao1_id FROM licoes WHERE modulo_id = v_modulo_id AND titulo LIKE '%Lição 1%';
        SELECT id INTO v_licao2_id FROM licoes WHERE modulo_id = v_modulo_id AND titulo LIKE '%Lição 2%';

        -- Get phrase IDs
        SELECT id INTO v_frase1_id FROM conteudos_linguisticos WHERE palavra_tupi = 'aba katu' LIMIT 1;
        SELECT id INTO v_frase2_id FROM conteudos_linguisticos WHERE palavra_tupi = 'kunhã poranga' LIMIT 1;
        SELECT id INTO v_frase3_id FROM conteudos_linguisticos WHERE palavra_tupi = '''y katu' LIMIT 1;
        SELECT id INTO v_frase4_id FROM conteudos_linguisticos WHERE palavra_tupi = 'tata akub/a' LIMIT 1;
        SELECT id INTO v_frase5_id FROM conteudos_linguisticos WHERE palavra_tupi = 'ybaka oby' LIMIT 1;
        SELECT id INTO v_frase6_id FROM conteudos_linguisticos WHERE palavra_tupi = 'kunumĩ mirĩ' LIMIT 1;
        SELECT id INTO v_frase7_id FROM conteudos_linguisticos WHERE palavra_tupi = 'yby katu' LIMIT 1;
        SELECT id INTO v_frase8_id FROM conteudos_linguisticos WHERE palavra_tupi = 'so''o mirĩ' LIMIT 1;
        SELECT id INTO v_frase9_id FROM conteudos_linguisticos WHERE palavra_tupi = 'pitangĩ poranga' LIMIT 1;
        SELECT id INTO v_frase10_id FROM conteudos_linguisticos WHERE palavra_tupi = 'oka eburusu' LIMIT 1;

        -- Link to Licao Conteudo (Dicas screen)
        IF v_licao1_id IS NOT NULL THEN
            DELETE FROM licao_conteudo WHERE licao_id = v_licao1_id;
            INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES 
            (v_licao1_id, v_frase1_id),
            (v_licao1_id, v_frase2_id),
            (v_licao1_id, v_frase3_id),
            (v_licao1_id, v_frase4_id),
            (v_licao1_id, v_frase5_id);
            
            -- Mix questions in Licao 1
            UPDATE exercicios SET ordem_index = 1 WHERE licao_id = v_licao1_id AND resposta_correta = 'aba katu';
            UPDATE exercicios SET ordem_index = 6 WHERE licao_id = v_licao1_id AND resposta_correta = 'homem bom';
            UPDATE exercicios SET ordem_index = 2 WHERE licao_id = v_licao1_id AND resposta_correta = 'kunhã poranga';
            UPDATE exercicios SET ordem_index = 7 WHERE licao_id = v_licao1_id AND resposta_correta = 'mulher bonita';
            UPDATE exercicios SET ordem_index = 3 WHERE licao_id = v_licao1_id AND resposta_correta = '''y katu';
            UPDATE exercicios SET ordem_index = 8 WHERE licao_id = v_licao1_id AND resposta_correta = 'água limpa';
            UPDATE exercicios SET ordem_index = 4 WHERE licao_id = v_licao1_id AND resposta_correta = 'tata akub/a';
            UPDATE exercicios SET ordem_index = 9 WHERE licao_id = v_licao1_id AND resposta_correta = 'fogo quente';
            UPDATE exercicios SET ordem_index = 5 WHERE licao_id = v_licao1_id AND resposta_correta = 'ybaka oby';
            UPDATE exercicios SET ordem_index = 10 WHERE licao_id = v_licao1_id AND resposta_correta = 'céu azul / verde';
        END IF;

        IF v_licao2_id IS NOT NULL THEN
            DELETE FROM licao_conteudo WHERE licao_id = v_licao2_id;
            INSERT INTO licao_conteudo (licao_id, conteudo_id) VALUES 
            (v_licao2_id, v_frase6_id),
            (v_licao2_id, v_frase7_id),
            (v_licao2_id, v_frase8_id),
            (v_licao2_id, v_frase9_id),
            (v_licao2_id, v_frase10_id);

            -- Mix questions in Licao 2
            UPDATE exercicios SET ordem_index = 1 WHERE licao_id = v_licao2_id AND resposta_correta = 'kunumĩ mirĩ';
            UPDATE exercicios SET ordem_index = 6 WHERE licao_id = v_licao2_id AND resposta_correta = 'menino pequeno';
            UPDATE exercicios SET ordem_index = 2 WHERE licao_id = v_licao2_id AND resposta_correta = 'yby katu';
            UPDATE exercicios SET ordem_index = 7 WHERE licao_id = v_licao2_id AND resposta_correta = 'terra boa / fértil';
            UPDATE exercicios SET ordem_index = 3 WHERE licao_id = v_licao2_id AND resposta_correta = 'so''o mirĩ';
            UPDATE exercicios SET ordem_index = 8 WHERE licao_id = v_licao2_id AND resposta_correta = 'animal pequeno';
            UPDATE exercicios SET ordem_index = 4 WHERE licao_id = v_licao2_id AND resposta_correta = 'pitangĩ poranga';
            UPDATE exercicios SET ordem_index = 9 WHERE licao_id = v_licao2_id AND resposta_correta = 'bebê bonito';
            UPDATE exercicios SET ordem_index = 5 WHERE licao_id = v_licao2_id AND resposta_correta = 'oka eburusu';
            UPDATE exercicios SET ordem_index = 10 WHERE licao_id = v_licao2_id AND resposta_correta = 'casa grande';
        END IF;
    END IF;
END $$;
