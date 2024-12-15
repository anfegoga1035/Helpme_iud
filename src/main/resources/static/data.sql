-- CREAR USUARIO A BAJO NIVEL

INSERT INTO users(id, username, password, nombre,
apellidos, enabled, fecha_nacimiento, created_at)
VALUES (1, 'julio.martinez@iudigital.edu.co', '$2a$10$d7EIoUYp7t3zYL81r3jwhOg8PnDL9QpDV/DdK1uEImtKscgQqoH7u',
'Julio',
'Martinez', true, '1985-08-05', current_timestamp);

-- CREACION ROLES A BAJO NIVEL

INSERT INTO roles (id, nombre, descripcion)
VALUES (1, "ROLE_ADMIN", "Administrador del sistema");

INSERT INTO roles (id, nombre, descripcion)
VALUES (2, "ROLE_USER", "Usuario normal auto-registrado");

-- ASOCIACION DE ROLES A BAJO NIVEL AL ADMIN

INSERT INTO roles_users(users_id, roles_id)
VALUES (1, 1);

INSERT INTO roles_users(users_id, roles_id)
VALUES (1, 2);
