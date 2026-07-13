-- ============================================================
-- Targeted seed SQL using verified column names from information_schema
-- ============================================================

-- ── banks ──────────────────────────────────────────────────
-- Check if banks table exists and get its columns first
DO $$ 
DECLARE v_exists boolean;
BEGIN
  SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema='public' AND table_name='banks') INTO v_exists;
  IF v_exists THEN
    EXECUTE 'INSERT INTO banks (name, code, country, swift_code, bank_type, active) VALUES
      (''BNP Paribas'', ''BNPFR'', ''France'', ''BNPAFRPP'', ''COMMERCIAL'', true),
      (''Societe Generale'', ''SOGFR'', ''France'', ''SOGEFRPP'', ''COMMERCIAL'', true),
      (''Credit Agricole'', ''CRLFR'', ''France'', ''CRLYFRPP'', ''COMMERCIAL'', true),
      (''HSBC France'', ''HSBFR'', ''France'', ''MIDLFRPP'', ''COMMERCIAL'', true),
      (''LCL Banque'', ''LCLFR'', ''France'', ''CREDFRPP'', ''RETAIL'', true)
      ON CONFLICT DO NOTHING';
  END IF;
END $$;

-- ── bank_branches ──────────────────────────────────────────
DO $$ 
DECLARE v_bank_id bigint; v_exists boolean;
BEGIN
  SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema='public' AND table_name='bank_branches') INTO v_exists;
  IF NOT v_exists THEN RETURN; END IF;
  SELECT id INTO v_bank_id FROM banks LIMIT 1;
  IF v_bank_id IS NULL THEN RETURN; END IF;
  INSERT INTO bank_branches (bank_id, code, name, swift_code, address) VALUES
    (v_bank_id, 'BR001', 'HQ Branch',         'BNPAFRPPHQ',  '16 Blvd des Italiens, Paris'),
    (v_bank_id, 'BR002', 'La Defense Branch',  'BNPAFRPPDEF', '1 Place de la Coupole, Puteaux'),
    (v_bank_id, 'BR003', 'Champs Elysees',     'BNPAFRPPCE',  '29 Av Champs-Elysees, Paris'),
    (v_bank_id, 'BR004', 'Bordeaux Branch',    'BNPAFRPPBDX', '112 Cours Victor Hugo, Bordeaux'),
    (v_bank_id, 'BR005', 'Montparnasse',       'BNPAFRPPMNT', '7 Rue de Rennes, Paris')
  ON CONFLICT DO NOTHING;
END $$;

-- ── bank_accounts ──────────────────────────────────────────
DO $$
DECLARE v_company bigint; v_branch bigint; v_exists boolean;
BEGIN
  SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema='public' AND table_name='bank_accounts') INTO v_exists;
  IF NOT v_exists THEN RETURN; END IF;
  SELECT id INTO v_company FROM companies LIMIT 1;
  SELECT id INTO v_branch  FROM bank_branches LIMIT 1;
  -- Get actual columns
  INSERT INTO bank_accounts (company_id, account_number, iban, currency_code, account_type, bank_id, branch_id, active)
  SELECT v_company, 'ACC-' || generate_series, 'FR76' || lpad(generate_series::text, 23, '0'),
    (ARRAY['EUR','USD','GBP','INR','AED'])[(generate_series-1)%5+1], 'CURRENT',
    (SELECT id FROM banks LIMIT 1), v_branch, true
  FROM generate_series(1,5)
  ON CONFLICT DO NOTHING;
EXCEPTION WHEN OTHERS THEN
  RAISE NOTICE 'bank_accounts error: %', SQLERRM;
END $$;

-- ── asset_categories ────────────────────────────────────────
INSERT INTO asset_categories (company_id, code, name, depreciation_method, depreciation_rate, useful_life_years,
  asset_account_id, accumulated_depreciation_account_id, depreciation_expense_account_id,
  gain_loss_account_id, capitalization_threshold)
