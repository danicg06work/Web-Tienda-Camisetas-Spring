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
