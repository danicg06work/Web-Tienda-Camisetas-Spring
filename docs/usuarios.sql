-- Usuarios
INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('admin', 'admin@gmail.com', CONVERT(b'', UNSIGNED), 'admin', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'admin');

INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('operario', 'operario@gmail.com', CONVERT(b'', UNSIGNED), 'operario', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'operario');

INSERT INTO usuario (apellido, email, enabled, nombre, password, username)
VALUES ('pepe', 'pepe@gmail.com', CONVERT(b'', UNSIGNED), 'pepe', '$2a$10$GWBmNQ.2hkcOAXZl.X7IIudgS0z4badLtcR.Q9mVjoDtuuQ2/RooS', 'pepe');

-- Asignación de roles (suponiendo que los IDs sean 1, 2 y 3)
INSERT INTO rol_usuario (rol, usuario_id) VALUES
('GESTOR', 1),   -- admin
('OPERARIO', 2), -- operario
('CLIENTE', 3);  -- pepe