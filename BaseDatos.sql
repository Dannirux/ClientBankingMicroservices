-- Eliminar la base de datos si existe y crearla nuevamente
DROP DATABASE IF EXISTS ms_person_client;
CREATE DATABASE ms_person_client;
USE ms_person_client;

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id CHAR(36) PRIMARY KEY,
    direccion VARCHAR(255) NOT NULL,
    edad INT NOT NULL,
    genero VARCHAR(20) NOT NULL CHECK (genero IN ('Masculino', 'Femenino')),
    identificacion VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    cliente_id CHAR(36) NOT NULL,
    contrasena VARCHAR(50) NOT NULL,
    estado BOOLEAN NOT NULL
);

-- Tabla de Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id CHAR(36) PRIMARY KEY,
    cliente_id CHAR(36) NOT NULL,
    estado BOOLEAN NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    saldo_inicial DECIMAL(10, 2) NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL CHECK (tipo_cuenta IN ('AHORRO', 'CORRIENTE')),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Tabla de Movimientos
CREATE TABLE IF NOT EXISTS movimientos (
    id CHAR(36) PRIMARY KEY,
    fecha DATETIME NOT NULL,
    saldo DECIMAL(10, 2) NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    valor DECIMAL(10, 2) NOT NULL,
    cuenta_id CHAR(36) NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);

-- Inserción de datos en Clientes
INSERT INTO clientes (id, direccion, edad, genero, identificacion, nombre, telefono, cliente_id, contrasena, estado)
VALUES
    ('adb846f0-106c-4789-8ae0-3c62ba8c3330', 'Norte', 34, 'Femenino', '1714031935', 'Patricia Orellana', '09673223323', '5be7775f-ff66-4d1a-9adb-4869275a21aa', '12345', FALSE);

-- Inserción de datos en Cuentas
INSERT INTO cuentas (id, cliente_id, estado, numero_cuenta, saldo_inicial, tipo_cuenta)
VALUES
    ('3d287fe4-6397-4990-be60-2f72d60ef5d4', 'adb846f0-106c-4789-8ae0-3c62ba8c3330', TRUE, '012230839', 15.00, 'AHORRO');

-- Inserción de datos en Movimientos
INSERT INTO movimientos (id, cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES
    ('641a8e1a-d23e-4ee5-80c1-4d547e55bb12', '3d287fe4-6397-4990-be60-2f72d60ef5d4', '2025-01-17 19:36:45', 'RETIRO', 5.00, 15.00);

-- Listado de Movimientos
SELECT
    m.fecha,
    c.nombre AS cliente,
    cu.numero_cuenta,
    cu.tipo_cuenta,
    cu.saldo_inicial,
    cu.estado,
    m.tipo_movimiento,
    m.valor AS movimiento,
    m.saldo AS saldo_disponible
FROM movimientos m
         JOIN cuentas cu ON m.cuenta_id = cu.id
         JOIN clientes c ON cu.cliente_id = c.id
ORDER BY m.fecha;
