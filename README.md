> ## Desenvolvido por
* Bianca Borges Santana - 25.01689-0
* Giovanna Nelli Martins - 25.00958-0
* Letícia Brito dos Santos - 25.01403-6

***
Projeto acadêmico sem fins lucrativos desenvolvido em parceria com a Etec Júlio de Mesquita. Nesse sentido, objetiva auxiliar alunos ingressantes no curso técnico de química integrado ao ensino médio a identificar materiais de laboratório. Isso ocorre através de um jogo educacional desenvolvido por alunos matriculados no curso de Ciência da Computação no Instituto Mauá de Tecnologia.

***


> ## **Funcionalidades**
* Perguntas no estilo quiz com graus dificuldade e imagens referentes aos materiais de identificação;
* Possibilidade de associação entre material, função e sistema experimental;
* Sistema de pontuação simples por partida;
* Possibilidade de solicitação de ajudas, como eliminar alternativas, chance extra e pular questão;
* Possibilidade de cadastro de questões pelo professor responsável pela turma;
* Acesso do professor a relatórios de desempenho.


> ## Tecnologias utilizadas

* __Linguagem:__ Java.
* __Banco de dados:__ MySQL.

> ## Agradecimentos
Agradecemos imensamente ao professor Evandro Catelani Ferraz e aos orientadores Rudolf Theoderich Buhler e Alexsander Tressino de Carvalho pelo suporte oferecido durante todo o desenvolvimento do projeto.

> ## **Como executar**

### **1° Clonar o repositório**

git clone < link do repositório >

cd < pasta >

### **2° Configurar o banco**

Execute migration/Script_PI_ETEC no MySQL

Crie o arquivo .env na raiz:

DB_URL=jdbc:mysql://localhost:3306/nome_do_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha

### **3° Rodar**

No terminal rode mvn exec:java

Ou

Abra src/aplicativo/labtech.java e clique em Run Java

### **Usuários de demonstração**
| Perfil    | E-mail                      | Senha       |
|-----------|-----------------------------|-------------|
| Aluno     | aluno@aluno.cps.sp.gov.br   | 11111111111 |
| Professor | professor@cps.sp.gov.br     | 11111111111 |
