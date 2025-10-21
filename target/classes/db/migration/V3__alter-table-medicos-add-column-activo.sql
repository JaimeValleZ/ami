ALTER TABLE medicos ADD COLUMN activo tinyint;
update medicos set activo = 1;