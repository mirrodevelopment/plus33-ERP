-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 405
-- File              : V405__create_store_settings.sql
-- Operation Type    : Create Table / Seed Data
-- Purpose           : Create store_settings table and seed initial store settings
-- ============================================================================

CREATE TABLE store_settings (
    id BIGSERIAL PRIMARY KEY,
    store_id BIGINT UNIQUE NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    operating_hours VARCHAR(100) NOT NULL DEFAULT '08:00 - 22:00',
    wifi_ssid VARCHAR(100) NOT NULL DEFAULT 'PLUS33-Guest',
    wifi_password VARCHAR(100) NOT NULL DEFAULT 'CoffeeBreak',
    low_stock_threshold INTEGER NOT NULL DEFAULT 50,
    sales_target DECIMAL(12,2) NOT NULL DEFAULT 10000.00,
    receipt_footer TEXT NOT NULL DEFAULT 'Thank you for visiting PLUS33 Coffee!',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed settings for all existing stores
INSERT INTO store_settings (store_id, operating_hours, wifi_ssid, wifi_password, low_stock_threshold, sales_target, receipt_footer)
SELECT id, '08:00 - 22:00', 'PLUS33-Guest', 'CoffeeBreak', 50, 10000.00, 'Thank you for visiting PLUS33 Coffee!'
FROM stores
ON CONFLICT (store_id) DO NOTHING;
