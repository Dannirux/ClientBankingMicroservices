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
    contrasena VARCHAR(50) NOT NULL,
    estado BOOLEAN NOT NULL
);

-- Tabla de Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id CHAR(36) PRIMARY KEY,
    cliente_id CHAR(36) NOT NULL,
    estado BOOLEAN NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL,
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

-- Inserción de datos iniciales en Clientes
INSERT INTO clientes (id, direccion, edad, genero, identificacion, nombre, telefono, contrasena, estado)
VALUES
    (UUID(), 'Otavalo sn y principal', 30, 'Masculino', '1718901234', 'Jose Lema', '098254785', '1234', TRUE),
    (UUID(), 'Amazonas y NNUU', 28, 'Femenino', '1712345678', 'Marianela Montalvo', '097548965', '5678', TRUE),
    (UUID(), '13 junio y Equinoccial', 40, 'Masculino', '1723456789', 'Juan Osorio', '098874587', '1245', TRUE);

-- Asigna IDs de clientes a variables
SET @id_jose = (SELECT id FROM clientes WHERE nombre = 'Jose Lema' LIMIT 1);
SET @id_marianela = (SELECT id FROM clientes WHERE nombre = 'Marianela Montalvo' LIMIT 1);
SET @id_juan = (SELECT id FROM clientes WHERE nombre = 'Juan Osorio' LIMIT 1);

-- Inserción de datos iniciales en Cuentas
INSERT INTO cuentas (id, cliente_id, estado, numero_cuenta, saldo_inicial, tipo_cuenta)
VALUES
    (UUID(), @id_jose, TRUE, '478758', 2000.00, 'AHORRO'),
    (UUID(), @id_marianela, TRUE, '225487', 100.00, 'CORRIENTE'),
    (UUID(), @id_juan, TRUE, '495878', 0.00, 'AHORRO'),
    (UUID(), @id_marianela, TRUE, '496825', 540.00, 'AHORRO'),
    (UUID(), @id_jose, TRUE, '585545', 1000.00, 'CORRIENTE');

-- Inserción de datos iniciales en Movimientos
INSERT INTO movimientos (id, cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES
    (UUID(), (SELECT id FROM cuentas WHERE numero_cuenta = '478758' LIMIT 1), NOW(), 'RETIRO', 575.00, 1425.00),
    (UUID(), (SELECT id FROM cuentas WHERE numero_cuenta = '225487' LIMIT 1), NOW(), 'DEPOSITO', 600.00, 700.00),
    (UUID(), (SELECT id FROM cuentas WHERE numero_cuenta = '495878' LIMIT 1), NOW(), 'DEPOSITO', 150.00, 150.00),
    (UUID(), (SELECT id FROM cuentas WHERE numero_cuenta = '496825' LIMIT 1), NOW(), 'RETIRO', 540.00, 0.00);

-- Listado de Movimientos por Fechas y Usuario
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
