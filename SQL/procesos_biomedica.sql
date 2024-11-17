CREATE DATABASE procesos_biomedica;

USE procesos_biomedica;

CREATE TABLE Rol (
  ID int,
  tipo varchar(90),
  PRIMARY KEY (ID)
);

CREATE TABLE Usuario (
  legajo int,
  DNI int,
  nombre varchar(90),
  apellido varchar(90),
  password varchar(90),
  rolID int,
  PRIMARY KEY (legajo),
  FOREIGN KEY (rolID) REFERENCES Rol(ID)
);

CREATE TABLE Costo (
  ID int,
  mes int,
  anio int,
  valor double,
  legajoDirector int,
  PRIMARY KEY (ID),
  FOREIGN KEY (legajoDirector) REFERENCES Usuario(legajo)
);

CREATE TABLE Unidad (
  ID int,
  nombre varchar(90),
  PRIMARY KEY (ID)
);

CREATE TABLE Producto (
  ID int,
  nombre varchar(90),
  cantidad int,
  unidadID int,
  PRIMARY KEY (ID),
  FOREIGN KEY (unidadID) REFERENCES Unidad(ID)
);

CREATE TABLE Area (
  ID int,
  nombre varchar(90),
  PRIMARY KEY (ID)
);

CREATE TABLE OrdenProduccion (
  ID int,
  lote int,
  ProductoID int,
  areaID int,
  costoID int,
  iniciado boolean,
  pausado boolean,
  terminado boolean,
  PRIMARY KEY (ID),
  FOREIGN KEY (ProductoID) REFERENCES Producto(ID),
  FOREIGN KEY (areaID) REFERENCES Area(ID),
  FOREIGN KEY (costoID) REFERENCES Costo(ID)
);

CREATE TABLE CategoriaParada (
  ID int,
  descripcion varchar(90),
  PRIMARY KEY (ID)
);

CREATE TABLE Parada (
  ID int,
  OPID int,
  inicio datetime,
  fin datetime,
  catParadaID int,
  observacion varchar(255),
  PRIMARY KEY (ID),
  FOREIGN KEY (OPID) REFERENCES OrdenProduccion(ID),
  FOREIGN KEY (catParadaID) REFERENCES CategoriaParada(ID)
);

CREATE TABLE Etapa (
  ID int,
  nombre varchar(90),
  PRIMARY KEY (ID)
);

CREATE TABLE OperarioEnOrden (
  ID int,
  OPID int,
  legajoOperario int,
  etapaID int,
  inicio datetime,
  fin datetime,
  PRIMARY KEY (ID),
  FOREIGN KEY (etapaID) REFERENCES Etapa(ID),
  FOREIGN KEY (OPID) REFERENCES OrdenProduccion(ID),
  FOREIGN KEY (legajoOperario) REFERENCES Usuario(legajo)
);

INSERT INTO Rol (ID, tipo) VALUES
	(1, 'Operario'),
    (2, 'Jefe de Produccion'),
    (3, 'Jefe de Planificacion'),
    (4, 'Director');

INSERT INTO Unidad (ID, nombre) VALUES
	(1, 'Kilogramos'),
    (2, 'Comprimidos'),
    (3, 'Capsulas'),
    (4, 'Blister x 2 comp'),
    (5, 'Blister x 7 comp'),
    (6, 'Blister x 15 caps'),
    (7, 'Estuche x 2 comp'),
    (8, 'Estuche x 14 comp'),
    (9, 'Estuche x 15 caps');

INSERT INTO Area (ID, nombre) VALUES
	(1, 'Granulacion'),
    (2, 'Compresion 1'),
    (3, 'Compresion 2'),
    (4, 'Encapsulado'),
    (5, 'Acondicionamiento 1'),
    (6, 'Acondicionamiento 2'),
    (7, 'Empaque');

INSERT INTO CategoriaParada (ID, descripcion) VALUES
	(1, 'Corte electrico'),
    (2, 'Temperatura ambiente Alta'),
    (3, 'Presion baja'),
    (4, 'Comprimidos rotos'),
    (5, 'Cambio de rollo de aluminio'),
    (6, 'Problema con formato de estuche'),
    (7, 'Refrigerio'),
    (8, 'Puesta a punto dificultosa');

INSERT INTO Etapa (ID, nombre) VALUES
	(1, 'Armado'),
    (2, 'En produccion'),
    (3, 'Desarmado y limpieza');

