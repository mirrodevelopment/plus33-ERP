-- ============================================================
-- V91__seed_wms_permissions.sql
-- PLUS33 ERP — Enterprise WMS/TMS Permissions Seed
-- ============================================================

INSERT INTO permissions (code, name, description) VALUES
-- Layout & Location Management
('WMS_LAYOUT_MANAGE',       'Manage Warehouse Layout',          'Allows creating and managing warehouse zones and locations'),
('WMS_LOCATION_MANAGE',     'Manage Bin Stock',                 'Allows viewing and adjusting bin-level stock positions'),
-- Reservation & Allocation
('WMS_RESERVATION_MANAGE',  'Manage Inventory Reservations',    'Allows creating, releasing, and managing inventory reservations'),
-- Inbound Operations
('WMS_RECEIVING',           'Warehouse Receiving',              'Allows receiving ASNs, checking in trucks, and verifying quantities'),
('WMS_PUT_AWAY',            'Warehouse Put-Away',               'Allows executing directed put-away tasks'),
-- Outbound Operations
('WMS_PICKING',             'Warehouse Picking',                'Allows executing wave and directed picking tasks'),
('WMS_PACKING',             'Warehouse Packing',                'Allows executing packing and staging tasks'),
('WMS_SHIPPING',            'Warehouse Shipping',               'Allows dispatching shipments and recording proof-of-delivery'),
-- Wave & Task Management
('WMS_WAVE_MANAGE',         'Manage Picking Waves',             'Allows creating, optimizing, and releasing picking waves'),
('WMS_TASK_MANAGE',         'Manage Warehouse Tasks',           'Allows assigning, pausing, and cancelling warehouse tasks'),
-- Inventory Counts & Adjustments
('WMS_CYCLE_COUNT',         'Cycle Count',                      'Allows creating cycle count plans, counting bins, and approving variances'),
('WMS_ADJUSTMENT',          'Inventory Adjustment',             'Allows posting manual inventory adjustments at bin level'),
-- Cross-Dock & Replenishment
('WMS_CROSS_DOCK',          'Cross-Dock Operations',            'Allows creating and managing cross-dock orders'),
('WMS_REPLENISHMENT',       'Replenishment Management',         'Allows managing replenishment rules and executing replenishment tasks'),
-- Yard & Dock
('WMS_YARD_MANAGE',         'Yard & Dock Management',           'Allows managing yard locations, dock doors, truck check-ins, and appointment slots'),
-- TMS / Transportation
('TMS_CARRIER_MANAGE',      'Manage Carrier Registry',          'Allows creating and managing carrier profiles and API integrations'),
('TMS_BOOKING',             'Carrier Booking',                  'Allows booking freight with carriers and generating shipping labels'),
('TMS_TRACKING',            'Shipment Tracking',                'Allows tracking shipments and recording proof-of-delivery'),
('TMS_RATE_MANAGE',         'Freight Rate Management',          'Allows retrieving and managing carrier freight rate estimates'),
-- Analytics
('WMS_ANALYTICS',           'WMS Analytics Dashboard',          'Allows viewing warehouse performance KPIs and analytics dashboards'),
('WMS_REPORT_EXPORT',       'Export WMS Reports',               'Allows exporting WMS analytics and inventory reports'),
-- Administration
('WMS_ADMIN',               'WMS Administration',               'Full administrative access to all WMS/TMS configuration and master data')
ON CONFLICT (code) DO NOTHING;

-- ============================================================
-- Map ALL WMS/TMS permissions to ULTIMATE_ADMIN
-- ============================================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'WMS_LAYOUT_MANAGE', 'WMS_LOCATION_MANAGE', 'WMS_RESERVATION_MANAGE',
    'WMS_RECEIVING', 'WMS_PUT_AWAY',
    'WMS_PICKING', 'WMS_PACKING', 'WMS_SHIPPING',
    'WMS_WAVE_MANAGE', 'WMS_TASK_MANAGE',
    'WMS_CYCLE_COUNT', 'WMS_ADJUSTMENT',
    'WMS_CROSS_DOCK', 'WMS_REPLENISHMENT',
    'WMS_YARD_MANAGE',
    'TMS_CARRIER_MANAGE', 'TMS_BOOKING', 'TMS_TRACKING', 'TMS_RATE_MANAGE',
    'WMS_ANALYTICS', 'WMS_REPORT_EXPORT', 'WMS_ADMIN'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
