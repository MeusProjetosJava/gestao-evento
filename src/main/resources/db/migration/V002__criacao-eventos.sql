create table eventos (
                         id bigint not null auto_increment,
                         nome varchar(60) not null,
                         horario datetime not null,
                         localizacao varchar(60) not null,
                         atracao varchar(60) not null,
                         preco decimal(10,2) not null,
                         estado varchar(20) not null,

                         constraint pk_eventos primary key (id),
                         constraint ck_eventos_estado check (estado in ('ATIVO', 'ENCERRADO'))
)
    engine=InnoDB
default charset=utf8mb4
collate=utf8mb4_unicode_ci;