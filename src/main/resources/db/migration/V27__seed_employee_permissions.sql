-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 27
-- File              : V27__seed_employee_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed employee permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions, role_permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V27__seed_employee_permissions.sql
-- PLUS33 ERP — Employee Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('EMPLOYEE_CREATE',   'Create Employees',    'Create new employee profiles'),
('EMPLOYEE_VIEW',     'View Employees',      'View employee profiles and details'),
('EMPLOYEE_UPDATE',   'Update Employees',    'Modify employee details'),
('EMPLOYEE_DELETE',   'Delete Employees',    'Soft-delete employee profiles');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'EMPLOYEE_CREATE', 'EMPLOYEE_VIEW', 'EMPLOYEE_UPDATE', 'EMPLOYEE_DELETE'
  );