SELECT c.id, v.code, v.name, v.dm, v.dr, v.ly, a.id, a.id, a.id, a.id, v.ct
FROM (VALUES
  ('MACH','Machinery & Equipment','STRAIGHT_LINE',   10.00::numeric, 10, 5000.00::numeric),
  ('FURN','Furniture & Fixtures', 'STRAIGHT_LINE',   20.00::numeric,  5, 1000.00::numeric),
  ('COMP','Computers & IT',       'DECLINING_BALANCE',33.33::numeric, 3,  500.00::numeric),
  ('VEHI','Vehicles',             'STRAIGHT_LINE',   20.00::numeric,  5,10000.00::numeric),
  ('BLDG','Buildings',            'STRAIGHT_LINE',    2.00::numeric, 50,100000.00::numeric)
) AS v(code,name,dm,dr,ly,ct)
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM chart_of_accounts LIMIT 1) a
WHERE NOT EXISTS (SELECT 1 FROM asset_categories WHERE code=v.code)
ON CONFLICT DO NOTHING;

-- ── fixed_assets ─────────────────────────────────────────────
INSERT INTO fixed_assets (company_id, category_id, asset_code, name, description,
  acquisition_date, acquisition_cost, salvage_value, useful_life_years, depreciation_rate,
  depreciation_method, status, current_book_value, accumulated_depreciation, created_by, created_at, updated_at)
SELECT c.id, cat.id,
  'FA-' || lpad(gs::text,4,'0'),
  (ARRAY['Espresso Machine Pro','Coffee Roaster','POS System','Delivery Van','Office Furniture'])[gs],
  'Asset seed record #' || gs,
  CURRENT_DATE - (gs * 180 || ' days')::interval,
  (gs * 10000)::numeric, (gs * 500)::numeric, 5, 20.00::numeric, 'STRAIGHT_LINE', 'ACTIVE',
  (gs * 8000)::numeric, (gs * 2000)::numeric,
  'admin', NOW(), NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM asset_categories LIMIT 1) cat
ON CONFLICT DO NOTHING;

-- ── budgets ─────────────────────────────────────────────────
INSERT INTO budgets (company_id, fiscal_year_id, code, name, budget_type, period_type, scenario,
  status, version_number, is_forecast, is_frozen, is_active, created_by, created_at, updated_at)
SELECT c.id, fy.id,
  'BUD-2026-' || lpad(gs::text,3,'0'),
  (ARRAY['Operations','Capital Expenditure','Marketing','IT Infrastructure','HR Training'])[gs] || ' Budget 2026',
  (ARRAY['OPERATIONAL','CAPITAL','OPERATIONAL','CAPITAL','OPERATIONAL'])[gs],
  'MONTHLY', 'BASE', 'APPROVED', 1, false, false, true,
  'admin', NOW(), NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM fiscal_years LIMIT 1) fy
ON CONFLICT DO NOTHING;

-- ── journal_entries ──────────────────────────────────────────
INSERT INTO journal_entries (entry_number, company_id, entry_date, description, source_module,
  source_reference, status, currency_code, created_by, created_at, updated_at, closing_entry)
SELECT 'JE-2026-' || lpad(gs::text,4,'0'), c.id,
  (DATE '2026-01-31' + ((gs-1) * 15 || ' days')::interval),
  (ARRAY['Payroll Accrual','Monthly Depreciation','AP Invoice','Revenue Recognition','Tax Provision'])[gs],
  'GL', 'SEED-REF-' || gs, 'POSTED', 'EUR', 'admin', NOW(), NOW(), false
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
ON CONFLICT DO NOTHING;

-- ── journal_entry_lines ──────────────────────────────────────
INSERT INTO journal_entry_lines (journal_entry_id, account_id, debit_amount, credit_amount)
SELECT je.id, coa.id,
  CASE WHEN gs % 2 = 1 THEN (gs * 5000.00) ELSE 0.00 END,
  CASE WHEN gs % 2 = 0 THEN (gs * 5000.00) ELSE 0.00 END
FROM (SELECT id FROM journal_entries ORDER BY id LIMIT 5) je
CROSS JOIN (SELECT id FROM chart_of_accounts LIMIT 1) coa
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM journal_entry_lines WHERE journal_entry_id=je.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── payments ─────────────────────────────────────────────────
INSERT INTO payments (payment_number, company_id, payment_date, payment_method, payment_type,
  amount, reference_number, currency_code, created_by, created_at, updated_at, status)
