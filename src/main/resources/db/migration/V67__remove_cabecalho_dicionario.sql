-- Remove registro espúrio que era cabeçalho de importação do arquivo TXT
DELETE FROM conteudos_linguisticos WHERE id = '946ada85-6320-4d1a-a2ed-736611809604' OR palavra_tupi ILIKE '%Ordem Alfabética%' OR palavra_tupi ILIKE '%Ordem Alfabetica%';

