-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 426
-- File              : V426__add_compact_cafe_stock.sql
-- Operation Type    : Data Seeding
-- Purpose           : Add stock records for Ile-de-France Store 01 (COMPACT CAFÉ)
-- ============================================================================

-- Seed inventory_stock for Store 15 (ST_FR_REG_1_01)
INSERT INTO inventory_stock (product_id, warehouse_id, store_id, quantity, reserved_quantity, version)
SELECT p.id, NULL, 15, 500.000000, 0.000000, 1
FROM products p
WHERE NOT EXISTS (
    SELECT 1 
    FROM inventory_stock 
    WHERE product_id = p.id AND store_id = 15
);