SELECT 'PAY-2026-' || lpad(gs::text,4,'0'), c.id,
  (DATE '2026-01-15' + ((gs-1)*15 || ' days')::interval),
  (ARRAY['BANK_TRANSFER','CHEQUE','BANK_TRANSFER','DIRECT_DEBIT','BANK_TRANSFER'])[gs],
  (ARRAY['SUPPLIER','SUPPLIER','CUSTOMER','SUPPLIER','EMPLOYEE'])[gs],
  (gs * 5000.50)::numeric, 'REF-' || gs, 'EUR', 'admin', NOW(), NOW(), 'COMPLETED'
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
ON CONFLICT DO NOTHING;

-- ── customer_invoice_items ───────────────────────────────────
INSERT INTO customer_invoice_items (customer_invoice_id, product_id, quantity, unit_price,
  discount_percentage, tax_percentage, net_amount, tax_amount, discount_amount, total_amount, version, returned_quantity)
SELECT ci.id, p.id, (gs * 10.0)::numeric, (gs * 8.5)::numeric,
  5.00::numeric, 20.00::numeric,
  (gs * 10.0 * gs * 8.5 * 0.95)::numeric,
  (gs * 10.0 * gs * 8.5 * 0.95 * 0.20)::numeric,
  (gs * 10.0 * gs * 8.5 * 0.05)::numeric,
  (gs * 10.0 * gs * 8.5 * 0.95 * 1.20)::numeric, 1, 0.0
FROM (SELECT id FROM customer_invoices LIMIT 1) ci
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM customer_invoice_items WHERE customer_invoice_id=ci.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── customer_returns ─────────────────────────────────────────
INSERT INTO customer_returns (company_id, customer_id, warehouse_id, return_number, status, reason, created_by, created_at, updated_at, version)
SELECT c.id, cu.id, w.id,
  'CR-2026-' || lpad(gs::text,4,'0'),
  (ARRAY['PENDING','APPROVED','COMPLETED','REJECTED','PENDING'])[gs],
  (ARRAY['Damaged packaging','Wrong product','Quality issue','Expired product','Customer preference'])[gs],
  'admin', NOW(), NOW(), 1
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM customers LIMIT 1) cu
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── customer_return_items ─────────────────────────────────────
INSERT INTO customer_return_items (customer_return_id, product_id, quantity, inspection_result, inspection_notes, version)
SELECT cr.id, p.id, (gs * 5.0)::numeric,
  (ARRAY['PASS','FAIL','PARTIAL','PASS','FAIL'])[gs],
  'Inspection note #' || gs, 1
FROM (SELECT id FROM customer_returns LIMIT 5) cr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM customer_return_items WHERE customer_return_id=cr.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── goods_receipts ───────────────────────────────────────────
INSERT INTO goods_receipts (receipt_number, purchase_order_id, company_id, warehouse_id, received_by, received_at, status, total_quantity, total_amount)
SELECT 'GR-2026-' || lpad(gs::text,4,'0'), po.id, c.id, w.id,
  1, NOW(), 'COMPLETED', (gs * 200.0)::numeric, (gs * 1200.0)::numeric
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM purchase_orders LIMIT 1) po
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── goods_receipt_items ──────────────────────────────────────
INSERT INTO goods_receipt_items (goods_receipt_id, product_id, received_quantity)
SELECT gr.id, p.id, (gs * 50.0)::numeric
FROM (SELECT id FROM goods_receipts LIMIT 5) gr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM goods_receipt_items WHERE goods_receipt_id=gr.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── pick_lists ───────────────────────────────────────────────
INSERT INTO pick_lists (company_id, sales_order_id, pick_number, status, warehouse_id, created_by, created_at, version)
SELECT c.id, so.id,
  'PL-2026-' || lpad(gs::text,4,'0'),
  (ARRAY['PENDING','IN_PROGRESS','COMPLETED','PENDING','COMPLETED'])[gs],
  w.id, 1, NOW(), 1
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM sales_orders LIMIT 1) so
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── pick_list_items ──────────────────────────────────────────
INSERT INTO pick_list_items (pick_list_id, sales_order_item_id, product_id, ordered_quantity, allocated_quantity, picked_quantity, shipped_quantity, version)
SELECT pl.id, soi.id, soi.product_id,
  (gs * 20.0)::numeric, (gs * 20.0)::numeric,
  (gs * 18.0)::numeric, (gs * 18.0)::numeric, 1
