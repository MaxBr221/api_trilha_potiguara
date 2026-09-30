-- Remove as palavras APENAS do dicionário (isso também apaga as associações licao_conteudo via CASCADE)
DELETE FROM conteudos_linguisticos
WHERE palavra_tupi IN ('kunumĩ', 'kunumĩ mirĩ', 'kunha''taĩ', 'cunhã');
