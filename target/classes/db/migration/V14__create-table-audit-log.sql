CREATE TABLE audit_log (
                           id BIGINT not null auto_increment,
                           usuario VARCHAR(255),
                           accion VARCHAR(50),
                           detalle VARCHAR(500),
                           fecha_hora TIMESTAMP NOT NULL,
                           primary key (id)

);
