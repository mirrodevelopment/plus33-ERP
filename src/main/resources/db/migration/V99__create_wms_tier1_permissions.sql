-- V99: Seed Tier-1 WMS/TMS Enterprise Permissions
INSERT INTO permissions (code, name, description)
VALUES
  ('WMS_3PL_MANAGE', '3PL Multi-Tenancy Management', 'Allows managing multi-client inventory ownership and 3PL billing'),
  ('WMS_LABOR_VIEW', 'Warehouse Labor Tracking', 'Allows viewing labor productivity, travel time, and task costs'),
  ('WMS_SLOTTING_MANAGE', 'Slotting Optimization Management', 'Allows configuring product affinity and re-slotting recommendations'),
  ('WMS_ROBOTICS_MANAGE', 'Autonomous Robotics Management', 'Allows managing AGV/AMR equipment and dispatching robot tasks'),
  ('WMS_RULES_MANAGE', 'Warehouse Rules Engine', 'Allows configuring dynamic hazmat, temp, and weight storage rules')
ON CONFLICT (code) DO NOTHING;

