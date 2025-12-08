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
