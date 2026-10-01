---
description: Padrões obrigatórios para geração de módulos e lições na Trilha Tupi
---

# Padrões de Criação de Exercícios e Módulos

Ao criar ou gerar novos exercícios, lições ou módulos para o aplicativo Trilha Tupi, o Agente **deve** obrigatoriamente seguir as 3 regras abaixo. Estas regras garantem o comportamento esperado da Tela de Dicas e a correta fluidez metodológica do aprendizado.

## 1. Vínculo de Conteúdos (Tela de Dicas e Dica Interativa)
Para cada lição criada, todas as palavras ou frases que forem ensinadas nessa lição devem ser mapeadas na tabela `licao_conteudo`. 
* **Por que?** Isso permite que a **tela de dicas antes de iniciar a lição** seja preenchida corretamente com o vocabulário, e também aciona a **lâmpada de dica** que aparece para o usuário caso ele erre o exercício mais de 2 vezes.
* **Como fazer?** Na sua script/migration SQL, sempre faça `INSERT INTO licao_conteudo (licao_id, conteudo_id)` para cada UUID da tabela `conteudos_linguisticos` associado aos exercícios da lição.

## 2. Mistura Inteligente de Exercícios (Ordem das Perguntas)
A sequência dos exercícios de uma lição (`ordem_index`) **NÃO** deve ser completamente aleatória, e você **NÃO** deve fazer exercícios da mesma palavra aparecerem em sequência (Ex: não pergunte "O que significa tatá" e logo depois "Como se diz fogo").
* **Como fazer?** Intercale a ordem dos exercícios agrupando-os por "direção de tradução". Por exemplo:
  * `ordem_index` 1 a N: Todos os exercícios de tradução Tupi -> PTBR (ex: "O que significa X em português?").
  * `ordem_index` N+1 a 2N: Todos os exercícios de tradução PTBR -> Tupi (ex: "Como se diz Y em Tupi?").
Isso garante o distanciamento da mesma palavra e permite que o usuário assimile melhor o conteúdo.

## 3. Geração Cuidadosa de Strings e Respostas Corretas
* Certifique-se de que palavras com aspas simples (glotal stop, ex: `'anga` ou `'y`) sejam corretamente escapadas no SQL (ex: `''anga`) para que o banco receba exatamente `'anga`.
* Certifique-se de utilizar apenas palavras que já estejam previamente registradas na tabela `conteudos_linguisticos` e no JSON primário do projeto, garantindo consistência com o banco.
