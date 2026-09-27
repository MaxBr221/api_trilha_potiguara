-- V48: Fix double single-quotes in exercicio_opcoes which break validations

-- Encontrar todas as opções que possuem 2 aspas simples consecutivas (escapadas na inserção acidentalmente) e substitui por uma.
-- Note que em PostgreSQL '''' significa UMA aspa simples dentro de uma string literal. 
-- Para buscar DUAS aspas simples textuais, usamos '%''''%'. 
-- No replace, procuramos DUAS aspas simples (escrito como '''''' no SQL para a função) 
-- So to replace two single quotes with one:
-- replace(opcao, '''''', '''')

UPDATE exercicio_opcoes 
SET opcao = replace(opcao, '''''', '''') 
WHERE opcao LIKE '%''''%';

-- Also check resposta_correta just in case
UPDATE exercicios 
SET resposta_correta = replace(resposta_correta, '''''', '''') 
WHERE resposta_correta LIKE '%''''%';

UPDATE exercicios 
SET enunciado = replace(enunciado, '''''', '''') 
WHERE enunciado LIKE '%''''%';
