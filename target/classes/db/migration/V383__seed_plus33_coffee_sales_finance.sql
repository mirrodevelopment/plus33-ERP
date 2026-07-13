-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 383
-- File              : V383__seed_plus33_coffee_sales_finance.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed plus33 coffee sales finance
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : budget_dimension_sets, budget_lines, budget_policies, budget_versions, budgets, chart_of_accounts, customer_invoice_items, customer_invoices, fiscal_years, journal_entries, journal_entry_lines, payments, sales_order_items, sales_orders
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V383__seed_plus33_coffee_sales_finance.sql
-- PLUS33 ERP — CoA, Budgets, Sales Orders, Customer Invoices, Payments, GL
-- ============================================================

DO $$
DECLARE
    v_company_id BIGINT;
    v_now TIMESTAMP := NOW();
    v_admin_id BIGINT;
    v_fy_id BIGINT;
    v_policy_id BIGINT;
    v_budget_id BIGINT;
    v_budget_version_id BIGINT;
    v_dim_set_id BIGINT;
    v_cust_id BIGINT;
    v_prod_id BIGINT;
    v_so_id BIGINT;
    v_ci_id BIGINT;
    v_je_id BIGINT;
    v_pay_id BIGINT;
    
    -- COA Accounts
    v_asset_id BIGINT;
    v_cash_id BIGINT;
    v_bank_id BIGINT;
    v_inv_id BIGINT;
    v_liab_id BIGINT;
    v_ap_id BIGINT;
    v_eq_id BIGINT;
    v_rev_id BIGINT;
    v_exp_id BIGINT;
    v_payroll_exp_id BIGINT;
    v_cogs_exp_id BIGINT;

    -- Iteration variables
    v_cust_name VARCHAR(150);
    v_cust_code VARCHAR(50);
    v_cust_type VARCHAR(20);
    v_tier VARCHAR(50);
    v_disc NUMERIC(5,2);
    v_addr TEXT;
    v_total NUMERIC(12,2);
    v_subtotal NUMERIC(12,2);
    v_tax NUMERIC(12,2);
    v_qty NUMERIC(12,2);
    v_price NUMERIC(12,2);
    v_tax_rate NUMERIC(12,2) := 20.00;
    v_order_num VARCHAR(50);
    v_invoice_num VARCHAR(50);
    v_payment_num VARCHAR(50);
    v_entry_num VARCHAR(50);
    
    v_counter INT := 0;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';
    
    SELECT id INTO v_admin_id FROM users WHERE email = 'jean-pierre.moreau@plus33coffee.fr';
    IF v_admin_id IS NULL THEN
        SELECT id INTO v_admin_id FROM users LIMIT 1;
    END IF;

    -- ── 1. Chart of Accounts ──────────────────────────────────
    -- Parent Account: Assets
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
    VALUES (v_company_id, '1000', 'Assets', 'ASSET', TRUE) RETURNING id INTO v_asset_id;

    -- Child Accounts under Assets
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '1100', 'Cash', 'ASSET', v_asset_id, TRUE) RETURNING id INTO v_cash_id;

    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '1200', 'Bank', 'ASSET', v_asset_id, TRUE) RETURNING id INTO v_bank_id;

    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '1300', 'Inventory', 'ASSET', v_asset_id, TRUE) RETURNING id INTO v_inv_id;

    -- Parent Account: Liabilities
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
    VALUES (v_company_id, '2000', 'Liabilities', 'LIABILITY', TRUE) RETURNING id INTO v_liab_id;

    -- Child Accounts under Liabilities
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '2100', 'Accounts Payable', 'LIABILITY', v_liab_id, TRUE) RETURNING id INTO v_ap_id;

    -- Parent Account: Equity
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
    VALUES (v_company_id, '3000', 'Equity', 'EQUITY', TRUE) RETURNING id INTO v_eq_id;

    -- Parent Account: Revenue
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
    VALUES (v_company_id, '4000', 'Revenue', 'REVENUE', TRUE) RETURNING id INTO v_rev_id;

    -- Parent Account: Expenses
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
    VALUES (v_company_id, '5000', 'Expenses', 'EXPENSE', TRUE) RETURNING id INTO v_exp_id;

    -- Child Accounts under Expenses
    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '5100', 'Payroll Expense', 'EXPENSE', v_exp_id, TRUE) RETURNING id INTO v_payroll_exp_id;

    INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
    VALUES (v_company_id, '5200', 'Cost of Goods Sold', 'EXPENSE', v_exp_id, TRUE) RETURNING id INTO v_cogs_exp_id;

    -- ── 2. Fiscal Year & Budget Policy ────────────────────────
    INSERT INTO fiscal_years (company_id, fiscal_year, start_date, end_date, status)
    VALUES (v_company_id, 2026, '2026-01-01', '2026-12-31', 'OPEN')
    RETURNING id INTO v_fy_id;

    INSERT INTO budget_policies (company_id, code, name, control_type, allow_negative, allow_transfers, allow_revisions, auto_reserve, auto_consume, approval_required, multi_currency_enabled, active)
    VALUES (v_company_id, 'STD_CONTROL', 'Standard Budget Policy', 'WARN', TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE)
    RETURNING id INTO v_policy_id;

    -- ── 3. Budget & Budget Lines ──────────────────────────────
    INSERT INTO budgets (
        company_id, fiscal_year_id, budget_policy_id, workflow_template_id, code, name,
        budget_type, period_type, scenario, status, version_number, is_forecast, is_frozen, is_active,
        rate_lock_type, budget_exchange_rate, created_by, created_at, updated_at
    ) VALUES (
        v_company_id, v_fy_id, v_policy_id, NULL, 'BUD_2026_OPEX', '2026 Operations Budget',
        'EXPENSE', 'MONTHLY', 'EXPECTED', 'APPROVED', 1, FALSE, FALSE, TRUE,
        'SPOT', 1.000000, 'jean-pierre.moreau@plus33coffee.fr', v_now, v_now
    ) RETURNING id INTO v_budget_id;

    INSERT INTO budget_versions (
        budget_id, version_code, description, status, created_by, created_at
    ) VALUES (
        v_budget_id, 'V1', 'Initial Budget Version', 'APPROVED', 'jean-pierre.moreau@plus33coffee.fr', v_now
    ) RETURNING id INTO v_budget_version_id;

    INSERT INTO budget_dimension_sets (
        company_id, department_id, cost_center_id, project_id, warehouse_id, asset_category_id, region_id, store_id
    ) VALUES (
        v_company_id, NULL, NULL, NULL, NULL, NULL, NULL, NULL
    ) RETURNING id INTO v_dim_set_id;

    -- budget lines for payroll and cogs
    FOR m IN 1..12 LOOP
        INSERT INTO budget_lines (
            budget_id, budget_version_id, account_id, dimension_set_id, period_start_date, period_end_date,
            allocated_amount, reserved_amount, consumed_amount, is_locked, distribution_method, forecast_confidence, predicted_spend
        ) VALUES (
            v_budget_id, v_budget_version_id, v_payroll_exp_id, v_dim_set_id, ('2026-' || LPAD(m::TEXT, 2, '0') || '-01')::DATE, ('2026-' || LPAD(m::TEXT, 2, '0') || '-01')::DATE + INTERVAL '1 month' - INTERVAL '1 day',
            50000.00, 0.00, 0.00, FALSE, 'EQUALLY', 95.00, 48000.00
        );
 
        INSERT INTO budget_lines (
            budget_id, budget_version_id, account_id, dimension_set_id, period_start_date, period_end_date,
            allocated_amount, reserved_amount, consumed_amount, is_locked, distribution_method, forecast_confidence, predicted_spend
        ) VALUES (
            v_budget_id, v_budget_version_id, v_cogs_exp_id, v_dim_set_id, ('2026-' || LPAD(m::TEXT, 2, '0') || '-01')::DATE, ('2026-' || LPAD(m::TEXT, 2, '0') || '-01')::DATE + INTERVAL '1 month' - INTERVAL '1 day',
            35000.00, 0.00, 0.00, FALSE, 'EQUALLY', 90.00, 32000.00
        );
    END LOOP;

    -- ── 4. Seeding 3000 Sales Orders ──────────────────────────
    -- Mapped across 100 customers and 250 products
    FOR i IN 1..3000 LOOP
        -- Select customer dynamically
        SELECT id, name, code, customer_type, pricing_tier, discount_rate, billing_address 
        INTO v_cust_id, v_cust_name, v_cust_code, v_cust_type, v_tier, v_disc, v_addr
        FROM customers WHERE company_id = v_company_id OFFSET (i % 100) LIMIT 1;
        
        v_order_num := 'SO-2026-' || LPAD(i::TEXT, 6, '0');

        INSERT INTO sales_orders (
            company_id, customer_id, order_number, client_reference_id, order_date, requested_delivery_date,
            currency_code, payment_terms_days, billing_address, shipping_address, status,
            customer_name, customer_code, customer_type, pricing_tier, discount_rate, tax_profile, ordered_by
        ) VALUES (
            v_company_id,
            v_cust_id,
            v_order_num,
            gen_random_uuid(),
            (v_now - (i * INTERVAL '2 hours'))::DATE,
            (v_now - (i * INTERVAL '2 hours') + INTERVAL '2 days')::DATE,
            'EUR',
            CASE WHEN v_cust_type = 'B2B' THEN 30 ELSE 0 END,
            v_addr,
            v_addr,
            CASE 
                WHEN i <= 500 THEN 'INVOICED'
                ELSE 
                    CASE (i % 4)
                        WHEN 0 THEN 'DRAFT'
                        WHEN 1 THEN 'SUBMITTED'
                        WHEN 2 THEN 'APPROVED'
                        ELSE 'FULFILLED'
                    END
            END,
            v_cust_name,
            v_cust_code,
            v_cust_type,
            v_tier,
            v_disc,
            'STANDARD',
            v_admin_id
        ) RETURNING id INTO v_so_id;

        -- Seed 2 to 4 items per order
        v_total := 0;
        FOR j IN 1..((i % 3) + 2) LOOP
            SELECT id, sku FROM products OFFSET ((i * 3 + j) % 250) LIMIT 1 INTO v_prod_id;
            
            v_qty := 1.00 + (j % 5);
            v_price := 4.50 + (j * 1.25);
            v_subtotal := v_qty * v_price;
            v_tax := v_subtotal * (v_tax_rate / 100.00);
            v_total := v_total + v_subtotal + v_tax;

            INSERT INTO sales_order_items (
                sales_order_id, product_id, ordered_quantity, unit_price, discount_percentage,
                tax_percentage, line_total, version, allocated_quantity, fulfilled_quantity, invoiced_quantity
            ) VALUES (
                v_so_id,
                v_prod_id,
                v_qty,
                v_price,
                v_disc,
                v_tax_rate,
                v_subtotal + v_tax,
                1,
                v_qty,
                CASE WHEN i % 4 = 0 OR i % 4 = 3 THEN v_qty ELSE 0.00 END,
                CASE WHEN i % 4 = 0 OR i % 4 = 3 THEN v_qty ELSE 0.00 END
            );
        END LOOP;

        -- ── 5. Seeding 500 Customer Invoices ────────────────────
        -- Link invoices to the first 500 completed sales orders
        IF i <= 500 THEN
            v_invoice_num := 'INV-CUST-' || LPAD(i::TEXT, 6, '0');

            INSERT INTO customer_invoices (
                company_id, customer_id, sales_order_id, invoice_number, client_reference_id,
                invoice_date, due_date, subtotal_amount, tax_amount, discount_amount, total_amount,
                paid_amount, outstanding_balance, status, currency_code, journal_entry_id,
                created_by, submitted_by, approved_by, cancelled_by, created_at, submitted_at, approved_at, version, credited_amount
            ) VALUES (
                v_company_id,
                v_cust_id,
                v_so_id,
                v_invoice_num,
                gen_random_uuid(),
                (v_now - (i * INTERVAL '4 hours'))::DATE,
                (v_now - (i * INTERVAL '4 hours') + INTERVAL '30 days')::DATE,
                v_total / 1.20,
                v_total - (v_total / 1.20),
                0.00,
                v_total,
                CASE WHEN i % 2 = 0 THEN v_total ELSE 0.00 END,
                CASE WHEN i % 2 = 0 THEN 0.00 ELSE v_total END,
                CASE WHEN i % 2 = 0 THEN 'PAID' ELSE 'APPROVED' END,
                'EUR',
                NULL, -- will link to GL below
                v_admin_id,
                v_admin_id,
                v_admin_id,
                NULL,
                v_now - (i * INTERVAL '4 hours'),
                v_now - (i * INTERVAL '4 hours') + INTERVAL '5 minutes',
                v_now - (i * INTERVAL '4 hours') + INTERVAL '10 minutes',
                1,
                0.00
            ) RETURNING id INTO v_ci_id;

            -- Invoice items
            FOR j IN 1..((i % 3) + 2) LOOP
                SELECT id FROM products OFFSET ((i * 3 + j) % 250) LIMIT 1 INTO v_prod_id;
                v_qty := 1.00 + (j % 5);
                v_price := 4.50 + (j * 1.25);
                v_subtotal := v_qty * v_price;
                v_tax := v_subtotal * (v_tax_rate / 100.00);

                INSERT INTO customer_invoice_items (
                    customer_invoice_id, sales_order_item_id, pick_list_item_id, product_id,
                    quantity, unit_price, discount_percentage, tax_percentage, net_amount, tax_amount, discount_amount, total_amount, returned_quantity, version
                ) VALUES (
                    v_ci_id,
                    NULL,
                    NULL,
                    v_prod_id,
                    v_qty,
                    v_price,
                    0.00,
                    v_tax_rate,
                    v_subtotal,
                    v_tax,
                    0.00,
                    v_subtotal + v_tax,
                    0.00,
                    1
                );
            END LOOP;

            -- ── 6. Seeding 500 Payments ─────────────────────────
            v_payment_num := 'PAY-' || LPAD(i::TEXT, 6, '0');
            
            INSERT INTO payments (
                payment_number, company_id, payment_date, payment_method, payment_type, amount,
                reference_number, journal_entry_id, currency_code, created_by, created_at, updated_at,
                supplier_id, status, customer_id, payment_batch_id
            ) VALUES (
                v_payment_num,
                v_company_id,
                (v_now - (i * INTERVAL '4 hours') + INTERVAL '1 day')::DATE,
                CASE (i % 3)
                    WHEN 0 THEN 'CARD'
                    WHEN 1 THEN 'BANK_TRANSFER'
                    ELSE 'CASH'
                END,
                'RECEIVABLE',
                v_total,
                'REF-' || LPAD(i::TEXT, 6, '0'),
                NULL,
                'EUR',
                v_admin_id,
                v_now - (i * INTERVAL '4 hours') + INTERVAL '1 day',
                v_now,
                NULL,
                'COMPLETED',
                v_cust_id,
                NULL
            ) RETURNING id INTO v_pay_id;

            -- ── 7. Seeding Journal Entries (GL logs) ────────────
            v_entry_num := 'JE-2026-' || LPAD(i::TEXT, 6, '0');

            INSERT INTO journal_entries (
                entry_number, company_id, entry_date, description, source_module,
                source_reference, status, reversal_entry_id, currency_code, posted_at, created_by, created_at, updated_at, closing_entry, closing_type
            ) VALUES (
                v_entry_num,
                v_company_id,
                (v_now - (i * INTERVAL '4 hours'))::DATE,
                'Sales Invoice posting ' || v_invoice_num,
                'SALES',
                v_invoice_num,
                'POSTED',
                NULL,
                'EUR',
                v_now - (i * INTERVAL '4 hours') + INTERVAL '12 minutes',
                v_admin_id,
                v_now - (i * INTERVAL '4 hours'),
                v_now,
                FALSE,
                NULL
            ) RETURNING id INTO v_je_id;

            -- Link journal entry to invoice and payment
            UPDATE customer_invoices SET journal_entry_id = v_je_id WHERE id = v_ci_id;
            UPDATE payments SET journal_entry_id = v_je_id WHERE id = v_pay_id;

            -- Double entry accounting lines: Debit Receivables (Bank/Cash), Credit Revenue
            INSERT INTO journal_entry_lines (journal_entry_id, account_id, debit_amount, credit_amount, dimension_set_id)
            VALUES (v_je_id, v_bank_id, v_total, 0.00, NULL);

            INSERT INTO journal_entry_lines (journal_entry_id, account_id, debit_amount, credit_amount, dimension_set_id)
            VALUES (v_je_id, v_rev_id, 0.00, v_total, NULL);
        END IF;
    END LOOP;

END $$;
