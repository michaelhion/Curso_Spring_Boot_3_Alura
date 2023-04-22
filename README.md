
# Projeto do curso de Spring boot

Projeto para treinamento do framework Spring boot na versão 3


## Stack utilizada



**Back-end:** 
- Spring boot 3
- Myql
- Flyway
- java 17


## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

`DDATASOURCE_USERNAME`

`DDATASOURCE_PASSWORD`

`-DDATASOURCE_URL`

## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/michaelhion/Curso_Spring_Boot_3_Alura.git
```

Entre no diretório do projeto

```bash
  cd Curso_Spring_Boot_3_Alura
```

Build do projeto

```bash
  mvn package
```

Inicie o servidor

```bash
  java -Dspring.profiles.active=prod -DDATASOURCE_URL=jdbc:mysql://<sua url do db> -DDATASOURCE_USERNAME=<seu db username> -DDATASOURCE_PASSWORD=<seu db password> -jar target/api-0.0.1-SNAPSHOT.jar
```

Acesse 

```
  http://localhost:8080/swagger-ui/index.html#/
```
## Funcionalidades

- Cadastar Médicos
- Atualizar Médicos
- Excluir Médicos
- Cadastar Pacientes
- Atualizar Pacientes
- Excluir Pacientes
- Agendar Consultas
- Excluir Consultas



## Aprendizados

Este projeto visou aprendizado de boas práticas de programação
para criar um serviço rest com spring boot e mysql, além disso foram cridas migrations com flyway e implementado metodos da biblioteca auth0 para autenthcar os usuários.


## Documentação (após inicializar o servidor)

```
  http://localhost:8080/swagger-ui/index.html#/
```


## Requisitos de sistema

É necessário ter instalado no computador o jdk 17 e maven para 
poder rodar o projeto localmente
