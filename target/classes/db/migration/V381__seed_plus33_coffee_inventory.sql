-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 381
-- File              : V381__seed_plus33_coffee_inventory.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed plus33 coffee inventory
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : inventory_stock, stock_adjustments, stock_count_items, stock_counts, stock_movements, stock_transfer_items, stock_transfers, tmp_locations
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V381__seed_plus33_coffee_inventory.sql
-- PLUS33 ERP — Inventory Stock, Movements, Transfers, Adjustments, Counts
-- ============================================================

DO $$
DECLARE
    v_company_id BIGINT;
    v_now TIMESTAMP := NOW();
    rec_prod RECORD;
    rec_loc RECORD;
    v_transfer_id BIGINT;
    v_count_id BIGINT;
    v_admin_id BIGINT;
    v_wh_id BIGINT;
    v_st_id BIGINT;
    v_qty NUMERIC(12,2);
    v_prev_qty NUMERIC(12,2);
    v_adj_qty NUMERIC(12,2);
    v_counter INT := 0;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';
    
    -- Select admin user to associate as creator
    SELECT id INTO v_admin_id FROM users WHERE email = 'jean-pierre.moreau@plus33coffee.fr';
    IF v_admin_id IS NULL THEN
        SELECT id INTO v_admin_id FROM users LIMIT 1;
    END IF;

    -- ── 1. Inventory Stock for all 250 products across 12 locations ──
    -- Locations include 2 warehouses and 10 stores.
    -- To keep it realistic, raw materials (e.g. coffee beans, packaging, supplies) are stocked heavily in warehouses, 
    -- while finished goods (e.g. hot coffee, desserts, bakery, snacks, merchandise) are stocked in stores.
    
    CREATE TEMP TABLE tmp_locations (
        seq INT,
        is_wh BOOLEAN,
        code VARCHAR(50)
    ) ON COMMIT DROP;

    INSERT INTO tmp_locations VALUES
    (1,  TRUE,  'WH_PARIS'),
    (2,  TRUE,  'WH_LYON'),
    (3,  FALSE, 'ST_PARIS_01'),
    (4,  FALSE, 'ST_PARIS_02'),
    (5,  FALSE, 'ST_MARS_01'),
    (6,  FALSE, 'ST_NICE_01'),
    (7,  FALSE, 'ST_LYON_01'),
    (8,  FALSE, 'ST_GREN_01'),
    (9,  FALSE, 'ST_TLSE_01'),
    (10, FALSE, 'ST_MTPL_01'),
    (11, FALSE, 'ST_BORD_01'),
    (12, FALSE, 'ST_LARO_01');

    FOR rec_prod IN SELECT p.id, p.sku, p.product_type FROM products p JOIN product_categories pc ON p.category_id = pc.id WHERE pc.code LIKE 'CAT_%' LOOP
        FOR rec_loc IN SELECT * FROM tmp_locations LOOP
            -- Determine quantity to seed
            IF rec_loc.is_wh THEN
                SELECT id INTO v_wh_id FROM warehouses WHERE code = rec_loc.code;
                v_st_id := NULL;
                
                IF rec_prod.product_type = 'RAW_MATERIAL' THEN
                    v_qty := 1000.00 + (rec_prod.id % 10) * 100.00;
                ELSE
                    v_qty := 100.00 + (rec_prod.id % 5) * 20.00;
                END IF;
            ELSE
                SELECT id INTO v_st_id FROM stores WHERE code = rec_loc.code;
                v_wh_id := NULL;
                
                IF rec_prod.product_type = 'RAW_MATERIAL' THEN
                    v_qty := 50.00 + (rec_prod.id % 10) * 5.00;
                ELSE
                    v_qty := 200.00 + (rec_prod.id % 5) * 10.00;
                END IF;
            END IF;

            INSERT INTO inventory_stock (product_id, warehouse_id, store_id, quantity, reserved_quantity, version)
            VALUES (rec_prod.id, v_wh_id, v_st_id, v_qty, 0.00, 1);
        END LOOP;
    END LOOP;

    -- ── 2. Stock Movements (~2500 movements) ──────────────────
    -- Let's insert historical movements (purchasing, sales, adjustments, transfers)
    -- We can generate 10 movements per product to reach ~2500 movements.
    FOR rec_prod IN SELECT id, sku FROM products LIMIT 250 LOOP
        FOR i IN 1..10 LOOP
            v_counter := v_counter + 1;
            
            -- Alternate locations
            IF i % 2 = 0 THEN
                SELECT id INTO v_wh_id FROM warehouses WHERE code = CASE WHEN i % 4 = 0 THEN 'WH_PARIS' ELSE 'WH_LYON' END;
                v_st_id := NULL;
            ELSE
                SELECT id INTO v_st_id FROM stores WHERE code = 'ST_PARIS_0' || (1 + (i % 2))::TEXT;
                v_wh_id := NULL;
            END IF;

            INSERT INTO stock_movements (
                product_id, warehouse_id, store_id, movement_type, quantity, reference_no,
                reference_type, reference_id, reference_number, movement_at, created_by
            ) VALUES (
                rec_prod.id,
                v_wh_id,
                v_st_id,
                CASE (i % 4)
                    WHEN 0 THEN 'INBOUND'
                    WHEN 1 THEN 'OUTBOUND'
                    WHEN 2 THEN 'TRANSFER'
                    ELSE 'ADJUSTMENT'
                END,
                10.00 + (i * 2.5),
                'REF-MOV-' || LPAD(v_counter::TEXT, 6, '0'),
                CASE (i % 4)
                    WHEN 0 THEN 'GOODS_RECEIPT'
                    WHEN 1 THEN 'SALES_ORDER'
                    WHEN 2 THEN 'INVENTORY_TRANSFER'
                    ELSE 'INVENTORY_ADJUSTMENT'
                END,
                v_counter,
                'DOC-' || LPAD(v_counter::TEXT, 6, '0'),
                v_now - (i * INTERVAL '2 days'),
                v_admin_id
            );
        END LOOP;
    END LOOP;

    -- ── 3. Stock Transfers (~50 transfers, 150 items) ────────
    FOR i IN 1..50 LOOP
        SELECT id INTO v_wh_id FROM warehouses WHERE code = 'WH_PARIS';
        SELECT id INTO v_st_id FROM stores WHERE code = 'ST_PARIS_0' || (1 + (i % 2))::TEXT;

        INSERT INTO stock_transfers (
            transfer_number, source_warehouse_id, source_store_id, destination_warehouse_id, destination_store_id,
            requested_by, status, requested_at, approved_by, approved_at, completed_at
        ) VALUES (
            'TRF-' || LPAD(i::TEXT, 5, '0'),
            v_wh_id,
            NULL,
            NULL,
            v_st_id,
            v_admin_id,
            'COMPLETED',
            v_now - (i * INTERVAL '1 day'),
            v_admin_id,
            v_now - (i * INTERVAL '1 day') + INTERVAL '1 hour',
            v_now - (i * INTERVAL '1 day') + INTERVAL '3 hours'
        ) RETURNING id INTO v_transfer_id;

        -- 3 items per transfer
        FOR j IN 1..3 LOOP
            INSERT INTO stock_transfer_items (stock_transfer_id, product_id, requested_quantity, transferred_quantity)
            SELECT v_transfer_id, id, 50.00, 50.00 FROM products OFFSET ((i * 3 + j) % 250) LIMIT 1;
        END LOOP;
    END LOOP;

    -- ── 4. Stock Adjustments (~50 adjustments) ────────────────
    FOR i IN 1..50 LOOP
        SELECT id INTO v_wh_id FROM warehouses WHERE code = 'WH_PARIS';
        SELECT id INTO v_st_id FROM stores WHERE code = 'ST_PARIS_01';

        INSERT INTO stock_adjustments (
            adjustment_number, warehouse_id, store_id, product_id, previous_quantity, adjusted_quantity, reason, approved_by, created_at
        ) VALUES (
            'ADJ-' || LPAD(i::TEXT, 5, '0'),
            CASE WHEN i % 2 = 0 THEN v_wh_id ELSE NULL END,
            CASE WHEN i % 2 = 1 THEN v_st_id ELSE NULL END,
            (SELECT id FROM products OFFSET (i % 250) LIMIT 1),
            100.00,
            CASE WHEN i % 3 = 0 THEN -5.00 WHEN i % 3 = 1 THEN 10.00 ELSE -2.00 END,
            CASE WHEN i % 3 = 0 THEN 'Damaged' WHEN i % 3 = 1 THEN 'Found during audit' ELSE 'Expired product' END,
            v_admin_id,
            v_now - (i * INTERVAL '18 hours')
        );
    END LOOP;

    -- ── 5. Stock Counts (~20 counts, ~100 items) ──────────────
    FOR i IN 1..20 LOOP
        SELECT id INTO v_wh_id FROM warehouses WHERE code = 'WH_PARIS';

        INSERT INTO stock_counts (
            count_number, company_id, warehouse_id, store_id, status, count_type, blind_count,
            assigned_to, approval_required, approval_threshold_percentage, remarks, client_reference_id
        ) VALUES (
            'CNT-' || LPAD(i::TEXT, 5, '0'),
            v_company_id,
            v_wh_id,
            NULL,
            'APPROVED',
            'CYCLE',
            FALSE,
            v_admin_id,
            TRUE,
            5.00,
            'Cycle count for Coffee category',
            gen_random_uuid()
        ) RETURNING id INTO v_count_id;

        -- 5 items per count
        FOR j IN 1..5 LOOP
            INSERT INTO stock_count_items (
                stock_count_id, product_id, system_quantity, reserved_quantity, available_quantity, counted_quantity, variance, version
            ) VALUES (
                v_count_id,
                (SELECT id FROM products OFFSET ((i * 5 + j) % 250) LIMIT 1),
                100.00,
                0.00,
                100.00,
                CASE WHEN (i + j) % 7 = 0 THEN 98.00 ELSE 100.00 END,
                CASE WHEN (i + j) % 7 = 0 THEN -2.00 ELSE 0.00 END,
                1
            );
        END LOOP;
    END LOOP;

END $$;
