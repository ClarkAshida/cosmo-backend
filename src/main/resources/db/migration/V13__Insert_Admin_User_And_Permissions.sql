-- Insert initial permissions
INSERT INTO permission (id, description) VALUES (1, 'ROLE_ADMIN');
INSERT INTO permission (id, description) VALUES (2, 'ROLE_USER');

-- Insert initial admin user
-- Password is "admin123" hashed with BCrypt (strength 10)
INSERT INTO users (id, email, first_name, last_name, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES (1, 'admin@cosmo.com', 'Admin', 'User', '$2a$10$eFYJ6WM7cndJkqLvUvuebOYVLiJRALDA5D37OKJ6OLsIninEDVByi', b'1', b'1', b'1', b'1');

-- Assign admin role to the admin user
INSERT INTO user_permission (id_user, id_permission) VALUES (1, 1);
INSERT INTO user_permission (id_user, id_permission) VALUES (1, 2);
