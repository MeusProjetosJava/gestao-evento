create table participacoes (
                               id bigint not null auto_increment,

                               usuario_id bigint not null,
                               evento_id bigint not null,

                               estado_pagamento varchar(20) not null,
                               estado_checkin varchar(20) not null,
                               horario_checkin datetime null,

                               constraint pk_participacoes primary key (id),

                               constraint uk_participacoes_usuario_evento
                                   unique (usuario_id, evento_id),

                               constraint fk_participacoes_usuario
                                   foreign key (usuario_id)
                                       references usuarios (id),

                               constraint fk_participacoes_evento
                                   foreign key (evento_id)
                                       references eventos (id),

                               constraint ck_participacoes_estado_pagamento
                                   check (estado_pagamento in ('PENDENTE', 'PAGO')),

                               constraint ck_participacoes_estado_checkin
                                   check (estado_checkin in ('NAO_REALIZADO', 'REALIZADO'))
)
    engine=InnoDB
default charset=utf8mb4
collate=utf8mb4_unicode_ci;
