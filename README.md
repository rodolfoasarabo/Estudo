# Desafio Poppin


## Proposta

A proposta deste desafio é verificar seu conhecimento em algumas etapas no processo de desenvolvimento de um aplicativo, tais como, entendimento de requisitos, construção visual (Design), legibilidade e estrutura do código e uso de ferramentas que auxiliem em todo o processo. 
O objetivo do aplicativo, é ser um buscador de filmes simples, onde o usuário consiga fazer buscas por nome de filmes e visualize todos os seus detalhes.

Para este desafio utilizaremos a API aberta do IMDB (http://www.omdbapi.com/), com a proposta de criar uma aplicação seguindo as especificações detalhadas a seguir.




## Requisitos

O aplicativo deve ser desenvolvido em Java

O aplicativo deve lidar apenas com respostas do tipo filmes (a API também tem informações de séries, episódios, etc., mas utilizaremos apenas a opção de filmes)

O aplicativo deve conter pelo menos duas telas:




###### Tela inicial:
 - Estado inicial da tela indicando a ação de busca
 - Barra de pesquisa
 - Lista com resultados da pesquisa*
 - Estado vazio para quando uma busca não retornar resultados
 - Estado de erro para quando a resposta da api for um erro, indicando ao usuário o que aconteceu




###### Tela de detalhes:
 - Exibir detalhes do item selecionado na lista da tela inicial**
 - Estado de erro ao carregar todos os detalhes




\* Cada item da lista deve conter as seguintes informações do filme: 
 - Nome
 - Ano
 - Imagem




** A tela de detalhe do filme deve exibir:
 - Nome
 - Data de lançamento
 - Classificação etária 	
 - Gênero
 - Info do diretor
 - Roteirista
 - Descrição
 - Imagem
 - Avaliações disponíveis (com fonte e nota) (fonte: imdb, rotten tomatoes, etc)




## Fluxos esperados

###### Tela inicial:
 - A busca deve ser do tipo “filmes”, utilizando a requisição de `search` do OMDB  (consultar parâmetros de requisição na documentação da api OMDB)
 - O clique no item da lista deve levar para o detalhe do item clicado.




###### Tela de detalhes do filme:
 - Essa tela deve ser aberta ao clicar em um filme da lista
 - Ao abrir a tela de detalhe deve ser feita uma nova requisição pelo `id` do filme selecionado, assim, carregando todos os detalhes disponíveis daquele filme
 - Exibir todos os detalhes do filme no retorno da requisição **




###### Tratamento de erros
 - Tratar casos de erro nas requisições de carregamento da lista e carregamento de detalhes do filmes para:
 - Busca sem resultados
 - Erro de conexão




> Dica: observar todos os parâmetros disponíveis na documentação


## Avaliação
	
A avaliação desse projeto se dará pelos pontos listados abaixo, e a validação dos fluxos listados acima. 

É totalmente aberto o uso de quaisquer bibliotecas que auxiliem no desenvolvimento do projeto.

Fica também a seu critério qual design pattern usar (MPV, MVP, MVVM, Rx, etc), assim como o design (UI).

- Qualidade, legibilidade e estrutura do código
- Modelagem dos objetos
- Uso do Git (commit, merge, etc.)
- Design (Cuidados com a UX)
- Documentação
- Domínio da plataforma




Requisitos não obrigatórios mas que serão um diferencial:
 - Testes
 - Animações



## Entrega

A entrega do projeto deve ser feita até o dia 07/08, e a submissão deve ser feita em um repositório no seu próprio Github. 
Assim que o projeto estiver disponível para avaliação em seu github, nos envie a URL do projeto no e-mail desafio@poppinapp.com.br com o assunto [Entrega/Android] - \<Seu nome>. 

Para qualquer dúvida, sinta-se à vontade para entrar em contato conosco no email desafio@poppinapp.com.br.

Boa sorte!