FROM (SELECT id FROM pick_lists LIMIT 5) pl
CROSS JOIN (SELECT id, product_id FROM sales_order_items LIMIT 5) soi
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM pick_list_items WHERE pick_list_id=pl.id AND product_id=soi.product_id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── inventory_stock ──────────────────────────────────────────
INSERT INTO inventory_stock (product_id, warehouse_id, quantity, version, reserved_quantity)
SELECT p.id, w.id, (gs * 1000.0)::numeric, 1, (gs * 50.0)::numeric
FROM (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 3) w
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM inventory_stock WHERE product_id=p.id AND warehouse_id=w.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── period_locks ─────────────────────────────────────────────
INSERT INTO period_locks (company_id, lock_date, lock_type, locked_by, locked_at, reason)
SELECT c.id,
  (DATE '2026-01-31' + ((gs-1)*30 || ' days')::interval),
  (ARRAY['FULL','FULL','FULL','PARTIAL','OPEN'])[gs],
  'admin', NOW(), 'Month-end period close ' || gs
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
ON CONFLICT DO NOTHING;

-- ── exchange_rates ───────────────────────────────────────────
INSERT INTO exchange_rates (company_id, from_currency, to_currency, rate_type, rate, effective_date)
SELECT c.id, v.fc, 'EUR', 'SPOT', v.rt, CURRENT_DATE
FROM (VALUES ('USD',0.9205::numeric),('GBP',1.1650::numeric),('INR',0.0110::numeric),('AED',0.2506::numeric),('CHF',1.0450::numeric)) AS v(fc,rt)
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
WHERE NOT EXISTS (SELECT 1 FROM exchange_rates WHERE from_currency=v.fc AND to_currency='EUR')
ON CONFLICT DO NOTHING;

-- ── attendance ───────────────────────────────────────────────
INSERT INTO attendance (employee_id, shift_id, attendance_date, check_in_time, check_out_time,
  status, work_minutes, overtime_minutes, notes, created_at, updated_at, late_minutes, early_out_minutes,
  paid_leave, deduction, payroll_status)
SELECT e.id, s.id, CURRENT_DATE - (gs || ' days')::interval,
  (CURRENT_DATE - (gs || ' days')::interval + TIME '09:00:00'),
  (CURRENT_DATE - (gs || ' days')::interval + TIME '18:00:00'),
  'PRESENT', 480, 0, 'Seed attendance record', NOW(), NOW(), 0, 0, false, false, 'PENDING'
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM employees LIMIT 5) e
CROSS JOIN (SELECT id FROM shifts LIMIT 1) s
WHERE NOT EXISTS (SELECT 1 FROM attendance WHERE employee_id=e.id AND attendance_date=(CURRENT_DATE - (gs || ' days')::interval))
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── stock_movements ──────────────────────────────────────────
INSERT INTO stock_movements (product_id, warehouse_id, movement_type, quantity, reference_no, movement_at, created_by, reference_type, reference_id, reference_number)
SELECT p.id, w.id,
  (ARRAY['IN','IN','OUT','OUT','ADJUSTMENT'])[gs],
  (gs * 100.0)::numeric,
  'MOV-' || gs, NOW() - (gs || ' days')::interval, 1,
  'SEED', gs, 'REF-MOV-' || gs
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── stock_adjustments ────────────────────────────────────────
INSERT INTO stock_adjustments (adjustment_number, warehouse_id, product_id, previous_quantity, adjusted_quantity, reason, approved_by, created_at)
SELECT 'ADJ-2026-' || lpad(gs::text,4,'0'),
  w.id, p.id, (gs * 100.0)::numeric, ((gs * 100.0) + gs * 5.0)::numeric,
  (ARRAY['Cycle count','Damage write-off','Recount','Expiry removal','Quality reject'])[gs],
  1, NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
