-- Correção da opção distratora inadequada na Lição da Iara (Módulo 2 de Mitos e Lendas)
UPDATE exercicio_opcoes 
SET opcao = 'homem' 
WHERE opcao LIKE '%sempre memé%';

