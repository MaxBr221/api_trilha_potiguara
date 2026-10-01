-- ==========================================
-- Reordenar Exercícios: Módulo 3 Mitos e Lendas
-- Evitar que a mesma palavra apareça em sequência.
-- ==========================================

DO $$ 
DECLARE
    v_licao_id UUID;
    v_ex_id UUID;
    v_ordem INT;
BEGIN
    FOR v_licao_id IN 
        SELECT l.id FROM licoes l JOIN modulos m ON l.modulo_id = m.id WHERE m.titulo LIKE 'Módulo 3: Deuses, Lendas%'
    LOOP
        v_ordem := 1;
        -- Primeiro os de Tupi->PTBR (que contêm "em português")
        FOR v_ex_id IN 
            SELECT id FROM exercicios WHERE licao_id = v_licao_id AND enunciado LIKE '%em português%' ORDER BY id
        LOOP
            UPDATE exercicios SET ordem_index = v_ordem WHERE id = v_ex_id;
            v_ordem := v_ordem + 1;
        END LOOP;

        -- Depois os de PTBR->Tupi (que contêm "em Tupi")
        FOR v_ex_id IN 
            SELECT id FROM exercicios WHERE licao_id = v_licao_id AND enunciado LIKE '%em Tupi%' ORDER BY id
        LOOP
            UPDATE exercicios SET ordem_index = v_ordem WHERE id = v_ex_id;
            v_ordem := v_ordem + 1;
        END LOOP;
    END LOOP;
END $$;
