-- V399__create_dashboard_indexes.sql
-- Create database indexes to optimize isolated regional/national dashboard query filters

CREATE INDEX IF NOT EXISTS idx_warehouses_region_id ON warehouses(region_id);
CREATE INDEX IF NOT EXISTS idx_stores_region_id ON stores(region_id);
CREATE INDEX IF NOT EXISTS idx_employees_user_id ON employees(user_id);
CREATE INDEX IF NOT EXISTS idx_location_stock_location_id ON location_stock(location_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_locations_zone_id ON warehouse_locations(zone_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_zones_warehouse_id ON warehouse_zones(warehouse_id);
