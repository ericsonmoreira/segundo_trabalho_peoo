# Segundo trabalho prático da disciplina PEOO da UECE semestre 2019.2

__Valor__: 2 pontos

__Data de entrega__: 08 de abril

__Equipe__: até 2 pessoas

Nome                                        | Matrícula
:-------------------------------------------|-------------:
Éricson Rogério Moreira                     |1538663
Paulo Henrique Souza Filho                  |1509022 

## Pede-se

Considere um arquivo texto onde cada linha representa as respostas de uma prova objetiva de um aluno. Essa prova contém 10 questões, todas do tipo V ou F. O final de cada linha contém o nome do aluno que respondeu aquelas opções separadas das respostas por um “tab”. A Figura abaixo mostra um exemplo desse arquivo:

![Alt Text](https://github.com/ericsonmoreira/segundo_trabalho_peoo/blob/master/imgs/imagem.png)

Crie um programa que faça os seguintes itens:

- [x] Permita ao usuário criar o arquivo com as respostas de todos os aluno de uma disciplina. O nome do arquivo será o nome da disciplina. O usuário é livre para inserir quantos alunos ele queira. O usuário pode criar mais de uma disciplina. 

- [x] Permita ao usuário gerar o resultado de uma disciplina. Seu programa deve permitir ao usuário escolher a disciplina e então informar a localização do arquivo contendo o gabarito oficial da prova (apenas uma linha com as 10 respostas corretas) da disciplina escolhida. Em seguida, deve produzir como resposta dois outros arquivos: um contendo a lista dos alunos e seus respectivos pontos (número de acertos) ordenadas por ordem alfabética, e outro contendo as mesmas informações, porém ordenado por ordem decrescente de notas (quantidade de acertos) e mostrando ao final a média da turma. Caso o aluno tenha marcado todas as questões com V ou F, o aluno receberá a 0. Permita ao usuário visualizar esses dados na tela. 

- [x] Crie também uma opção de criar o histórico dos alunos. Para tanto, leia o resultado de cada disciplina e crie um arquivo para cada aluno contendo as disciplinas e notas obtida por ele. Ao final, grave também a média do aluno. O nome do arquivo é o nome do aluno.

Fique livre para organizar seus arquivos em diretórios da melhor maneira possível.

## Como Funciona

O progama usa a seguinte hierarquia de pastas:

 - doc/alunos/ (pasta onde ficam os arquivos de histórico dos alunos)
 - doc/disciplinas/ (pasta onde ficam salvas as disciplinas cadastradas)
 - doc/gabaritos/ (pasta onde são salvos os gabaritos cadastrados)
 - doc/disciplinas/resultados/ (pasta onde ficam salvos os resultados)
 - doc/disciplinas/resultados/ord_nota/ (sub-pasta para resultados por ondem decrescente de nota)
 - doc/disciplinas/resultados/ord_nome/ (sub-pasta para resultados por ordem do nome do aluno)

Não é preciso criar essas pastas antes de testar o programa, pois elas são criadas no caso de não existirem.

A classe br.uece.peoo.Main deve ser executada para poder usar o programa. 