INSERT INTO Usuario (legajo, DNI, nombre, apellido, password, rolID) VALUES
	(1, 30123456, 'Juan', 'Pérez', 'password123', 1),
	(2, 31234567, 'Ana', 'Gómez', 'password456', 3),
	(3, 32345678, 'Luis', 'Martínez', 'password789', 1),
	(4, 33456789, 'María', 'López', 'password012', 2),
	(5, 34567890, 'Carlos', 'Fernández', 'password345', 1),
	(6, 35678901, 'Laura', 'Ruiz', 'password678', 4);

INSERT INTO Costo (ID, mes, anio, valor, legajoDirector) VALUES
	(1, 6, 2024, 1500.00, 6),
	(2, 7, 2024, 1600.00, 6),
	(3, 8, 2024, 1700.00, 6);

INSERT INTO Producto (ID, nombre, cantidad, unidadID) VALUES
	(1, 'Diclofenac', 100, 1),
	(2, 'Comp Diclofenac', 150, 2),
	(3, 'Diclofenac BL x 2', 200, 4),
	(4, 'Diclofenac Est x 2', 250, 7),
	(5, 'Ibuprofeno', 300, 1),
	(6, 'Ibuprofeno 400', 350, 3),
	(7, 'Ibuprofeno 400 bl x 15', 400, 6),
	(8, 'Ibuprofeno 400 Est x 15', 450, 9);

INSERT INTO OrdenProduccion (ID, lote, ProductoID, areaID, costoID, iniciado, pausado, terminado) VALUES
    (100001, 101, 1, 1, 1, true, false, true),
    (100002, 101, 2, 2, 2, true, true, false),
    (100003, 103, 5, 1, 3, true, false, false),
    (100004, 101, 8, 7, 1, true, false, true),
    (100005, 102, 1, 1, 2, false, false, false);

INSERT INTO Parada (ID, OPID, inicio, fin, catParadaID, observacion) VALUES
    (1, 100002, '2024-10-06 10:00:00', '2024-10-06 11:00:00', 7, 'Refrigerio programado');

INSERT INTO OperarioEnOrden (ID, OPID, legajoOperario, etapaID, inicio, fin) VALUES
    (1, 100002, 1, 2, '2024-10-06 08:00:00', NULL),
    (2, 100003, 3, 1, '2024-10-06 09:00:00', NULL);

-- Muestra todas las ordenes de produccion iniciadas
SELECT 
    OrdenProduccion.ID,
    Producto.nombre AS nombreProducto,
    OrdenProduccion.lote,
    Area.nombre AS nombreArea
FROM 
    OrdenProduccion
JOIN 
    Producto ON OrdenProduccion.ProductoID = Producto.ID
JOIN 
    Area ON OrdenProduccion.areaID = Area.ID
JOIN 
    Costo ON OrdenProduccion.costoID = Costo.ID
WHERE 
    OrdenProduccion.iniciado = true AND OrdenProduccion.pausado = false;

-- Ordenes de produccion en las que se encuentra trabajando cada operario
SELECT 
    Usuario.nombre AS nombreOperario, 
    Usuario.apellido AS apellidoOperario, 
    OperarioEnOrden.OPID, 
    Producto.nombre AS nombreProducto, 
    OrdenProduccion.lote
FROM 
    OperarioEnOrden
JOIN 
    Usuario ON OperarioEnOrden.legajoOperario = Usuario.legajo
JOIN 
    OrdenProduccion ON OperarioEnOrden.OPID = OrdenProduccion.ID
JOIN 
    Producto ON OrdenProduccion.ProductoID = Producto.ID;

-- Ordenes de produccion en el área de Granulacion
SELECT OrdenProduccion.ID, Producto.nombre AS nombreProducto, Usuario.nombre AS nombreOperario, Usuario.apellido AS apellidoOperario
FROM OrdenProduccion
JOIN Producto ON OrdenProduccion.ProductoID = Producto.ID
JOIN OperarioEnOrden ON OrdenProduccion.ID = OperarioEnOrden.OPID
JOIN Usuario ON OperarioEnOrden.legajoOperario = Usuario.legajo
WHERE OrdenProduccion.areaID = 1;

-- Mostrar ordenes disponibles
SELECT 
    OrdenProduccion.ID AS numeroOrden,
    Producto.nombre AS nombreProducto,
    OrdenProduccion.lote,
    Area.nombre AS nombreArea
FROM 
    OrdenProduccion
JOIN 
    Producto ON OrdenProduccion.ProductoID = Producto.ID
JOIN 
    Area ON OrdenProduccion.areaID = Area.ID
WHERE 
    OrdenProduccion.iniciado = false 
    AND OrdenProduccion.pausado = false 
    AND OrdenProduccion.terminado = false;