drop database if exists barberia;
drop user if exists usuario_barberia;

CREATE database barberia
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

create user 'usuario_barberia'@'%' identified by 'Barber1a_Clave.';

grant select, insert, update, delete on barberia.* to 'usuario_barberia'@'%';
flush privileges;

use barberia;

create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(25) NOT NULL,
  PRIMARY KEY (id_rol),
  unique (nombre))
  ENGINE = InnoDB;

create table usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  apellidos VARCHAR(75) NOT NULL,
  correo VARCHAR(75) NOT NULL,
  telefono VARCHAR(25) NULL,
  contrasena VARCHAR(512) NOT NULL,
  id_rol INT NOT NULL,
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario),
  unique (correo),
  foreign key fk_usuario_rol (id_rol) references rol(id_rol))
  ENGINE = InnoDB;

create table servicio (
  id_servicio INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  descripcion text,
  precio decimal(12,2) NOT NULL CHECK (precio >= 0),
  duracion_minutos int unsigned NOT NULL CHECK (duracion_minutos > 0),
  activo boolean NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id_servicio),
  unique (nombre))
  ENGINE = InnoDB;

create table barbero (
  id_barbero INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(75) NOT NULL,
  telefono VARCHAR(25) NULL,
  foto varchar(1024),
  activo boolean NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id_barbero))
  ENGINE = InnoDB;

create table barbero_servicio (
  id_barbero INT NOT NULL,
  id_servicio INT NOT NULL,
  PRIMARY KEY (id_barbero, id_servicio),
  foreign key fk_barberoServicio_barbero (id_barbero) references barbero(id_barbero),
  foreign key fk_barberoServicio_servicio (id_servicio) references servicio(id_servicio))
  ENGINE = InnoDB;

create table horario (
  id_horario INT NOT NULL AUTO_INCREMENT,
  id_barbero INT NOT NULL,
  dia_semana TINYINT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
  hora_inicio TIME NOT NULL,
  hora_fin TIME NOT NULL,
  CHECK (hora_fin > hora_inicio),
  PRIMARY KEY (id_horario),
  index ndx_horario_barbero (id_barbero),
  foreign key fk_horario_barbero (id_barbero) references barbero(id_barbero))
  ENGINE = InnoDB;

create table estado_cita (
  id_estado_cita INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(25) NOT NULL,
  PRIMARY KEY (id_estado_cita),
  unique (nombre))
  ENGINE = InnoDB;

create table cita (
  id_cita INT NOT NULL AUTO_INCREMENT,
  id_cliente INT NOT NULL,
  id_barbero INT NOT NULL,
  id_servicio INT NOT NULL,
  fecha DATE NOT NULL,
  hora_inicio TIME NOT NULL,
  hora_fin TIME NOT NULL,
  id_estado_cita INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_cita),
  index ndx_cita_fecha (fecha),
  index ndx_cita_barbero (id_barbero),
  foreign key fk_cita_cliente (id_cliente) references usuario(id_usuario),
  foreign key fk_cita_barbero (id_barbero) references barbero(id_barbero),
  foreign key fk_cita_servicio (id_servicio) references servicio(id_servicio),
  foreign key fk_cita_estado (id_estado_cita) references estado_cita(id_estado_cita))
  ENGINE = InnoDB;

insert into rol (nombre) values ('Cliente'), ('Administrador');

insert into estado_cita (nombre) values ('Pendiente'), ('Confirmada'), ('Cancelada'), ('Completada');

INSERT INTO usuario (nombre, apellidos, correo, telefono, contrasena, id_rol, activo) VALUES
('Administrador', 'Barbería', 'administracion@barberia.com', NULL, 'cortesencillo2003', 2, true),
('Juan', 'Pérez', 'juanperez6572@gmail.com', '6688-2211', '9942347', 1, true);

INSERT INTO servicio (nombre, descripcion, precio, duracion_minutos, activo) VALUES
('Corte de cabello', 'Corte de cabello según el estilo solicitado por el cliente.', 5000, 30, true);

INSERT INTO barbero (nombre, telefono, foto, activo) VALUES
('Carlos Rodriguez', '6611-3344', NULL, true);

INSERT INTO barbero_servicio (id_barbero, id_servicio) VALUES (1, 1);

INSERT INTO horario (id_barbero, dia_semana, hora_inicio, hora_fin) VALUES
(1, 1, '10:00', '17:00'),
(1, 2, '10:00', '17:00'),
(1, 3, '10:00', '17:00'),
(1, 4, '10:00', '17:00'),
(1, 5, '10:00', '17:00');

INSERT INTO cita (id_cliente, id_barbero, id_servicio, fecha, hora_inicio, hora_fin, id_estado_cita) VALUES
(2, 1, 1, '2026-10-10', '22:00', '22:30', 4);
