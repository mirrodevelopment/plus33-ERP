-- Alter regions table to support hierarchical structure
ALTER TABLE regions ADD COLUMN IF NOT EXISTS parent_id BIGINT;

-- Add self-referential foreign key constraint
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'fk_regions_parent' AND table_name = 'regions'
    ) THEN
        ALTER TABLE regions 
        ADD CONSTRAINT fk_regions_parent 
        FOREIGN KEY (parent_id) REFERENCES regions(id) ON DELETE SET NULL;
    END IF;
END $$;

-- Ensure REGIONAL_ADMIN role exists in database (since it was renamed to NATIONAL_ADMIN)
INSERT INTO roles (code, name, description) 
VALUES ('REGIONAL_ADMIN', 'Regional Admin', 'Regional operations management') 
ON CONFLICT (code) DO NOTHING;

-- Rename existing regions to reflect Country levels
UPDATE regions SET code = 'FR_COUNTRY', name = 'France' WHERE id = 7;
UPDATE regions SET code = 'AE_COUNTRY', name = 'UAE' WHERE id = 8;
UPDATE regions SET code = 'IN_COUNTRY', name = 'India' WHERE id = 9;

-- Insert sub-regions under countries (France ID=7, UAE ID=8, India ID=9)
INSERT INTO regions (code, name, description, company_id, parent_id) 
VALUES 
('FR_NORTH', 'North France', 'North France Regional Operations', 3, 7),
('IN_SOUTH', 'South India', 'South India Regional Operations', 3, 9),
('UAE_DUBAI', 'Dubai Region', 'Dubai Region Regional Operations', 3, 8)
ON CONFLICT (code) DO NOTHING;

-- Re-map existing stores and warehouses to their respective sub-regions
UPDATE stores SET region_id = (SELECT id FROM regions WHERE code = 'FR_NORTH') WHERE region_id = 7;
UPDATE warehouses SET region_id = (SELECT id FROM regions WHERE code = 'FR_NORTH') WHERE region_id = 7;

UPDATE stores SET region_id = (SELECT id FROM regions WHERE code = 'IN_SOUTH') WHERE region_id = 9;
UPDATE warehouses SET region_id = (SELECT id FROM regions WHERE code = 'IN_SOUTH') WHERE region_id = 9;

UPDATE stores SET region_id = (SELECT id FROM regions WHERE code = 'UAE_DUBAI') WHERE region_id = 8;
UPDATE warehouses SET region_id = (SELECT id FROM regions WHERE code = 'UAE_DUBAI') WHERE region_id = 8;

-- Map the existing national admin users (user IDs 211, 212, 213) to their respective country regions in user_regions
-- Note: They were already mapped to IDs 7, 8, 9, which are now FR_COUNTRY, AE_COUNTRY, IN_COUNTRY. So their mappings are already correct.

-- Insert New Regional Admin Users (for South India, North France, Dubai Region)
-- Password hash for 'pass123'
-- 1. North France Regional Admin
INSERT INTO users (email, password, first_name, last_name, active) 
VALUES ('regional_north_fr@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Jean', 'Dupont', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r 
WHERE u.email = 'regional_north_fr@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r 
WHERE u.email = 'regional_north_fr@plus33.com' AND r.code = 'FR_NORTH'
ON CONFLICT DO NOTHING;

INSERT INTO user_stores (user_id, store_id, assigned_at)
SELECT u.id, s.id, NOW() FROM users u CROSS JOIN stores s 
WHERE u.email = 'regional_north_fr@plus33.com' AND s.code = 'ST_EU_01'
ON CONFLICT DO NOTHING;

-- 2. South India Regional Admin
INSERT INTO users (email, password, first_name, last_name, active) 
VALUES ('regional_south_ind@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Vijay', 'Iyer', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r 
WHERE u.email = 'regional_south_ind@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r 
WHERE u.email = 'regional_south_ind@plus33.com' AND r.code = 'IN_SOUTH'
ON CONFLICT DO NOTHING;

INSERT INTO user_stores (user_id, store_id, assigned_at)
SELECT u.id, s.id, NOW() FROM users u CROSS JOIN stores s 
WHERE u.email = 'regional_south_ind@plus33.com' AND s.code = 'ST_IND_01'
ON CONFLICT DO NOTHING;

-- 3. Dubai Regional Admin
INSERT INTO users (email, password, first_name, last_name, active) 
VALUES ('regional_dubai@plus33.com', '$2a$10$CEvzjS0qbP22egOndJtyHezUqRuR.WQLjFqrHSY3jCJylTVaeQxvC', 'Zayd', 'Al-Maktoum', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r 
WHERE u.email = 'regional_dubai@plus33.com' AND r.code = 'REGIONAL_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_regions (user_id, region_id, assigned_at)
SELECT u.id, r.id, NOW() FROM users u CROSS JOIN regions r 
WHERE u.email = 'regional_dubai@plus33.com' AND r.code = 'UAE_DUBAI'
ON CONFLICT DO NOTHING;

INSERT INTO user_stores (user_id, store_id, assigned_at)
SELECT u.id, s.id, NOW() FROM users u CROSS JOIN stores s 
WHERE u.email = 'regional_dubai@plus33.com' AND s.code = 'ST_DXB_01'
ON CONFLICT DO NOTHING;

-- Assign WMS and Store Admin permissions to REGIONAL_ADMIN role
-- Grant all permissions to the REGIONAL_ADMIN role just like WAREHOUSE_MANAGER/STORE_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p 
WHERE r.code = 'REGIONAL_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;
