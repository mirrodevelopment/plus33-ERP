-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 57
-- File              : V57__seed_pick_list_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed pick list permissions
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
-- V57__seed_pick_list_permissions.sql
-- PLUS33 ERP — Pick List Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('PICK_LIST_CREATE',  'Create Pick Lists',  'Allows creating new pick lists'),
('PICK_LIST_VIEW',    'View Pick Lists',    'Allows viewing picking information'),
('PICK_LIST_RELEASE', 'Release Pick Lists', 'Allows releasing pick lists and allocating stock'),
('PICK_LIST_PICK',    'Pick Items',         'Allows updating picked quantities'),
('PICK_LIST_PACK',    'Pack Items',         'Allows marking picked lists as packed'),
('PICK_LIST_SHIP',    'Ship Orders',        'Allows shipping packed lists and deducting stock'),
('PICK_LIST_CANCEL',  'Cancel Pick Lists',  'Allows cancelling pick lists and releasing allocations')
ON CONFLICT (code) DO NOTHING;

-- Map picking permissions to ULTIMATE_ADMIN and WAREHOUSE_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'WAREHOUSE_MANAGER', 'REGIONAL_MANAGER')
  AND p.code IN (
    'PICK_LIST_CREATE', 'PICK_LIST_VIEW', 'PICK_LIST_RELEASE', 
    'PICK_LIST_PICK', 'PICK_LIST_PACK', 'PICK_LIST_SHIP', 'PICK_LIST_CANCEL'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
