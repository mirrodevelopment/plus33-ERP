-- Flyway Migration V433: Merge store_settings into stores, drop store_settings, and create store_documents
-- Add columns to stores table
ALTER TABLE stores ADD COLUMN operating_hours VARCHAR(100) NOT NULL DEFAULT '08:00 - 22:00';
ALTER TABLE stores ADD COLUMN wifi_ssid VARCHAR(100) NOT NULL DEFAULT 'PLUS33-Guest';
ALTER TABLE stores ADD COLUMN wifi_password VARCHAR(100) NOT NULL DEFAULT 'CoffeeBreak';
ALTER TABLE stores ADD COLUMN low_stock_threshold INTEGER NOT NULL DEFAULT 50;
ALTER TABLE stores ADD COLUMN sales_target DECIMAL(12,2) NOT NULL DEFAULT 10000.00;
ALTER TABLE stores ADD COLUMN receipt_footer TEXT NOT NULL DEFAULT 'Thank you for visiting PLUS33 Coffee!';
ALTER TABLE stores ADD COLUMN admin_name VARCHAR(100) NOT NULL DEFAULT 'giri';
ALTER TABLE stores ADD COLUMN admin_number VARCHAR(50) NOT NULL DEFAULT 'EMP10245';
ALTER TABLE stores ADD COLUMN admin_mobile VARCHAR(30) NOT NULL DEFAULT '+919999999999';

-- Migrate settings data from store_settings to stores table
UPDATE stores s
SET operating_hours = ss.operating_hours,
    wifi_ssid = ss.wifi_ssid,
    wifi_password = ss.wifi_password,
    low_stock_threshold = ss.low_stock_threshold,
    sales_target = ss.sales_target,
    receipt_footer = ss.receipt_footer
FROM store_settings ss
WHERE s.id = ss.store_id;

-- Drop store_settings table
DROP TABLE IF EXISTS store_settings CASCADE;

-- Create store_documents table
CREATE TABLE store_documents (
    id BIGSERIAL PRIMARY KEY,
    store_id BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    document_type VARCHAR(100) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
