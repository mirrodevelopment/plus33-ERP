-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 6
-- File              : V6__seed_role_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed role permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : role_permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V6__seed_role_permissions.sql
-- PLUS33 ERP — Mapping all permissions to ULTIMATE_ADMIN role
-- ============================================================

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN';
