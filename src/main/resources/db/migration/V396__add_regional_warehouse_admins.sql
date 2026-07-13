-- 1. Insert Role
INSERT INTO roles (code, name, description) 
VALUES ('REGIONAL_WAREHOUSE_ADMIN', 'Regional Warehouse Admin', 'Regional warehouse administrator')
ON CONFLICT (code) DO NOTHING;

-- 2. Grant Permissions (same permissions as REGIONAL_ADMIN)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r_new.id, rp.permission_id
FROM roles r_new
CROSS JOIN role_permissions rp
JOIN roles r_old ON rp.role_id = r_old.id
WHERE r_new.code = 'REGIONAL_WAREHOUSE_ADMIN' AND r_old.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

-- 3. Create Users & Map Roles & Map Regions & Warehouses & Employees

-- User 1: FR_NORTH
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_fr_north@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin North France', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_fr_north@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_fr_north@plus33.com' AND r.code = 'FR_NORTH'
ON CONFLICT DO NOTHING;

INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'regional_wh_admin_fr_north@plus33.com' AND w.code = 'WH_EU_01'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-01', u.id, 3, 'Regional Warehouse', 'Admin North France', 'regional_wh_admin_fr_north@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_fr_north@plus33.com'
ON CONFLICT DO NOTHING;


-- User 2: IN_SOUTH
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_in_south@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin South India', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_in_south@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_in_south@plus33.com' AND r.code = 'IN_SOUTH'
ON CONFLICT DO NOTHING;

INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'regional_wh_admin_in_south@plus33.com' AND w.code = 'WH_IND_01'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-02', u.id, 3, 'Regional Warehouse', 'Admin South India', 'regional_wh_admin_in_south@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_in_south@plus33.com'
ON CONFLICT DO NOTHING;


-- User 3: UAE_DUBAI
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_uae_dubai@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Dubai Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_uae_dubai@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_uae_dubai@plus33.com' AND r.code = 'UAE_DUBAI'
ON CONFLICT DO NOTHING;

INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'regional_wh_admin_uae_dubai@plus33.com' AND w.code = 'WH_DXB_01'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-03', u.id, 3, 'Regional Warehouse', 'Admin Dubai Region', 'regional_wh_admin_uae_dubai@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_uae_dubai@plus33.com'
ON CONFLICT DO NOTHING;


-- User 4: FR_REG_1
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_fr_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Ile-de-France', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_fr_reg_1@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_fr_reg_1@plus33.com' AND r.code = 'FR_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-04', u.id, 3, 'Regional Warehouse', 'Admin Ile-de-France', 'regional_wh_admin_fr_reg_1@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_fr_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 5: FR_REG_2
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_fr_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Provence-Alpes-Cote d''Azur', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_fr_reg_2@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_fr_reg_2@plus33.com' AND r.code = 'FR_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-05', u.id, 3, 'Regional Warehouse', 'Admin Provence-Alpes-Cote d''Azur', 'regional_wh_admin_fr_reg_2@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_fr_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 6: FR_REG_3
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_fr_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Auvergne-Rhone-Alpes', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_fr_reg_3@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_fr_reg_3@plus33.com' AND r.code = 'FR_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-06', u.id, 3, 'Regional Warehouse', 'Admin Auvergne-Rhone-Alpes', 'regional_wh_admin_fr_reg_3@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_fr_reg_3@plus33.com'
ON CONFLICT DO NOTHING;


-- User 7: AE_REG_1
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_ae_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Abu Dhabi Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_ae_reg_1@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_ae_reg_1@plus33.com' AND r.code = 'AE_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-07', u.id, 3, 'Regional Warehouse', 'Admin Abu Dhabi Region', 'regional_wh_admin_ae_reg_1@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_ae_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 8: AE_REG_2
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_ae_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Sharjah Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_ae_reg_2@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_ae_reg_2@plus33.com' AND r.code = 'AE_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-08', u.id, 3, 'Regional Warehouse', 'Admin Sharjah Region', 'regional_wh_admin_ae_reg_2@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_ae_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 9: AE_REG_3
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_ae_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin Ajman Region', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_ae_reg_3@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_ae_reg_3@plus33.com' AND r.code = 'AE_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-09', u.id, 3, 'Regional Warehouse', 'Admin Ajman Region', 'regional_wh_admin_ae_reg_3@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_ae_reg_3@plus33.com'
ON CONFLICT DO NOTHING;


-- User 10: IN_REG_1
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_in_reg_1@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin West India', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_in_reg_1@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_in_reg_1@plus33.com' AND r.code = 'IN_REG_1'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-10', u.id, 3, 'Regional Warehouse', 'Admin West India', 'regional_wh_admin_in_reg_1@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_in_reg_1@plus33.com'
ON CONFLICT DO NOTHING;


-- User 11: IN_REG_2
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_in_reg_2@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin North India', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_in_reg_2@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_in_reg_2@plus33.com' AND r.code = 'IN_REG_2'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-11', u.id, 3, 'Regional Warehouse', 'Admin North India', 'regional_wh_admin_in_reg_2@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_in_reg_2@plus33.com'
ON CONFLICT DO NOTHING;


-- User 12: IN_REG_3
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_wh_admin_in_reg_3@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Regional Warehouse', 'Admin East India', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_wh_admin_in_reg_3@plus33.com' AND r.code = 'REGIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_wh_admin_in_reg_3@plus33.com' AND r.code = 'IN_REG_3'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-RWA-12', u.id, 3, 'Regional Warehouse', 'Admin East India', 'regional_wh_admin_in_reg_3@plus33.com', '', 'Regional Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_wh_admin_in_reg_3@plus33.com'
ON CONFLICT DO NOTHING;
