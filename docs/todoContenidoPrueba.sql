-- Usuarios
INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('admin', 'admin@gmail.com', 1, 'admin', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'admin');

INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('operario', 'operario@gmail.com', 1, 'operario', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'operario');

INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('pepe', 'pepe@gmail.com', 1, 'pepe', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'pepe');

-- Asignación de roles (suponiendo que los IDs sean 1, 2 y 3)
INSERT INTO rol_usuario (rol, usuario_id) VALUES
('GESTOR', 1),   -- admin
('OPERARIO', 2), -- operario
('CLIENTE', 3);  -- pepe


-- Categorías principales
INSERT INTO categoria (descripcion, nombre, padre_id) VALUES 
('Todo lo relacionado con ropa deportiva', 'Deportes', NULL),
('Todo lo relacionado con ropa casual', 'Casual', NULL),
('Todo lo relacionado con ropa formal', 'Formal', NULL);

-- Subcategorías de Deportes
INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Camisetas y tops deportivos', 'Camisetas Deportivas', id FROM categoria WHERE nombre = 'Deportes';

INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Pantalones y shorts deportivos', 'Pantalones Deportivos', id FROM categoria WHERE nombre = 'Deportes';

-- Subcategorías de Casual
INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Camisetas y polos casuales', 'Camisetas Casual', id FROM categoria WHERE nombre = 'Casual';

INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Jeans y pantalones casuales', 'Pantalones Casual', id FROM categoria WHERE nombre = 'Casual';

-- Subcategorías de Formal
INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Camisas formales para oficina o eventos', 'Camisas Formales', id FROM categoria WHERE nombre = 'Formal';

INSERT INTO categoria (descripcion, nombre, padre_id)
SELECT 'Pantalones y trajes formales', 'Pantalones Formales', id FROM categoria WHERE nombre = 'Formal';

-- Camisetas para categorías principales
INSERT INTO camiseta (descripcion, nombre, precio, stock, talla, categoria_id) VALUES
('Super cómoda y ligera', 'DeporteTop', 14, 10, 'XL', 1),
('Cómoda y casual para todos los días', 'CasualCool', 12, 15, 'L', 2),
('Elegante y perfecta para eventos', 'FormalElegance', 20, 8, 'M', 3);

-- Camisetas para subcategorías de Deportes
INSERT INTO camiseta (descripcion, nombre, precio, stock, talla, categoria_id) VALUES
('Camiseta deportiva con buena transpiración', 'RunFast', 16, 12, 'M', 4),
('Short deportivo cómodo', 'SportyShort', 18, 10, 'L', 5);

-- Camisetas para subcategorías de Casual
INSERT INTO camiseta (descripcion, nombre, precio, stock, talla, categoria_id) VALUES
('Camiseta casual para diario', 'EverydayCasual', 13, 20, 'S', 6),
('Jeans casual combinable', 'CasualJeans', 22, 7, 'M', 7);

-- Camisetas para subcategorías de Formal
INSERT INTO camiseta (descripcion, nombre, precio, stock, talla, categoria_id) VALUES
('Camisa formal para oficina', 'OfficeShirt', 25, 5, 'L', 8),
('Pantalón formal elegante', 'FormalPants', 30, 4, 'M', 9);