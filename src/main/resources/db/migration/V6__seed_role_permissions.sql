-- ============================================================
-- V6__seed_role_permissions.sql
-- PLUS33 ERP — Mapping all permissions to ULTIMATE_ADMIN role
-- ============================================================

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN';