CROSS JOIN (SELECT id FROM products LIMIT 5) p
ON CONFLICT DO NOTHING;

-- ── inventory_adjustment_items ───────────────────────────────
INSERT INTO inventory_adjustment_items (adjustment_id, product_id, quantity, version)
SELECT a.id, p.id, (gs * 10.0)::numeric, 1
FROM (SELECT id FROM stock_adjustments LIMIT 5) a
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM inventory_adjustment_items WHERE adjustment_id=a.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── stock_counts ─────────────────────────────────────────────
INSERT INTO stock_counts (count_number, company_id, warehouse_id, status, count_type, blind_count,
  assigned_to, approval_required, created_by, created_at, version)
SELECT 'SC-2026-' || lpad(gs::text,4,'0'), c.id, w.id,
  (ARRAY['COMPLETED','COMPLETED','PENDING','SCHEDULED','SCHEDULED'])[gs],
  (ARRAY['FULL','PARTIAL','SPOT','FULL','PARTIAL'])[gs], false,
  1, false, 1, NOW(), 1
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── stock_count_items ────────────────────────────────────────
INSERT INTO stock_count_items (stock_count_id, product_id, system_quantity, reserved_quantity, available_quantity, counted_quantity, variance, version)
SELECT sc.id, p.id, (gs * 500.0)::numeric, (gs * 20.0)::numeric,
  (gs * 480.0)::numeric, (gs * 498.0)::numeric, (gs * -2.0)::numeric, 1
FROM (SELECT id FROM stock_counts LIMIT 5) sc
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM stock_count_items WHERE stock_count_id=sc.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── stock_transfers ──────────────────────────────────────────
INSERT INTO stock_transfers (transfer_number, source_warehouse_id, destination_warehouse_id, requested_by, status, requested_at)
SELECT 'ST-2026-' || lpad(gs::text,4,'0'),
  (SELECT id FROM warehouses LIMIT 1),
  (SELECT id FROM warehouses ORDER BY id DESC LIMIT 1),
  1,
  (ARRAY['COMPLETED','COMPLETED','IN_TRANSIT','PENDING','COMPLETED'])[gs],
  NOW() - (gs * 10 || ' days')::interval
FROM generate_series(1,5) gs
ON CONFLICT DO NOTHING;

-- ── stock_transfer_items ─────────────────────────────────────
INSERT INTO stock_transfer_items (stock_transfer_id, product_id, requested_quantity, transferred_quantity)
SELECT st.id, p.id, (gs * 100.0)::numeric, (gs * 98.0)::numeric
FROM (SELECT id FROM stock_transfers LIMIT 5) st
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM stock_transfer_items WHERE stock_transfer_id=st.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── purchase_request_items ───────────────────────────────────
INSERT INTO purchase_request_items (purchase_request_id, product_id, requested_quantity, unit_of_measure, remarks)
SELECT pr.id, p.id, (gs * 100.0)::numeric, 'KG', 'Seed requisition item #' || gs
FROM (SELECT id FROM purchase_requests LIMIT 5) pr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM purchase_request_items WHERE purchase_request_id=pr.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── sales_order_items ────────────────────────────────────────
INSERT INTO sales_order_items (sales_order_id, product_id, ordered_quantity, unit_price, discount_percentage, tax_percentage, line_total, version, allocated_quantity, fulfilled_quantity, invoiced_quantity)
SELECT so.id, p.id, (gs * 50.0)::numeric, (gs * 8.5)::numeric, 5.0::numeric, 20.0::numeric,
  (gs * 50.0 * gs * 8.5 * 0.95)::numeric, 1, (gs * 50.0)::numeric, (gs * 50.0)::numeric, (gs * 50.0)::numeric
