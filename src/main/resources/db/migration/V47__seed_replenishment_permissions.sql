-- ============================================================
-- V47__seed_replenishment_permissions.sql
-- PLUS33 ERP — Inventory Replenishment Permissions Seed Data
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('REPLENISHMENT_RULE_CREATE',             'Create Replenishment Rules',        'Create new inventory replenishment rules'),
('REPLENISHMENT_RULE_VIEW',               'View Replenishment Rules',          'View replenishment rules and configurations'),
('REPLENISHMENT_RULE_UPDATE',             'Update Replenishment Rules',        'Update details on replenishment rules'),
('REPLENISHMENT_RULE_DELETE',             'Delete Replenishment Rules',        'Deactivate replenishment rules'),
('REPLENISHMENT_SUGGESTION_VIEW',         'View Replenishment Suggestions',    'View generated replenishment suggestions'),
('REPLENISHMENT_SUGGESTION_ACKNOWLEDGE',  'Acknowledge Replenishment Suggestion', 'Acknowledge open replenishment suggestions'),
('REPLENISHMENT_SUGGESTION_ORDER',        'Order Replenishment Suggestion',      'Create Purchase Requests or Transfers from suggestions'),
('REPLENISHMENT_SUGGESTION_CANCEL',       'Cancel Replenishment Suggestion',     'Cancel replenishment suggestions')
ON CONFLICT (code) DO NOTHING;

-- Assign permissions to ULTIMATE_ADMIN, REGIONAL_MANAGER, WAREHOUSE_MANAGER, and PROCUREMENT_MANAGER roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER', 'PROCUREMENT_MANAGER')
  AND p.code IN (
    'REPLENISHMENT_RULE_CREATE', 'REPLENISHMENT_RULE_VIEW', 'REPLENISHMENT_RULE_UPDATE', 'REPLENISHMENT_RULE_DELETE',
    'REPLENISHMENT_SUGGESTION_VIEW', 'REPLENISHMENT_SUGGESTION_ACKNOWLEDGE', 'REPLENISHMENT_SUGGESTION_ORDER', 'REPLENISHMENT_SUGGESTION_CANCEL'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
