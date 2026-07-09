-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 5
-- File              : V5__seed_ultimate_admin_user.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed ultimate admin user
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : user_roles, users
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V5__seed_ultimate_admin_user.sql
-- PLUS33 ERP — First system administrator account
-- ============================================================

-- Create the first system administrator account
INSERT INTO users (
    email,
    password,
    first_name,
    last_name,
    active
)
VALUES (
    'admin@plus33.com',
    '$2a$10$6pRRvx2jqvZqcfWjVPGjgO9DzPOh4NOUUozM/37iVk4O.2BZK8TQa',
    'System',
    'Administrator',
    TRUE
);

-- Assign the ULTIMATE_ADMIN role to the administrator
INSERT INTO user_roles (user_id, role_id)
SELECT
    u.id,
    r.id
FROM users u
CROSS JOIN roles r
WHERE u.email = 'admin@plus33.com'
  AND r.code = 'ULTIMATE_ADMIN';