FROM (SELECT id FROM sales_orders LIMIT 5) so
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM sales_order_items WHERE sales_order_id=so.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── purchase_order_items ─────────────────────────────────────
INSERT INTO purchase_order_items (purchase_order_id, product_id, ordered_quantity, unit_price, received_quantity, remaining_quantity, version, invoiced_quantity)
SELECT po.id, p.id, (gs * 200.0)::numeric, (gs * 6.5)::numeric, 0.0::numeric, (gs * 200.0)::numeric, 1, 0.0::numeric
FROM (SELECT id FROM purchase_orders LIMIT 5) po
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM purchase_order_items WHERE purchase_order_id=po.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── supplier_invoice_items ───────────────────────────────────
INSERT INTO supplier_invoice_items (supplier_invoice_id, product_id, quantity, unit_price, net_amount, tax_rate, tax_amount, discount_amount, total_amount)
SELECT si.id, p.id, (gs * 100.0)::numeric, (gs * 6.0)::numeric,
  (gs * 100.0 * gs * 6.0)::numeric, 20.0::numeric,
  (gs * 100.0 * gs * 6.0 * 0.20)::numeric, 0.0::numeric,
  (gs * 100.0 * gs * 6.0 * 1.20)::numeric
FROM (SELECT id FROM supplier_invoices LIMIT 5) si
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM supplier_invoice_items WHERE supplier_invoice_id=si.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── replenishment_rules ───────────────────────────────────────
INSERT INTO replenishment_rules (company_id, product_id, warehouse_id, min_quantity, max_quantity, reorder_point, reorder_quantity, lead_time_days, active, version, created_at, updated_at)
SELECT c.id, p.id, w.id,
  (gs * 100.0)::numeric, (gs * 1000.0)::numeric, (gs * 200.0)::numeric, (gs * 500.0)::numeric,
  14, true, 1, NOW(), NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
WHERE NOT EXISTS (SELECT 1 FROM replenishment_rules WHERE company_id=c.id AND product_id=p.id AND warehouse_id=w.id)
ON CONFLICT DO NOTHING;

-- ── replenishment_suggestions ─────────────────────────────────
INSERT INTO replenishment_suggestions (rule_id, company_id, product_id, warehouse_id, current_quantity, reserved_quantity, available_quantity, suggested_quantity, status, version, created_at, updated_at)
SELECT r.id, c.id, r.product_id, r.warehouse_id,
  (gs * 80.0)::numeric, (gs * 10.0)::numeric, (gs * 70.0)::numeric, (gs * 500.0)::numeric,
  (ARRAY['PENDING','APPROVED','ORDERED','PENDING','APPROVED'])[gs], 1, NOW(), NOW()
FROM (SELECT id, product_id, warehouse_id FROM replenishment_rules LIMIT 5) r
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM replenishment_suggestions WHERE rule_id=r.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── payment_batches ──────────────────────────────────────────
INSERT INTO payment_batches (company_id, batch_number, source_bank_account_id, status, total_amount, currency_code, created_by, created_at)
SELECT c.id, 'PB-2026-' || lpad(gs::text,4,'0'),
  ba.id, (ARRAY['COMPLETED','COMPLETED','PENDING','DRAFT','PENDING'])[gs],
  (gs * 25000.0)::numeric, 'EUR', 1, NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM companies LIMIT 1) c
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
ON CONFLICT DO NOTHING;

-- ── payment_allocations ───────────────────────────────────────
INSERT INTO payment_allocations (payment_id, supplier_invoice_id, allocated_amount)
SELECT p.id, si.id, (gs * 5000.0)::numeric
FROM (SELECT id FROM payments LIMIT 5) p
CROSS JOIN (SELECT id FROM supplier_invoices LIMIT 5) si
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM payment_allocations WHERE payment_id=p.id AND supplier_invoice_id=si.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── employee_shifts ───────────────────────────────────────────
INSERT INTO employee_shifts (employee_id, shift_id, effective_from, effective_to)
SELECT e.id, s.id, '2026-01-01', '2026-12-31'
FROM (SELECT id FROM employees LIMIT 5) e
CROSS JOIN (SELECT id FROM shifts LIMIT 1) s
WHERE NOT EXISTS (SELECT 1 FROM employee_shifts WHERE employee_id=e.id AND shift_id=s.id)
ON CONFLICT DO NOTHING;

