-- Seed Users for missing standard National Admins
-- Password hash for 'pass123' is '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC'

-- 1. France National Admin (regional_fr@plus33.com)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_fr@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'France National', 'Admin', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_fr@plus33.com' AND r.code = 'NATIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_fr@plus33.com' AND r.code = 'FR_COUNTRY'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NA-10', u.id, 3, 'France National', 'Admin', 'regional_fr@plus33.com', 'National Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_fr@plus33.com'
ON CONFLICT DO NOTHING;


-- 2. India National Admin (regional_ind@plus33.com)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_ind@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'India National', 'Admin', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_ind@plus33.com' AND r.code = 'NATIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_ind@plus33.com' AND r.code = 'IN_COUNTRY'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NA-11', u.id, 3, 'India National', 'Admin', 'regional_ind@plus33.com', 'National Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_ind@plus33.com'
ON CONFLICT DO NOTHING;


-- 3. UAE National Admin (regional_uae@plus33.com)
INSERT INTO users (email, password, first_name, last_name, active)
VALUES ('regional_uae@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'UAE National', 'Admin', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'regional_uae@plus33.com' AND r.code = 'NATIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r
WHERE u.email = 'regional_uae@plus33.com' AND r.code = 'AE_COUNTRY'
ON CONFLICT DO NOTHING;

INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-NA-12', u.id, 3, 'UAE National', 'Admin', 'regional_uae@plus33.com', 'National Admin', 'Administration', 'PERMANENT', '2024-01-15', 'ACTIVE', TRUE
FROM users u WHERE u.email = 'regional_uae@plus33.com'
ON CONFLICT DO NOTHING;
