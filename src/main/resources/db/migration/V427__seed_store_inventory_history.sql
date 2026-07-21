-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 427
-- File              : V427__seed_store_inventory_history.sql
-- Operation Type    : Data Seeding
-- Purpose           : Seed dim_date, dim_location, dim_product, and fact_inventory
--                     for Store 15 (Ile-de-France Store 01) to support monthly trends.
-- ============================================================================

-- 1. Pre-populate dim_date for 2026 if not already present
INSERT INTO dim_date (date_key, full_date, day_of_week, day_name, day_of_month, day_of_year,
                      week_of_year, month_number, month_name, quarter_number, quarter_name,
                      year_number, is_weekend, is_holiday, is_working_day)
SELECT
    TO_CHAR(d, 'YYYYMMDD')::INTEGER,
    d::date,
    EXTRACT(DOW FROM d)::INTEGER,
    TO_CHAR(d, 'Day'),
    EXTRACT(DAY FROM d)::INTEGER,
    EXTRACT(DOY FROM d)::INTEGER,
    EXTRACT(WEEK FROM d)::INTEGER,
    EXTRACT(MONTH FROM d)::INTEGER,
    TO_CHAR(d, 'Month'),
    EXTRACT(QUARTER FROM d)::INTEGER,
    'Q' || EXTRACT(QUARTER FROM d)::INTEGER,
    EXTRACT(YEAR FROM d)::INTEGER,
    EXTRACT(DOW FROM d) IN (0, 6),
    FALSE,
    EXTRACT(DOW FROM d) NOT IN (0, 6)
FROM GENERATE_SERIES('2026-01-01'::timestamp, '2026-12-31'::timestamp, '1 day'::interval) d
ON CONFLICT (date_key) DO NOTHING;

-- 2. Seed dim_location for Store 15 (ST_FR_REG_1_01)
INSERT INTO dim_location (id, company_id, location_id, location_code, location_name, location_type, city, country, region, is_active, is_current, effective_from, effective_to)
VALUES (15, 1, 15, 'ST_FR_REG_1_01', 'Ile-de-France Store 01', 'STORE', 'Paris', 'France', 'Ile-de-France', true, true, '2026-01-01', '2029-12-31')
ON CONFLICT (id) DO NOTHING;

-- 3. Seed dim_product from product catalog
INSERT INTO dim_product (id, company_id, product_id, product_code, product_name, product_category, uom, is_active, is_current, effective_from, effective_to)
SELECT p.id, 1, p.id, p.sku, p.name, pc.name, uom.code, true, true, '2026-01-01', '2029-12-31'
FROM products p
JOIN product_categories pc ON pc.id = p.category_id
JOIN units_of_measure uom ON uom.id = p.unit_id
ON CONFLICT (id) DO NOTHING;

-- 4. Seed fact_inventory with 7 months of stock movements for Store 15
INSERT INTO fact_inventory (company_id, date_key, product_dim_id, location_dim_id, movement_type, quantity_in, quantity_out, closing_stock, unit_cost, total_value)
SELECT 1, 20260000 + m.month * 100 + 15, p.id, 15, 'RECEIPT', 
       100.00 + (m.month * 10) + (p.id % 5) * 20, 
       50.00 + (m.month * 5), 
       500.00 + (m.month * 5) + (p.id % 5) * 20, 
       12.50, 
       (500.00 + (m.month * 5) + (p.id % 5) * 20) * 12.50
FROM products p
CROSS JOIN (SELECT generate_series(1, 7) AS month) m
LIMIT 500
ON CONFLICT DO NOTHING;
