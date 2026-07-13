-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 99
-- File              : V99__create_wms_tier1_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : create wms tier1 permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V99: Seed Tier-1 WMS/TMS Enterprise Permissions
INSERT INTO permissions (code, name, description)
VALUES
  ('WMS_3PL_MANAGE', '3PL Multi-Tenancy Management', 'Allows managing multi-client inventory ownership and 3PL billing'),
  ('WMS_LABOR_VIEW', 'Warehouse Labor Tracking', 'Allows viewing labor productivity, travel time, and task costs'),
  ('WMS_SLOTTING_MANAGE', 'Slotting Optimization Management', 'Allows configuring product affinity and re-slotting recommendations'),
  ('WMS_ROBOTICS_MANAGE', 'Autonomous Robotics Management', 'Allows managing AGV/AMR equipment and dispatching robot tasks'),
  ('WMS_RULES_MANAGE', 'Warehouse Rules Engine', 'Allows configuring dynamic hazmat, temp, and weight storage rules')
ON CONFLICT (code) DO NOTHING;

