-- ============================================================
-- V382__seed_plus33_coffee_purchasing.sql
-- PLUS33 ERP — Purchase Requests, Purchase Orders, Supplier Invoices
-- ============================================================

DO $$
DECLARE
    v_company_id BIGINT;
    v_now TIMESTAMP := NOW();
    v_admin_id BIGINT;
    v_sup_id BIGINT;
    v_wh_id BIGINT;
    v_req_id BIGINT;
    v_po_id BIGINT;
    v_si_id BIGINT;
    v_prod_id BIGINT;
    v_qty NUMERIC(12,2);
    v_price NUMERIC(12,2);
    v_tax_rate NUMERIC(12,2) := 20.00; -- Standard French VAT rate is 20%
    v_tax_amt NUMERIC(12,2);
    v_net_amt NUMERIC(12,2);
    v_total_amt NUMERIC(12,2);
    v_po_item_id BIGINT;
    v_req_item_id BIGINT;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';
    
    -- Select user for requested_by / ordered_by
    SELECT id INTO v_admin_id FROM users WHERE email = 'jean-pierre.moreau@plus33coffee.fr';
    IF v_admin_id IS NULL THEN
        SELECT id INTO v_admin_id FROM users LIMIT 1;
    END IF;

    SELECT id INTO v_wh_id FROM warehouses WHERE code = 'WH_PARIS';

    -- ── 1. Seeding ~50 Purchase Requests ──────────────────────
    FOR i IN 1..50 LOOP
        -- Select a supplier and a location
        SELECT id INTO v_sup_id FROM suppliers WHERE company_id = v_company_id OFFSET (i % 30) LIMIT 1;
        
        INSERT INTO purchase_requests (
            request_number, requested_by, warehouse_id, store_id, status, requested_at,
            company_id, supplier_id, submitted_by, approved_by, request_date, required_date,
            notes, submitted_at, approved_at
        ) VALUES (
            'PRQ-' || LPAD(i::TEXT, 5, '0'),
            v_admin_id,
            v_wh_id,
            NULL,
            CASE (i % 4)
                WHEN 0 THEN 'DRAFT'
                WHEN 1 THEN 'SUBMITTED'
                WHEN 2 THEN 'APPROVED'
                ELSE 'REJECTED'
            END,
            v_now - (i * INTERVAL '3 days'),
            v_company_id,
            v_sup_id,
            v_admin_id,
            CASE WHEN i % 4 = 2 THEN v_admin_id ELSE NULL END,
            (v_now - (i * INTERVAL '3 days'))::DATE,
            (v_now - (i * INTERVAL '3 days') + INTERVAL '7 days')::DATE,
            'Need supplies for regional inventory replenishment sequence ' || i,
            v_now - (i * INTERVAL '3 days') + INTERVAL '1 hour',
            CASE WHEN i % 4 = 2 THEN v_now - (i * INTERVAL '3 days') + INTERVAL '3 hours' ELSE NULL END
        ) RETURNING id INTO v_req_id;

        -- 3 items per request
        FOR j IN 1..3 LOOP
            -- Select a raw material product
            SELECT id INTO v_prod_id FROM products WHERE product_type = 'RAW_MATERIAL' OFFSET ((i * 3 + j) % 50) LIMIT 1;
            
            INSERT INTO purchase_request_items (
                purchase_request_id, product_id, requested_quantity, approved_quantity, unit_of_measure, remarks
            ) VALUES (
                v_req_id,
                v_prod_id,
                100.00 + (j * 10),
                CASE WHEN i % 4 = 2 THEN 100.00 + (j * 10) ELSE 0.00 END,
                'PCS',
                'Replenishment requested item ' || j
            );
        END LOOP;
    END LOOP;

    -- ── 2. Seeding 150 Purchase Orders & Invoices ─────────────
    FOR i IN 1..150 LOOP
        SELECT id INTO v_sup_id FROM suppliers WHERE company_id = v_company_id OFFSET (i % 30) LIMIT 1;
        
        -- Optionally link to an approved purchase request (ensure uniqueness, only link up to 12 approved requests)
        IF i <= 12 THEN
            SELECT id INTO v_req_id FROM purchase_requests WHERE company_id = v_company_id AND status = 'APPROVED' OFFSET (i - 1) LIMIT 1;
        ELSE
            v_req_id := NULL;
        END IF;

        INSERT INTO purchase_orders (
            order_number, supplier_id, purchase_request_id, ordered_by, expected_delivery_date,
            status, created_at, company_id, notes, issued_by, issued_at
        ) VALUES (
            'PO-' || LPAD(i::TEXT, 5, '0'),
            v_sup_id,
            v_req_id,
            v_admin_id,
            (v_now - (i * INTERVAL '1 day') + INTERVAL '5 days')::DATE,
            CASE (i % 5)
                WHEN 0 THEN 'DRAFT'
                WHEN 1 THEN 'ISSUED'
                WHEN 2 THEN 'ISSUED'
                WHEN 3 THEN 'RECEIVED'
                ELSE 'CANCELLED'
            END,
            v_now - (i * INTERVAL '2 days'),
            v_company_id,
            'Purchase order for coffee shop operations - Batch ' || i,
            v_admin_id,
            v_now - (i * INTERVAL '2 days') + INTERVAL '2 hours'
        ) RETURNING id INTO v_po_id;

        -- 3 items per PO
        v_total_amt := 0;
        FOR j IN 1..3 LOOP
            SELECT id INTO v_prod_id FROM products WHERE product_type = 'RAW_MATERIAL' OFFSET ((i * 3 + j) % 50) LIMIT 1;
            v_qty := 50.00 + (j * 10);
            v_price := 5.00 + (j * 1.50);
            v_net_amt := v_qty * v_price;
            v_total_amt := v_total_amt + v_net_amt + (v_net_amt * (v_tax_rate / 100.00));

            INSERT INTO purchase_order_items (
                purchase_order_id, product_id, ordered_quantity, unit_price, received_quantity,
                remarks, remaining_quantity, version, invoiced_quantity
            ) VALUES (
                v_po_id,
                v_prod_id,
                v_qty,
                v_price,
                CASE WHEN i % 5 = 3 THEN v_qty ELSE 0.00 END,
                'Ordered item ' || j,
                CASE WHEN i % 5 = 3 THEN 0.00 ELSE v_qty END,
                1,
                CASE WHEN i % 5 = 3 THEN v_qty ELSE 0.00 END
            );
        END LOOP;

        -- Seeding matched Supplier Invoices for RECEIVED orders
        IF i % 5 = 3 THEN
            INSERT INTO supplier_invoices (
                company_id, supplier_id, purchase_order_id, invoice_number, invoice_date, due_date,
                subtotal_amount, tax_amount, discount_amount, total_amount, status, currency_code, created_at, updated_at
            ) VALUES (
                v_company_id,
                v_sup_id,
                v_po_id,
                'INV-SUP-' || LPAD(i::TEXT, 5, '0'),
                (v_now - (i * INTERVAL '1 day'))::DATE,
                (v_now - (i * INTERVAL '1 day') + INTERVAL '30 days')::DATE,
                v_total_amt / 1.20,
                v_total_amt - (v_total_amt / 1.20),
                0.00,
                v_total_amt,
                CASE WHEN i % 2 = 0 THEN 'PAID' ELSE 'APPROVED' END,
                'EUR',
                v_now - (i * INTERVAL '1 day'),
                v_now
            ) RETURNING id INTO v_si_id;

            -- Supplier invoice items
            FOR j IN 1..3 LOOP
                SELECT id INTO v_prod_id FROM products WHERE product_type = 'RAW_MATERIAL' OFFSET ((i * 3 + j) % 50) LIMIT 1;
                SELECT id INTO v_po_item_id FROM purchase_order_items WHERE purchase_order_id = v_po_id OFFSET (j - 1) LIMIT 1;
                v_qty := 50.00 + (j * 10);
                v_price := 5.00 + (j * 1.50);
                v_net_amt := v_qty * v_price;
                v_tax_amt := v_net_amt * (v_tax_rate / 100.00);

                INSERT INTO supplier_invoice_items (
                    supplier_invoice_id, purchase_order_item_id, goods_receipt_item_id, product_id,
                    quantity, unit_price, net_amount, tax_rate, tax_amount, discount_amount, total_amount
                ) VALUES (
                    v_si_id,
                    v_po_item_id,
                    NULL,
                    v_prod_id,
                    v_qty,
                    v_price,
                    v_net_amt,
                    v_tax_rate,
                    v_tax_amt,
                    0.00,
                    v_net_amt + v_tax_amt
                );
            END LOOP;
        END IF;
    END LOOP;

END $$;
