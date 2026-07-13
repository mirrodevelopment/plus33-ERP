-- 1. Insert Role
INSERT INTO roles (code, name, description) 
VALUES ('NATIONAL_WAREHOUSE_ADMIN', 'National Warehouse Admin', 'National warehouse administrator')
ON CONFLICT (code) DO NOTHING;

-- 2. Grant Permissions (same permissions as REGIONAL_ADMIN)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r_new.id, rp.permission_id
FROM roles r_new
CROSS JOIN role_permissions rp
JOIN roles r_old ON rp.role_id = r_old.id
WHERE r_new.code = 'NATIONAL_WAREHOUSE_ADMIN' AND r_old.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

-- 3. Create Users
-- France NWA
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('national_wh_admin_fr@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'National Warehouse', 'Admin France', TRUE)
ON CONFLICT (email) DO NOTHING;

-- UAE NWA
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('national_wh_admin_ae@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'National Warehouse', 'Admin UAE', TRUE)
ON CONFLICT (email) DO NOTHING;

-- India NWA
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('national_wh_admin_in@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'National Warehouse', 'Admin India', TRUE)
ON CONFLICT (email) DO NOTHING;

-- 4. Map Users to Role
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'national_wh_admin_fr@plus33.com' AND r.code = 'NATIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'national_wh_admin_ae@plus33.com' AND r.code = 'NATIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'national_wh_admin_in@plus33.com' AND r.code = 'NATIONAL_WAREHOUSE_ADMIN'
ON CONFLICT DO NOTHING;

-- 5. Map Users to Country Regions
INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'national_wh_admin_fr@plus33.com' AND r.code = 'FR_COUNTRY'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'national_wh_admin_ae@plus33.com' AND r.code = 'AE_COUNTRY'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'national_wh_admin_in@plus33.com' AND r.code = 'IN_COUNTRY'
ON CONFLICT DO NOTHING;

-- 6. Map Users to Warehouses
INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'national_wh_admin_fr@plus33.com' AND w.code = 'WH_EU_01'
ON CONFLICT DO NOTHING;

INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'national_wh_admin_ae@plus33.com' AND w.code = 'WH_DXB_01'
ON CONFLICT DO NOTHING;

INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
SELECT u.id, w.id, NOW() FROM users u CROSS JOIN warehouses w
WHERE u.email = 'national_wh_admin_in@plus33.com' AND w.code = 'WH_IND_01'
ON CONFLICT DO NOTHING;

-- 7. Insert Employee Records
INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NWA-01', u.id, 3, 'National Warehouse', 'Admin France', 'national_wh_admin_fr@plus33.com', '', 'National Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'national_wh_admin_fr@plus33.com'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NWA-02', u.id, 3, 'National Warehouse', 'Admin UAE', 'national_wh_admin_ae@plus33.com', '', 'National Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'national_wh_admin_ae@plus33.com'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NWA-03', u.id, 3, 'National Warehouse', 'Admin India', 'national_wh_admin_in@plus33.com', '', 'National Warehouse Admin', 'Logistics', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'national_wh_admin_in@plus33.com'
ON CONFLICT DO NOTHING;
