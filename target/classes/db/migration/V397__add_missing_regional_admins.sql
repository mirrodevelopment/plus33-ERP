-- 1. Create Users for missing standard Regional Admins (regions 4-12)
-- Password hash for 'pass123' is '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC'

-- User 1: FR_REG_1 (Ile-de-France)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_fr_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Ile-de-France', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_fr_reg_1@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_fr_reg_1@plus33.com' AND r.code = 'FR_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-04', u.id, 3, 'Regional Admin', 'Ile-de-France', 'regional_admin_fr_reg_1@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_fr_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 2: FR_REG_2 (Provence-Alpes-Cote d'Azur)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_fr_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Provence-Alpes-Cote d''Azur', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_fr_reg_2@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_fr_reg_2@plus33.com' AND r.code = 'FR_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-05', u.id, 3, 'Regional Admin', 'Provence-Alpes-Cote d''Azur', 'regional_admin_fr_reg_2@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_fr_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 3: FR_REG_3 (Auvergne-Rhone-Alpes)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_fr_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Auvergne-Rhone-Alpes', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_fr_reg_3@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_fr_reg_3@plus33.com' AND r.code = 'FR_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-06', u.id, 3, 'Regional Admin', 'Auvergne-Rhone-Alpes', 'regional_admin_fr_reg_3@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_fr_reg_3@plus33.com'
ON CONFLICT DO NOTHING;


-- User 4: AE_REG_1 (Abu Dhabi)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_ae_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Abu Dhabi Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_ae_reg_1@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_ae_reg_1@plus33.com' AND r.code = 'AE_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-07', u.id, 3, 'Regional Admin', 'Abu Dhabi Region', 'regional_admin_ae_reg_1@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_ae_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 5: AE_REG_2 (Sharjah)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_ae_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Sharjah Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_ae_reg_2@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_ae_reg_2@plus33.com' AND r.code = 'AE_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-08', u.id, 3, 'Regional Admin', 'Sharjah Region', 'regional_admin_ae_reg_2@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_ae_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 6: AE_REG_3 (Ajman)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_ae_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'Ajman Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_ae_reg_3@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_ae_reg_3@plus33.com' AND r.code = 'AE_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-09', u.id, 3, 'Regional Admin', 'Ajman Region', 'regional_admin_ae_reg_3@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_ae_reg_3@plus33.com'
ON CONFLICT DO NOTHING;


-- User 7: IN_REG_1 (West India)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_in_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'West India Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_in_reg_1@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_in_reg_1@plus33.com' AND r.code = 'IN_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-10', u.id, 3, 'Regional Admin', 'West India Region', 'regional_admin_in_reg_1@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_in_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 8: IN_REG_2 (North India)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_in_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'North India Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_in_reg_2@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_in_reg_2@plus33.com' AND r.code = 'IN_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-11', u.id, 3, 'Regional Admin', 'North India Region', 'regional_admin_in_reg_2@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_in_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 9: IN_REG_3 (East India)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_admin_in_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Admin', 'East India Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_admin_in_reg_3@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_admin_in_reg_3@plus33.com' AND r.code = 'IN_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RA-12', u.id, 3, 'Regional Admin', 'East India Region', 'regional_admin_in_reg_3@plus33.com', 'Regional Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_admin_in_reg_3@plus33.com'
ON CONFLICT DO NOTHING;
