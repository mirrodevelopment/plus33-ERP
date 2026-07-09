-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 3
-- File              : V3__seed_roles_and_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed roles and permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions, roles
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V3__seed_roles_and_permissions.sql
-- PLUS33 ERP — Initial roles and permissions seed data
-- ============================================================

-- ── Roles ────────────────────────────────────────────────────
INSERT INTO roles (code, name, description)
VALUES
('ULTIMATE_ADMIN',  'Ultimate Admin',   'Full system access'),
('REGIONAL_ADMIN',  'Regional Admin',   'Regional operations management'),
('STORE_ADMIN',     'Store Admin',      'Store operations management'),
('SHIFT_SUPERVISOR','Shift Supervisor', 'Shift management'),
('SENIOR_EMPLOYEE', 'Senior Employee',  'Senior store employee'),
('JUNIOR_EMPLOYEE', 'Junior Employee',  'Junior store employee'),
('TRAINEE',         'Trainee',          'Training employee');

-- ── Permissions ───────────────────────────────────────────────
INSERT INTO permissions (code, name, description)
VALUES
('USER_VIEW',       'View Users',       'View user information'),
('USER_CREATE',     'Create Users',     'Create user accounts'),
('USER_EDIT',       'Edit Users',       'Modify user accounts'),

('STORE_VIEW',      'View Stores',      'View store information'),
('STORE_CREATE',    'Create Stores',    'Create stores'),
('STORE_EDIT',      'Edit Stores',      'Modify stores'),

('INVENTORY_VIEW',  'View Inventory',   'View inventory data'),
('INVENTORY_EDIT',  'Edit Inventory',   'Modify inventory'),

('WORKFORCE_VIEW',  'View Workforce',   'View employee data'),
('WORKFORCE_EDIT',  'Edit Workforce',   'Modify employee data');
