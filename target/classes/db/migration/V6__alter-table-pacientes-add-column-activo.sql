ALTER TABLE pacientes ADD COLUMN activo tinyint;
update pacientes set activo = 1;