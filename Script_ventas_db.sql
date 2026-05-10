-- ======================================================
-- ELIMINAR TABLAS EXISTENTES (orden correcto por dependencias)
-- ======================================================

-- Primero eliminar las tablas con foreign keys
DROP TABLE IF EXISTS logs_ventas CASCADE;
DROP TABLE IF EXISTS logs_acceso CASCADE;
DROP TABLE IF EXISTS camisas_estampado CASCADE;
DROP TABLE IF EXISTS camisas CASCADE;
DROP TABLE IF EXISTS pantalones CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS prendas CASCADE;

-- Eliminar vistas si existen
DROP VIEW IF EXISTS vw_inventario_por_tipo CASCADE;
DROP VIEW IF EXISTS vw_ventas_diarias CASCADE;
DROP VIEW IF EXISTS vw_stock_bajo CASCADE;

-- Eliminar funciones y triggers
DROP TRIGGER IF EXISTS trg_actualizar_stock ON logs_ventas;
DROP FUNCTION IF EXISTS actualizar_stock();

-- ======================================================
-- CREAR TABLAS NUEVAS (UNIFICADAS)
-- ======================================================

-- 1. TABLA DE USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100),
    last_active TIMESTAMP
);

-- 2. TABLA DE LOGS DE ACCESO
CREATE TABLE IF NOT EXISTS logs_acceso (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    actividad VARCHAR(500) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABLA DE PRENDAS (UNIFICADA)
CREATE TABLE IF NOT EXISTS prendas (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(30) NOT NULL,           -- CAMISA, CAMISA_ESTAMPADO, PANTALON
    marca VARCHAR(100) NOT NULL,
    tamanio VARCHAR(10) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 0,
    tipo_estampado VARCHAR(20)            -- PLASTICO, BORDADO (solo para camisas con estampado)
);

-- 4. TABLA DE LOGS DE VENTAS
CREATE TABLE IF NOT EXISTS logs_ventas (
    id BIGSERIAL PRIMARY KEY,
    prenda_id BIGINT NOT NULL,
    tipo_prenda VARCHAR(30) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    cantidad_vendida INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    total_venta DECIMAL(10,2) NOT NULL,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Camisas normales
INSERT INTO prendas (tipo, marca, tamanio, precio, cantidad, tipo_estampado) VALUES
('CAMISA', 'Nike', 'S', 39.99, 15, NULL),
('CAMISA', 'Nike', 'M', 45.99, 20, NULL),
('CAMISA', 'Nike', 'L', 45.99, 15, NULL),
('CAMISA', 'Nike', 'XL', 49.99, 10, NULL),
('CAMISA', 'Adidas', 'S', 42.99, 12, NULL),
('CAMISA', 'Adidas', 'M', 49.99, 18, NULL),
('CAMISA', 'Adidas', 'L', 49.99, 14, NULL),
('CAMISA', 'Puma', 'M', 39.99, 10, NULL),
('CAMISA', 'Puma', 'L', 44.99, 8, NULL);

-- Camisas con estampado
INSERT INTO prendas (tipo, marca, tamanio, precio, cantidad, tipo_estampado) VALUES
('CAMISA_ESTAMPADO', 'Nike', 'M', 59.99, 8, 'PLASTICO'),
('CAMISA_ESTAMPADO', 'Nike', 'L', 64.99, 5, 'PLASTICO'),
('CAMISA_ESTAMPADO', 'Adidas', 'M', 69.99, 6, 'BORDADO'),
('CAMISA_ESTAMPADO', 'Adidas', 'L', 74.99, 4, 'BORDADO'),
('CAMISA_ESTAMPADO', 'Puma', 'M', 54.99, 7, 'PLASTICO'),
('CAMISA_ESTAMPADO', 'Puma', 'L', 59.99, 5, 'PLASTICO'),
('CAMISA_ESTAMPADO', 'Ralph Lauren', 'M', 89.99, 3, 'BORDADO'),
('CAMISA_ESTAMPADO', 'Ralph Lauren', 'L', 94.99, 2, 'BORDADO');

-- Pantalones
INSERT INTO prendas (tipo, marca, tamanio, precio, cantidad, tipo_estampado) VALUES
('PANTALON', 'Levis', '28', 79.99, 8, NULL),
('PANTALON', 'Levis', '30', 84.99, 12, NULL),
('PANTALON', 'Levis', '32', 89.99, 10, NULL),
('PANTALON', 'Levis', '34', 89.99, 8, NULL),
('PANTALON', 'Levis', '36', 94.99, 5, NULL),
('PANTALON', 'Dockers', '30', 74.99, 10, NULL),
('PANTALON', 'Dockers', '32', 79.99, 12, NULL),
('PANTALON', 'Dockers', '34', 79.99, 8, NULL),
('PANTALON', 'Wrangler', '32', 69.99, 6, NULL),
('PANTALON', 'Wrangler', '34', 74.99, 4, NULL);

SELECT id, tipo_prenda, marca, tamanio, precio, cantidad FROM prendas;

UPDATE prendas SET tipo_prenda = 'CAMISA' WHERE marca IN ('Nike', 'Adidas', 'Puma') AND precio < 50 AND cantidad > 0;

-- Camisas con estampado (precios 59.99 - 94.99 de Nike, Adidas, Puma, Ralph Lauren)
UPDATE prendas SET tipo_prenda = 'CAMISA_PLASTICO' WHERE marca IN ('Nike', 'Adidas', 'Puma', 'Ralph Lauren') AND precio >= 50 AND id BETWEEN 10 AND 17;

-- Pantalones (Levis, Dockers, Dorkare)
UPDATE prendas SET tipo_prenda = 'PANTALON' WHERE marca IN ('Levis', 'Dockers', 'Dorkare');