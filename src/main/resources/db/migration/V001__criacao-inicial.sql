create table usuarios (
                          id bigint not null auto_increment,
                          nome varchar(60) not null,
                          email varchar(60) not null,
                          senha varchar(60) not null,
                          tipo_usuario varchar(20) not null,

                          constraint pk_usuarios primary key (id),
                          constraint uk_usuarios_email unique (email),
                          constraint ck_usuarios_tipo check (tipo_usuario in ('ADMIN', 'USUARIO'))
)engine=InnoDB default charset=utf8mb4;


