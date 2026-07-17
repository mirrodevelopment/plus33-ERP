-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 403
-- File              : V403__add_supplier_to_location_stock.sql
-- Operation Type    : Schema Alteration
-- Purpose           : Add supplier_id column to location_stock for direct vendor tracking
-- ============================================================================

-- Add supplier_id column to location_stock table
ALTER TABLE location_stock ADD COLUMN supplier_id INT8;

-- Add foreign key constraint to suppliers table
ALTER TABLE location_stock 
    ADD CONSTRAINT fk_location_stock_supplier 
    FOREIGN KEY (supplier_id) 
    REFERENCES suppliers(id) 
    ON DELETE SET NULL;

-- Populate existing stock entries to use default supplier (id = 31)
UPDATE location_stock SET supplier_id = 31 WHERE supplier_id IS NULL;