-- ── bank_statement_lines (needs bank_statements first) ────────
INSERT INTO bank_statement_lines (statement_id, transaction_date, value_date, description, reference, amount, reconciled)
SELECT bs.id,
  (DATE '2026-01-05' + ((gs-1)*6 || ' days')::interval),
  (DATE '2026-01-05' + ((gs-1)*6 || ' days')::interval),
  (ARRAY['Supplier Payment','Customer Receipt','Payroll Transfer','Raw Material','Export Receipt'])[gs],
  'REF-' || lpad(gs::text,5,'0'),
  ((ARRAY[1,-1])[gs%2+1] * gs * 12000.0)::numeric, false
FROM (SELECT id FROM bank_statements LIMIT 5) bs
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM bank_statement_lines WHERE statement_id=bs.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── bank_statements (needs bank_accounts) ─────────────────────
INSERT INTO bank_statements (bank_account_id, statement_number, start_date, end_date,
  opening_balance, closing_balance, status, imported_at, imported_by)
SELECT ba.id, 'STMT-2026-' || lpad(gs::text,4,'0'),
  (DATE '2026-01-01' + ((gs-1)*30 || ' days')::interval),
  (DATE '2026-01-31' + ((gs-1)*30 || ' days')::interval),
  (gs * 100000.0)::numeric, (gs * 95000.0)::numeric,
  (ARRAY['RECONCILED','RECONCILED','PENDING','RECONCILED','PENDING'])[gs],
  NOW(), 'admin'
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
WHERE NOT EXISTS (SELECT 1 FROM bank_statements WHERE bank_account_id=ba.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── bank_virtual_accounts ────────────────────────────────────
INSERT INTO bank_virtual_accounts (parent_account_id, virtual_account_number, owner_reference_type, owner_reference_id, active)
SELECT ba.id, 'VA-' || lpad(gs::text,5,'0'), 'CUSTOMER', gs, true
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
WHERE NOT EXISTS (SELECT 1 FROM bank_virtual_accounts WHERE parent_account_id=ba.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── bank_account_signatories ─────────────────────────────────
INSERT INTO bank_account_signatories (bank_account_id, employee_id, signing_level, transaction_limit, effective_from, effective_to, active)
SELECT ba.id, e.id,
  (ARRAY['PRIMARY','SECONDARY','SOLE','DUAL','PRIMARY'])[gs],
  (gs * 25000.0)::numeric, '2026-01-01', '2026-12-31', true
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
CROSS JOIN (SELECT id FROM employees LIMIT 5) e
WHERE NOT EXISTS (SELECT 1 FROM bank_account_signatories WHERE bank_account_id=ba.id AND employee_id=e.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── bank_fee_rules ────────────────────────────────────────────
INSERT INTO bank_fee_rules (bank_account_id, charge_type, rate_percent, fixed_amount, gl_expense_account_id, active)
SELECT ba.id,
  (ARRAY['MAINT','TRANS','SVC','PENL','INT'])[gs],
  (gs * 0.1)::numeric, (gs * 2.50)::numeric, coa.id, true
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
CROSS JOIN (SELECT id FROM chart_of_accounts LIMIT 1) coa
WHERE NOT EXISTS (SELECT 1 FROM bank_fee_rules WHERE bank_account_id=ba.id AND charge_type=(ARRAY['MAINT','TRANS','SVC','PENL','INT'])[gs])
ON CONFLICT DO NOTHING;

-- ── bank_relationships ────────────────────────────────────────
INSERT INTO bank_relationships (bank_id, relationship_manager, credit_rating, banking_products, contract_expiry, sla_details, created_at)
SELECT b.id,
  (ARRAY['Jean-Pierre Martin','Marie Lefebvre','Philippe Moreau','Sarah Thompson','Henri Dubois'])[gs],
  (ARRAY['AA','AA+','A+','AA','A'])[gs],
  (ARRAY['Lending, FX, Payroll','Trade Finance, FX','Payroll, Savings','Investment, FX','Basic Banking'])[gs],
  (DATE '2027-12-31' - ((gs-1)*90 || ' days')::interval),
  (ARRAY['99.9% uptime SLA','99.5% uptime SLA','99.0% uptime SLA','99.9% uptime SLA','98.5% uptime SLA'])[gs],
  NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM banks LIMIT 5) b
WHERE NOT EXISTS (SELECT 1 FROM bank_relationships WHERE bank_id=b.id)
LIMIT 5
ON CONFLICT DO NOTHING;
