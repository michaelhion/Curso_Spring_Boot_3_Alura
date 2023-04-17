create table pacientes(
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    cpf varchar(11) not null unique,
    telefone varchar(8) not null,
    logradouro varchar(100) not null,
    bairro varchar(100) not null,
    cep varchar(9) not null,
    complemento varchar(100) not null,
    numero varchar(20) not null,
    uf varchar(2) not null,
    cidade varchar(20) not null,

    primary key(id)
);