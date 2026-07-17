-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 402
-- File              : V402__seed_stock_ledger.sql
-- Operation Type    : Data Seeding
-- Purpose           : Populate location_stock table for products to show in frontend
-- ============================================================================

-- Clear any existing stock to prevent duplicate constraint violation
TRUNCATE TABLE location_stock CASCADE;

-- Insert stock records for all products under company_id = 3 and location_id = 1
INSERT INTO location_stock (company_id, location_id, product_id, lot_number, quantity, reserved_quantity, expiry_date, manufacture_date, receipt_date, unit_cost, abc_class, version, created_at, updated_at)
SELECT 
    3, -- company_id
    1, -- location_id (LOC-STD-01)
    id, -- product_id
    'LOT-STD-001', -- lot_number
    500.000000, -- quantity
    0.000000, -- reserved_quantity
    '2027-12-31', -- expiry_date
    '2026-07-01', -- manufacture_date
    '2026-07-14', -- receipt_date
    1.500000, -- unit_cost
    'A', -- abc_class
    0, -- version
    NOW(),
    NOW()
FROM products;
