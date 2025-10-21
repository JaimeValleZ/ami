ALTER TABLE usuarios ADD COLUMN activo tinyint;
update usuarios set activo = 1;