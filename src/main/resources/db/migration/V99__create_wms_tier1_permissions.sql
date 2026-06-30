-- V99: Seed Tier-1 WMS/TMS Enterprise Permissions
INSERT INTO permissions (code, name, module, description, created_at, updated_at)
VALUES
  ('WMS_3PL_MANAGE', '3PL Multi-Tenancy Management', 'WMS', 'Allows managing multi-client inventory ownership and 3PL billing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('WMS_LABOR_VIEW', 'Warehouse Labor Tracking', 'WMS', 'Allows viewing labor productivity, travel time, and task costs', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('WMS_SLOTTING_MANAGE', 'Slotting Optimization Management', 'WMS', 'Allows configuring product affinity and re-slotting recommendations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('WMS_ROBOTICS_MANAGE', 'Autonomous Robotics Management', 'WMS', 'Allows managing AGV/AMR equipment and dispatching robot tasks', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('WMS_RULES_MANAGE', 'Warehouse Rules Engine', 'WMS', 'Allows configuring dynamic hazmat, temp, and weight storage rules', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (code) DO NOTHING;
