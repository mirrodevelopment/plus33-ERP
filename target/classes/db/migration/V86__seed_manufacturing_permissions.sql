-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 86
-- File              : V86__seed_manufacturing_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed manufacturing permissions
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
-- V86__seed_manufacturing_permissions.sql
-- PLUS33 ERP — Enterprise Manufacturing Permissions Seed
-- ============================================================

INSERT INTO permissions (code, name, description) VALUES
('MRP_RUN',                   'Run MRP',                      'Allows initiating Material Requirements Planning (MRP) runs'),
('MRP_APPROVE',               'Approve MRP Planned Orders',   'Allows firming and approving planned orders from MRP results'),
('BOM_MANAGE',                'Manage Bill of Materials',     'Allows creating, versioning, and approving BOM headers and lines'),
('ROUTING_MANAGE',            'Manage Routings',              'Allows creating, versioning, and approving manufacturing routings'),
('PRODUCTION_RELEASE',        'Release Production Orders',    'Allows releasing planned production orders for execution'),
('PRODUCTION_EXECUTE',        'Execute Production',           'Allows booking material issues, labor, and machine hours on production orders'),
('PRODUCTION_CLOSE',          'Close Production Orders',      'Allows completing and closing finalized production orders'),
('QUALITY_APPROVE',           'Approve Quality Inspections',  'Allows approving or rejecting quality inspection results'),
('MANUFACTURING_COSTING',     'Manufacturing Cost Management','Allows managing standard costs, triggering cost roll-ups, and finalizing production costs'),
('MANUFACTURING_REPORT_EXPORT','Export Manufacturing Reports','Allows exporting production, OEE, variance, and scheduling reports'),
('MANUFACTURING_ADMIN',       'Manufacturing Administration', 'Allows full administration of manufacturing master data, calendars, and configurations')
ON CONFLICT (code) DO NOTHING;

-- Map ALL permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'MRP_RUN', 'MRP_APPROVE', 'BOM_MANAGE', 'ROUTING_MANAGE',
    'PRODUCTION_RELEASE', 'PRODUCTION_EXECUTE', 'PRODUCTION_CLOSE',
    'QUALITY_APPROVE', 'MANUFACTURING_COSTING', 'MANUFACTURING_REPORT_EXPORT',
    'MANUFACTURING_ADMIN'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Map subset to FINANCE_MANAGER role (costing and reporting)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'FINANCE_MANAGER'
  AND p.code IN (
    'MANUFACTURING_COSTING', 'MANUFACTURING_REPORT_EXPORT', 'MRP_APPROVE'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
