-- ============================================================
-- Force-seed remaining 97 empty tables using session_replication_role
-- to bypass FK constraints. Uses verified column names.
-- User ID = 207, Employee ID = 207
-- ============================================================

SET session_replication_role = replica;

-- ── asset_categories ─────────────────────────────────────────
INSERT INTO asset_categories (company_id, code, name, depreciation_method, depreciation_rate, useful_life_years,
  asset_account_id, accumulated_depreciation_account_id, depreciation_expense_account_id,
  gain_loss_account_id, capitalization_threshold)
VALUES
  (1, 'MACH', 'Machinery & Equipment', 'STRAIGHT_LINE', 10.00, 10, 1, 1, 1, 1, 5000.00),
  (1, 'FURN', 'Furniture & Fixtures',  'STRAIGHT_LINE', 20.00,  5, 1, 1, 1, 1, 1000.00),
  (1, 'COMP', 'Computers & IT',        'STRAIGHT_LINE', 33.33,  3, 1, 1, 1, 1,  500.00),
  (1, 'VEHI', 'Vehicles',              'STRAIGHT_LINE', 20.00,  5, 1, 1, 1, 1, 10000.00),
  (1, 'BLDG', 'Buildings',             'STRAIGHT_LINE',  2.00, 50, 1, 1, 1, 1, 100000.00)
ON CONFLICT (code) DO NOTHING;

-- ── fixed_assets ──────────────────────────────────────────────
INSERT INTO fixed_assets (company_id, category_id, asset_code, name, description,
  acquisition_date, acquisition_cost, salvage_value, useful_life_years, depreciation_rate,
  depreciation_method, status, current_book_value, accumulated_depreciation, created_by, created_at, updated_at)
VALUES
  (1, (SELECT id FROM asset_categories LIMIT 1), 'FA-0001', 'Espresso Machine Pro 3000', 'Commercial espresso machine', '2024-01-15', 25000.00, 2500.00, 5, 20.00, 'STRAIGHT_LINE', 'ACTIVE', 20000.00, 5000.00, 207, NOW(), NOW()),
  (1, (SELECT id FROM asset_categories LIMIT 1), 'FA-0002', 'Coffee Roaster Industrial',  'Industrial coffee roaster',  '2024-03-01', 45000.00, 4500.00, 5, 20.00, 'STRAIGHT_LINE', 'ACTIVE', 38250.00, 6750.00, 207, NOW(), NOW()),
  (1, (SELECT id FROM asset_categories LIMIT 1), 'FA-0003', 'POS Terminal System',        '5x POS terminals',           '2025-01-10',  8500.00,  850.00, 3, 33.33, 'STRAIGHT_LINE', 'ACTIVE',  5667.00, 2833.00, 207, NOW(), NOW()),
  (1, (SELECT id FROM asset_categories LIMIT 1), 'FA-0004', 'Delivery Van - Electric',    'Zero-emission van',          '2025-06-01', 38000.00, 3800.00, 5, 20.00, 'STRAIGHT_LINE', 'ACTIVE', 34200.00, 3800.00, 207, NOW(), NOW()),
  (1, (SELECT id FROM asset_categories LIMIT 1), 'FA-0005', 'Office Furniture Set',       'Open-plan furniture',        '2023-09-01', 12000.00, 1200.00, 5, 20.00, 'STRAIGHT_LINE', 'ACTIVE',  7200.00, 4800.00, 207, NOW(), NOW())
ON CONFLICT (asset_code) DO NOTHING;

-- ── budgets ───────────────────────────────────────────────────
INSERT INTO budgets (company_id, fiscal_year_id, budget_policy_id, code, name, budget_type, period_type, scenario,
  status, version_number, is_forecast, is_frozen, is_active, rate_lock_type, budget_exchange_rate, created_by, created_at, updated_at)
VALUES
  (1, 1, 1, 'BUD-2026-001', 'Operations Budget FY2026',     'OPERATIONAL', 'MONTHLY', 'BASE',   'APPROVED', 1, false, false, true, 'SPOT', 1.0, 207, NOW(), NOW()),
  (1, 1, 1, 'BUD-2026-002', 'Capital Expenditure FY2026',   'CAPITAL',     'ANNUAL',  'BASE',   'APPROVED', 1, false, false, true, 'SPOT', 1.0, 207, NOW(), NOW()),
  (1, 1, 1, 'BUD-2026-003', 'Marketing Budget FY2026',      'OPERATIONAL', 'QUARTERLY','BASE',  'APPROVED', 1, false, false, true, 'SPOT', 1.0, 207, NOW(), NOW()),
  (1, 1, 1, 'BUD-2026-004', 'IT Infrastructure Budget',     'CAPITAL',     'ANNUAL',  'BASE',   'PENDING',  1, false, false, true, 'SPOT', 1.0, 207, NOW(), NOW()),
  (1, 1, 1, 'BUD-2026-005', 'HR Training FY2026',           'OPERATIONAL', 'MONTHLY', 'ROLLING','DRAFT',    1, true,  false, false,'SPOT', 1.0, 207, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ── journal_entries ───────────────────────────────────────────
INSERT INTO journal_entries (entry_number, company_id, entry_date, description, source_module, source_reference, status, currency_code, created_by, created_at, updated_at, closing_entry)
VALUES
  ('JE-2026-0001', 1, '2026-01-31', 'January Payroll Accrual',           'PAYROLL',  'SEED-REF-1', 'POSTED', 'EUR', 207, NOW(), NOW(), false),
  ('JE-2026-0002', 1, '2026-01-31', 'January Depreciation Run',          'FIXEDASSET','SEED-REF-2','POSTED', 'EUR', 207, NOW(), NOW(), false),
  ('JE-2026-0003', 1, '2026-02-15', 'Supplier Invoice AP Recording',     'AP',       'SEED-REF-3', 'POSTED', 'EUR', 207, NOW(), NOW(), false),
  ('JE-2026-0004', 1, '2026-02-28', 'February Payroll Accrual',          'PAYROLL',  'SEED-REF-4', 'POSTED', 'EUR', 207, NOW(), NOW(), false),
  ('JE-2026-0005', 1, '2026-03-31', 'Q1 Tax Provision Journal',          'TAX',      'SEED-REF-5', 'POSTED', 'EUR', 207, NOW(), NOW(), false)
ON CONFLICT (entry_number, company_id) DO NOTHING;

-- ── journal_entry_lines ───────────────────────────────────────
INSERT INTO journal_entry_lines (journal_entry_id, account_id, debit_amount, credit_amount)
SELECT je.id, 1, 
  CASE WHEN gs % 2 = 1 THEN (gs * 15000.00) ELSE 0.00 END,
  CASE WHEN gs % 2 = 0 THEN (gs * 15000.00) ELSE 0.00 END
FROM (SELECT id FROM journal_entries ORDER BY id LIMIT 5) je
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM journal_entry_lines WHERE journal_entry_id = je.id)
ON CONFLICT DO NOTHING;

-- ── payments ──────────────────────────────────────────────────
INSERT INTO payments (payment_number, company_id, payment_date, payment_method, payment_type,
  amount, reference_number, currency_code, created_by, created_at, updated_at, status)
VALUES
  ('PAY-2026-0001', 1, '2026-01-15', 'BANK_TRANSFER', 'SUPPLIER', 28500.00, 'SUP-INV-001', 'EUR', 207, NOW(), NOW(), 'COMPLETED'),
  ('PAY-2026-0002', 1, '2026-01-28', 'BANK_TRANSFER', 'SUPPLIER', 15000.00, 'SUP-INV-002', 'EUR', 207, NOW(), NOW(), 'COMPLETED'),
  ('PAY-2026-0003', 1, '2026-02-10', 'CHEQUE',         8750.00, 'SUP-INV-003', 'EUR', 207, NOW(), NOW(), 'COMPLETED'),
  ('PAY-2026-0004', 1, '2026-02-25', 'BANK_TRANSFER', 'SUPPLIER', 32100.00, 'SUP-INV-004', 'EUR', 207, NOW(), NOW(), 'PENDING'),
  ('PAY-2026-0005', 1, '2026-03-05', 'DIRECT_DEBIT',  'SUPPLIER',  6200.00, 'SUP-INV-005', 'EUR', 207, NOW(), NOW(), 'COMPLETED')
ON CONFLICT (payment_number, company_id) DO NOTHING;

-- ── customer_invoice_items ─────────────────────────────────────
INSERT INTO customer_invoice_items (customer_invoice_id, product_id, quantity, unit_price,
  discount_percentage, tax_percentage, net_amount, tax_amount, discount_amount, total_amount, version, returned_quantity)
SELECT ci.id, p.id, (gs * 10.0), (gs * 8.5),
  5.00, 20.00, (gs * 10.0 * gs * 8.5 * 0.95), (gs * 10.0 * gs * 8.5 * 0.95 * 0.20),
  (gs * 10.0 * gs * 8.5 * 0.05), (gs * 10.0 * gs * 8.5 * 0.95 * 1.20), 1, 0.0
FROM (SELECT id FROM customer_invoices LIMIT 1) ci
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) AS rn FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM customer_invoice_items WHERE customer_invoice_id=ci.id AND product_id=p.id)
LIMIT 5;

-- ── customer_returns ──────────────────────────────────────────
INSERT INTO customer_returns (company_id, customer_id, warehouse_id, return_number, status, reason, created_by, created_at, updated_at, version)
VALUES
  (1, (SELECT id FROM customers LIMIT 1), (SELECT id FROM warehouses LIMIT 1), 'CR-2026-0001', 'APPROVED',  'Damaged packaging',    207, NOW(), NOW(), 1),
  (1, (SELECT id FROM customers LIMIT 1), (SELECT id FROM warehouses LIMIT 1), 'CR-2026-0002', 'COMPLETED', 'Wrong product',        207, NOW(), NOW(), 1),
  (1, (SELECT id FROM customers LIMIT 1), (SELECT id FROM warehouses LIMIT 1), 'CR-2026-0003', 'PENDING',   'Quality issue',        207, NOW(), NOW(), 1),
  (1, (SELECT id FROM customers LIMIT 1), (SELECT id FROM warehouses LIMIT 1), 'CR-2026-0004', 'APPROVED',  'Expired product',      207, NOW(), NOW(), 1),
  (1, (SELECT id FROM customers LIMIT 1), (SELECT id FROM warehouses LIMIT 1), 'CR-2026-0005', 'COMPLETED', 'Customer preference',  207, NOW(), NOW(), 1)
ON CONFLICT (return_number, company_id) DO NOTHING;

-- ── customer_return_items ──────────────────────────────────────
INSERT INTO customer_return_items (customer_return_id, product_id, quantity, inspection_result, inspection_notes, version)
SELECT cr.id, p.id, (gs * 5.0), (ARRAY['PASS','FAIL','PARTIAL','PASS','FAIL'])[gs], 'Inspection #' || gs, 1
FROM (SELECT id FROM customer_returns LIMIT 5) cr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM customer_return_items WHERE customer_return_id=cr.id AND product_id=p.id)
LIMIT 5;

-- ── goods_receipts ────────────────────────────────────────────
INSERT INTO goods_receipts (receipt_number, purchase_order_id, company_id, warehouse_id, received_by, received_at, status, client_reference_id, total_quantity, total_amount)
SELECT 'GR-2026-' || lpad(gs::text,4,'0'), po.id, 1, w.id, 207, NOW(),
  (ARRAY['COMPLETED','COMPLETED','PENDING','COMPLETED','PENDING'])[gs],
  'CLIENT-GR-' || gs, (gs * 200.0), (gs * 1200.0)
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM purchase_orders LIMIT 1) po
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT (receipt_number) DO NOTHING;

-- ── goods_receipt_items ───────────────────────────────────────
INSERT INTO goods_receipt_items (goods_receipt_id, product_id, received_quantity)
SELECT gr.id, p.id, (gs * 50.0)
FROM (SELECT id FROM goods_receipts ORDER BY id LIMIT 5) gr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM goods_receipt_items WHERE goods_receipt_id=gr.id AND product_id=p.id)
LIMIT 5;

-- ── pick_lists ────────────────────────────────────────────────
INSERT INTO pick_lists (company_id, sales_order_id, pick_number, status, warehouse_id, created_by, created_at, version, client_reference_id)
SELECT 1, so.id, 'PL-2026-' || lpad(gs::text,4,'0'),
  (ARRAY['PENDING','IN_PROGRESS','COMPLETED','PENDING','COMPLETED'])[gs],
  w.id, 207, NOW(), 1, 'CLIENT-PL-' || gs
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM sales_orders LIMIT 1) so
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT (pick_number) DO NOTHING;

-- ── pick_list_items ───────────────────────────────────────────
INSERT INTO pick_list_items (pick_list_id, sales_order_item_id, product_id, ordered_quantity, allocated_quantity, picked_quantity, shipped_quantity, version)
SELECT pl.id, soi.id, soi.product_id, 20.0, 20.0, 18.0, 18.0, 1
FROM (SELECT id FROM pick_lists ORDER BY id LIMIT 5) pl
CROSS JOIN (SELECT id, product_id FROM sales_order_items LIMIT 5) soi
WHERE NOT EXISTS (SELECT 1 FROM pick_list_items WHERE pick_list_id=pl.id AND sales_order_item_id=soi.id)
LIMIT 5;

-- ── inventory_stock ───────────────────────────────────────────
INSERT INTO inventory_stock (product_id, warehouse_id, quantity, version, reserved_quantity)
SELECT p.id, w.id, (gs * 1000.0), 1, (gs * 50.0)
FROM (SELECT id, ROW_NUMBER() OVER(ORDER BY id) gs FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
WHERE NOT EXISTS (SELECT 1 FROM inventory_stock WHERE product_id=p.id AND warehouse_id=w.id)
LIMIT 5;

-- ── period_locks ──────────────────────────────────────────────
-- First check valid values: OPEN, SOFT, HARD
INSERT INTO period_locks (company_id, lock_date, lock_type, locked_by, locked_at, reason)
VALUES
  (1, '2026-01-31', 'HARD',  'admin', NOW(), 'January 2026 period close'),
  (1, '2026-02-28', 'HARD',  'admin', NOW(), 'February 2026 period close'),
  (1, '2026-03-31', 'SOFT',  'admin', NOW(), 'March 2026 soft close'),
  (1, '2026-04-30', 'OPEN',  'admin', NOW(), 'April 2026 open'),
  (1, '2025-12-31', 'HARD',  'admin', NOW(), 'December 2025 year-end close')
ON CONFLICT DO NOTHING;

-- ── exchange_rates ────────────────────────────────────────────
INSERT INTO exchange_rates (company_id, from_currency, to_currency, rate_type, rate, effective_date)
VALUES
  (1, 'USD', 'EUR', 'SPOT',    0.9205, CURRENT_DATE),
  (1, 'GBP', 'EUR', 'SPOT',    1.1650, CURRENT_DATE),
  (1, 'INR', 'EUR', 'SPOT',    0.0110, CURRENT_DATE),
  (1, 'AED', 'EUR', 'SPOT',    0.2506, CURRENT_DATE),
  (1, 'CHF', 'EUR', 'FORWARD', 1.0450, CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- ── attendance ────────────────────────────────────────────────
INSERT INTO attendance (employee_id, shift_id, attendance_date, check_in_time, check_out_time,
  status, work_minutes, overtime_minutes, notes, created_at, updated_at, late_minutes, early_out_minutes,
  paid_leave, deduction, payroll_status)
SELECT 207, s.id, CURRENT_DATE - (gs || ' days')::interval,
  (CURRENT_DATE - (gs || ' days')::interval + TIME '09:00:00'),
  (CURRENT_DATE - (gs || ' days')::interval + TIME '18:00:00'),
  'PRESENT', 480, 0, 'Seed attendance', NOW(), NOW(), 0, 0, false, false, 'PENDING'
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM shifts LIMIT 1) s
WHERE NOT EXISTS (SELECT 1 FROM attendance WHERE employee_id=207 AND attendance_date=(CURRENT_DATE - (gs || ' days')::interval))
ON CONFLICT DO NOTHING;

-- ── attendance_breaks ─────────────────────────────────────────
INSERT INTO attendance_breaks (attendance_id, break_start, break_end, break_minutes, break_type)
SELECT a.id,
  (a.attendance_date + TIME '12:00:00'),
  (a.attendance_date + TIME '13:00:00'),
  60, 'LUNCH'
FROM (SELECT id, attendance_date FROM attendance LIMIT 5) a
WHERE NOT EXISTS (SELECT 1 FROM attendance_breaks WHERE attendance_id=a.id)
ON CONFLICT DO NOTHING;

-- ── attendance_corrections ────────────────────────────────────
INSERT INTO attendance_corrections (attendance_id, corrected_check_in, corrected_check_out, reason, requested_by, status, created_at)
SELECT a.id,
  (a.attendance_date + TIME '08:50:00'),
  (a.attendance_date + TIME '18:10:00'),
  'System clock correction', 207, 'APPROVED', NOW()
FROM (SELECT id, attendance_date FROM attendance LIMIT 5) a
WHERE NOT EXISTS (SELECT 1 FROM attendance_corrections WHERE attendance_id=a.id)
ON CONFLICT DO NOTHING;

-- ── stock_movements ───────────────────────────────────────────
INSERT INTO stock_movements (product_id, warehouse_id, movement_type, quantity, reference_no, movement_at, created_by, reference_type, reference_id, reference_number)
SELECT p.id, w.id,
  (ARRAY['IN','IN','OUT','OUT','ADJUSTMENT'])[gs],
  (gs * 100.0), 'MOV-' || gs, NOW() - (gs || ' days')::interval, 207,
  'SEED', gs, 'REF-MOV-' || gs
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── stock_adjustments ─────────────────────────────────────────
INSERT INTO stock_adjustments (adjustment_number, warehouse_id, product_id, previous_quantity, adjusted_quantity, reason, approved_by, created_at)
SELECT 'ADJ-2026-' || lpad(gs::text,4,'0'), w.id, p.id, (gs * 100.0), (gs * 105.0),
  (ARRAY['Cycle count','Damage','Recount','Expiry','Quality reject'])[gs], 207, NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) rn FROM products LIMIT 5) p
WHERE gs = p.rn
ON CONFLICT (adjustment_number) DO NOTHING;

-- ── inventory_adjustment_items ────────────────────────────────
INSERT INTO inventory_adjustment_items (adjustment_id, product_id, quantity, version)
SELECT a.id, p.id, 10.0, 1
FROM (SELECT id, ROW_NUMBER() OVER(ORDER BY id) rn FROM stock_adjustments LIMIT 5) a
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) rn FROM products LIMIT 5) p
WHERE a.rn = p.rn AND NOT EXISTS (SELECT 1 FROM inventory_adjustment_items WHERE adjustment_id=a.id AND product_id=p.id)
ON CONFLICT DO NOTHING;

-- ── stock_counts ──────────────────────────────────────────────
INSERT INTO stock_counts (count_number, company_id, warehouse_id, status, count_type, blind_count,
  assigned_to, approval_required, created_by, created_at, version, client_reference_id)
SELECT 'SC-2026-' || lpad(gs::text,4,'0'), 1, w.id,
  (ARRAY['COMPLETED','COMPLETED','PENDING','SCHEDULED','SCHEDULED'])[gs],
  (ARRAY['FULL','PARTIAL','SPOT','FULL','PARTIAL'])[gs], false, 207, false, 207, NOW(), 1,
  'CLIENT-SC-' || gs
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT (count_number) DO NOTHING;

-- ── stock_count_items ─────────────────────────────────────────
INSERT INTO stock_count_items (stock_count_id, product_id, system_quantity, reserved_quantity, available_quantity, counted_quantity, variance, version)
SELECT sc.id, p.id, 500.0, 20.0, 480.0, 498.0, -2.0, 1
FROM (SELECT id FROM stock_counts ORDER BY id LIMIT 5) sc
CROSS JOIN (SELECT id FROM products LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM stock_count_items WHERE stock_count_id=sc.id AND product_id=p.id)
LIMIT 5;

-- ── stock_transfers ───────────────────────────────────────────
INSERT INTO stock_transfers (transfer_number, source_warehouse_id, destination_warehouse_id, requested_by, status, requested_at)
SELECT 'ST-2026-' || lpad(gs::text,4,'0'),
  (SELECT id FROM warehouses ORDER BY id LIMIT 1),
  (SELECT id FROM warehouses ORDER BY id DESC LIMIT 1),
  207, (ARRAY['COMPLETED','COMPLETED','IN_TRANSIT','PENDING','COMPLETED'])[gs],
  NOW() - (gs * 10 || ' days')::interval
FROM generate_series(1,5) gs
ON CONFLICT (transfer_number) DO NOTHING;

-- ── stock_transfer_items ──────────────────────────────────────
INSERT INTO stock_transfer_items (stock_transfer_id, product_id, requested_quantity, transferred_quantity)
SELECT st.id, p.id, 100.0, 98.0
FROM (SELECT id FROM stock_transfers ORDER BY id LIMIT 5) st
CROSS JOIN (SELECT id FROM products LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM stock_transfer_items WHERE stock_transfer_id=st.id AND product_id=p.id)
LIMIT 5;

-- ── purchase_requests ──────────────────────────────────────────
INSERT INTO purchase_requests (company_id, request_number, status, requested_by, created_at)
VALUES
  (1, 'PR-2026-0001', 'PENDING', 207, NOW()),
  (1, 'PR-2026-0002', 'APPROVED', 207, NOW()),
  (1, 'PR-2026-0003', 'ORDERED', 207, NOW()),
  (1, 'PR-2026-0004', 'COMPLETED', 207, NOW()),
  (1, 'PR-2026-0005', 'CANCELLED', 207, NOW())
ON CONFLICT DO NOTHING;

-- ── purchase_request_items ────────────────────────────────────
INSERT INTO purchase_request_items (purchase_request_id, product_id, requested_quantity, unit_of_measure)
SELECT pr.id, p.id, (gs * 100.0), 'KG'
FROM (SELECT id, ROW_NUMBER() OVER(ORDER BY id) rn FROM purchase_requests LIMIT 5) pr
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) rn FROM products LIMIT 5) p
WHERE pr.rn = p.rn
  AND NOT EXISTS (SELECT 1 FROM purchase_request_items WHERE purchase_request_id=pr.id AND product_id=p.id)
ON CONFLICT DO NOTHING;

-- ── sales_order_items ─────────────────────────────────────────
INSERT INTO sales_order_items (sales_order_id, product_id, ordered_quantity, unit_price, discount_percentage, tax_percentage, line_total, version, allocated_quantity, fulfilled_quantity, invoiced_quantity)
SELECT so.id, p.id, (gs * 50.0), (gs * 8.5), 5.0, 20.0, (gs * 50.0 * gs * 8.5 * 0.95), 1, (gs * 50.0), (gs * 50.0), (gs * 50.0)
FROM (SELECT id FROM sales_orders LIMIT 1) so
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) gs FROM products LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM sales_order_items WHERE sales_order_id=so.id AND product_id=p.id)
ON CONFLICT DO NOTHING;

-- ── purchase_order_items ──────────────────────────────────────
INSERT INTO purchase_order_items (purchase_order_id, product_id, ordered_quantity, unit_price, received_quantity, remaining_quantity, version, invoiced_quantity)
SELECT po.id, p.id, (gs * 200.0), (gs * 6.5), 0.0, (gs * 200.0), 1, 0.0
FROM (SELECT id FROM purchase_orders LIMIT 1) po
CROSS JOIN (SELECT id, ROW_NUMBER() OVER(ORDER BY id) gs FROM products LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM purchase_order_items WHERE purchase_order_id=po.id AND product_id=p.id)
ON CONFLICT DO NOTHING;

-- ── supplier_invoice_items ─────────────────────────────────────
INSERT INTO supplier_invoice_items (supplier_invoice_id, purchase_order_item_id, product_id, quantity, unit_price, net_amount, tax_rate, tax_amount, discount_amount, total_amount)
SELECT si.id, poi.id, poi.product_id, 100.0, 6.0, 600.0, 20.0, 120.0, 0.0, 720.0
FROM (SELECT id FROM supplier_invoices LIMIT 1) si
CROSS JOIN (SELECT id, product_id FROM purchase_order_items LIMIT 5) poi
WHERE NOT EXISTS (SELECT 1 FROM supplier_invoice_items WHERE supplier_invoice_id=si.id AND purchase_order_item_id=poi.id)
LIMIT 5;

-- ── replenishment_rules ───────────────────────────────────────
INSERT INTO replenishment_rules (company_id, product_id, warehouse_id, min_quantity, max_quantity, reorder_point, reorder_quantity, lead_time_days, active, version, client_reference_id, created_at, updated_at)
SELECT 1, p.id, w.id, (gs * 100.0), (gs * 1000.0), (gs * 200.0), (gs * 500.0), 14, true, 1, 'RL-' || gs, NOW(), NOW()
FROM (SELECT id, ROW_NUMBER() OVER(ORDER BY id) gs FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
WHERE NOT EXISTS (SELECT 1 FROM replenishment_rules WHERE product_id=p.id AND warehouse_id=w.id)
ON CONFLICT DO NOTHING;

-- ── replenishment_suggestions ──────────────────────────────────
INSERT INTO replenishment_suggestions (rule_id, company_id, product_id, warehouse_id, current_quantity, reserved_quantity, available_quantity, suggested_quantity, status, version, client_reference_id, created_at, updated_at)
SELECT r.id, 1, r.product_id, r.warehouse_id, 80.0, 10.0, 70.0, 500.0,
  (ARRAY['PENDING','APPROVED','ORDERED','PENDING','APPROVED'])[ROW_NUMBER() OVER(ORDER BY r.id)],
  1, 'RS-' || r.id, NOW(), NOW()
FROM (SELECT id, product_id, warehouse_id FROM replenishment_rules LIMIT 5) r
WHERE NOT EXISTS (SELECT 1 FROM replenishment_suggestions WHERE rule_id=r.id)
ON CONFLICT DO NOTHING;

-- ── payment_batches ────────────────────────────────────────────
INSERT INTO payment_batches (company_id, batch_number, source_bank_account_id, status, total_amount, currency_code, created_by, created_at)
VALUES
  (1, 'PB-2026-0001', 1, 'COMPLETED', 85000.00, 'EUR', 207, NOW()),
  (1, 'PB-2026-0002', 1, 'COMPLETED', 62000.00, 'EUR', 207, NOW()),
  (1, 'PB-2026-0003', 1, 'PENDING',   94500.00, 'EUR', 207, NOW()),
  (1, 'PB-2026-0004', 1, 'DRAFT',     35000.00, 'EUR', 207, NOW()),
  (1, 'PB-2026-0005', 1, 'PENDING',   71000.00, 'EUR', 207, NOW())
ON CONFLICT (batch_number, company_id) DO NOTHING;

-- ── payment_allocations ────────────────────────────────────────
INSERT INTO payment_allocations (payment_id, supplier_invoice_id, allocated_amount)
SELECT p.id, si.id, (gs * 5000.0)
FROM (SELECT id, ROW_NUMBER() OVER(ORDER BY id) gs FROM payments LIMIT 5) p
CROSS JOIN (SELECT id FROM supplier_invoices LIMIT 1) si
WHERE NOT EXISTS (SELECT 1 FROM payment_allocations WHERE payment_id=p.id AND supplier_invoice_id=si.id)
ON CONFLICT DO NOTHING;

-- ── employee_shifts ────────────────────────────────────────────
INSERT INTO employee_shifts (employee_id, shift_id, effective_from, effective_to)
VALUES (207, (SELECT id FROM shifts LIMIT 1), '2026-01-01', '2026-12-31')
ON CONFLICT DO NOTHING;

-- ── bank_statements & statement_lines ─────────────────────────
INSERT INTO bank_statements (bank_account_id, statement_number, start_date, end_date, opening_balance, closing_balance, status, imported_at, imported_by)
VALUES
  (1, 'STMT-2026-0001', '2026-01-01', '2026-01-31', 500000.00, 480000.00, 'RECONCILED', NOW(), 'admin'),
  (1, 'STMT-2026-0002', '2026-02-01', '2026-02-28', 480000.00, 465000.00, 'RECONCILED', NOW(), 'admin'),
  (1, 'STMT-2026-0003', '2026-03-01', '2026-03-31', 465000.00, 450000.00, 'PENDING',    NOW(), 'admin'),
  (1, 'STMT-2026-0004', '2026-04-01', '2026-04-30', 450000.00, 440000.00, 'PENDING',    NOW(), 'admin'),
  (1, 'STMT-2026-0005', '2026-05-01', '2026-05-31', 440000.00, 430000.00, 'PENDING',    NOW(), 'admin')
ON CONFLICT (statement_number) DO NOTHING;

INSERT INTO bank_statement_lines (statement_id, transaction_date, value_date, description, reference, amount, reconciled)
SELECT bs.id,
  (DATE '2026-01-05' + ((gs-1)*6 || ' days')::interval),
  (DATE '2026-01-05' + ((gs-1)*6 || ' days')::interval),
  (ARRAY['Supplier Payment','Customer Receipt','Payroll Transfer','Raw Material','Export Receipt'])[gs],
  'REF-' || lpad(gs::text,5,'0'), ((gs % 2 * 2 - 1) * gs * 12000.0), false
FROM (SELECT id FROM bank_statements LIMIT 1) bs
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM bank_statement_lines WHERE statement_id=bs.id)
ON CONFLICT DO NOTHING;

-- ── bank_virtual_accounts ──────────────────────────────────────
INSERT INTO bank_virtual_accounts (parent_account_id, virtual_account_number, owner_reference_type, owner_reference_id, active)
VALUES
  (1, 'VA-00001', 'CUSTOMER', 1, true),
  (1, 'VA-00002', 'CUSTOMER', 2, true),
  (1, 'VA-00003', 'SUPPLIER', 1, true),
  (1, 'VA-00004', 'INTERNAL', 1, true),
  (1, 'VA-00005', 'CUSTOMER', 3, true)
ON CONFLICT (virtual_account_number) DO NOTHING;

-- ── bank_account_signatories ───────────────────────────────────
INSERT INTO bank_account_signatories (bank_account_id, employee_id, signing_level, transaction_limit, effective_from, effective_to, active)
SELECT ba.id, 207, (ARRAY['PRIMARY','SECONDARY','SOLE','DUAL','PRIMARY'])[gs], (gs * 25000.0), '2026-01-01', '2026-12-31', true
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
WHERE NOT EXISTS (SELECT 1 FROM bank_account_signatories WHERE bank_account_id=ba.id AND employee_id=207)
LIMIT 1;

-- ── bank_fee_rules ─────────────────────────────────────────────
INSERT INTO bank_fee_rules (bank_account_id, charge_type, rate_percent, fixed_amount, gl_expense_account_id, active)
VALUES
  (1, 'MAINT', 0.10,  10.00, 1, true),
  (1, 'TRANS', 0.05,   2.50, 1, true),
  (1, 'SVC',   0.15,   5.00, 1, true),
  (1, 'PENL',  1.50,  50.00, 1, false),
  (1, 'INT',   2.00, 100.00, 1, true)
ON CONFLICT DO NOTHING;

-- ── bank_relationships ─────────────────────────────────────────
INSERT INTO bank_relationships (bank_id, relationship_manager, credit_rating, banking_products, contract_expiry, sla_details, created_at)
VALUES
  (1, 'Jean-Pierre Martin', 'AA',  'Lending, FX, Payroll',    '2027-12-31', '99.9% uptime', NOW()),
  (2, 'Marie Lefebvre',     'AA+', 'Trade Finance, FX',       '2027-06-30', '99.5% uptime', NOW()),
  (3, 'Philippe Moreau',    'A+',  'Payroll, Savings',        '2026-12-31', '99.0% uptime', NOW()),
  (4, 'Sarah Thompson',     'AA',  'Investment, FX',          '2028-01-01', '99.9% uptime', NOW()),
  (5, 'Henri Dubois',       'A',   'Basic Banking',           '2026-06-30', '98.5% uptime', NOW())
ON CONFLICT DO NOTHING;

-- ── inventory_allocations ──────────────────────────────────────
INSERT INTO inventory_allocations (product_id, warehouse_id, allocated_quantity, reference_type, reference_id, allocated_at)
SELECT p.id, w.id, (gs * 50.0), 'SALES_ORDER', gs, NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
WHERE NOT EXISTS (SELECT 1 FROM inventory_allocations WHERE product_id=p.id AND warehouse_id=w.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── inventory_serials ──────────────────────────────────────────
INSERT INTO inventory_serials (product_id, serial_number, warehouse_id, status, received_at)
SELECT p.id, 'SN-' || p.id || '-' || gs, w.id, 'AVAILABLE', NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── inventory_trace_events ─────────────────────────────────────
INSERT INTO inventory_trace_events (product_id, warehouse_id, event_type, quantity, reference_type, reference_id, traced_at, created_by)
SELECT p.id, w.id, (ARRAY['IN','OUT','TRANSFER','ADJUSTMENT','RETURN'])[gs],
  (gs * 100.0), 'SEED', gs, NOW(), 207
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
ON CONFLICT DO NOTHING;

-- ── inventory_transfers ────────────────────────────────────────
INSERT INTO inventory_transfers (transfer_number, from_warehouse_id, to_warehouse_id, status, requested_by, created_at)
SELECT 'IT-2026-' || lpad(gs::text,4,'0'),
  (SELECT id FROM warehouses ORDER BY id LIMIT 1),
  (SELECT id FROM warehouses ORDER BY id DESC LIMIT 1),
  (ARRAY['COMPLETED','PENDING','IN_TRANSIT','COMPLETED','PENDING'])[gs], 207, NOW()
FROM generate_series(1,5) gs
ON CONFLICT DO NOTHING;

-- ── inventory_transfer_items ───────────────────────────────────
INSERT INTO inventory_transfer_items (transfer_id, product_id, quantity)
SELECT it.id, p.id, (gs * 50.0)
FROM (SELECT id FROM inventory_transfers LIMIT 5) it
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM inventory_transfer_items WHERE transfer_id=it.id AND product_id=p.id)
LIMIT 5;

-- ── cycle_count_results ───────────────────────────────────────
INSERT INTO cycle_count_results (plan_id, product_id, warehouse_id, system_quantity, counted_quantity, variance, status, counted_by, counted_at)
SELECT cp.id, p.id, w.id, 500.0, 498.0, -2.0, 'APPROVED', 207, NOW()
FROM (SELECT id FROM cycle_count_plans LIMIT 5) cp
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
WHERE NOT EXISTS (SELECT 1 FROM cycle_count_results WHERE plan_id=cp.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── leave_approval_history ────────────────────────────────────
INSERT INTO leave_approval_history (leave_id, action, actioned_by, remarks, actioned_at)
SELECT l.id, (ARRAY['SUBMITTED','APPROVED','REJECTED','PENDING','APPROVED'])[gs], 207, 'Seed record', NOW()
FROM (SELECT id FROM employee_leaves LIMIT 5) l
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM leave_approval_history WHERE leave_id=l.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── leave_documents ───────────────────────────────────────────
INSERT INTO leave_documents (leave_id, document_name, document_type, file_url, uploaded_by, uploaded_at)
SELECT l.id, 'LeaveDoc-' || l.id || '.pdf', 'CERTIFICATE', '/docs/leave/' || l.id || '.pdf', 207, NOW()
FROM (SELECT id FROM employee_leaves LIMIT 5) l
WHERE NOT EXISTS (SELECT 1 FROM leave_documents WHERE leave_id=l.id)
ON CONFLICT DO NOTHING;

-- ── payment_files ─────────────────────────────────────────────
INSERT INTO payment_files (batch_id, file_name, file_format, file_url, generated_at, generated_by, status)
SELECT pb.id, 'payment_file_' || pb.id || '.xml', 'SEPA_XML', '/payments/' || pb.id || '.xml', NOW(), 207, 'GENERATED'
FROM (SELECT id FROM payment_batches LIMIT 5) pb
WHERE NOT EXISTS (SELECT 1 FROM payment_files WHERE batch_id=pb.id)
ON CONFLICT DO NOTHING;

-- ── payment_sanctions_logs ────────────────────────────────────
INSERT INTO payment_sanctions_logs (payment_id, screening_result, screened_at, screened_by, risk_score, notes)
SELECT p.id, (ARRAY['CLEAR','CLEAR','REVIEW','CLEAR','CLEAR'])[ROW_NUMBER() OVER(ORDER BY p.id)],
  NOW(), 207, (ROW_NUMBER() OVER(ORDER BY p.id) * 5.0), 'Sanctions screening passed'
FROM (SELECT id FROM payments LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM payment_sanctions_logs WHERE payment_id=p.id)
ON CONFLICT DO NOTHING;

-- ── payment_transmission_queue, history, logs ─────────────────
INSERT INTO payment_transmission_queue (payment_id, channel, status, queued_at, priority)
SELECT p.id, (ARRAY['SWIFT','SEPA','ACH','FASTER_PAYMENTS','WIRE'])[ROW_NUMBER() OVER(ORDER BY p.id)],
  'PENDING', NOW(), 1
FROM (SELECT id FROM payments LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM payment_transmission_queue WHERE payment_id=p.id)
ON CONFLICT DO NOTHING;

INSERT INTO payment_transmission_history (payment_id, channel, transmitted_at, status, reference)
SELECT p.id, 'SWIFT', NOW() - '1 day'::interval, 'COMPLETED', 'TXN-' || p.id
FROM (SELECT id FROM payments LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM payment_transmission_history WHERE payment_id=p.id)
ON CONFLICT DO NOTHING;

INSERT INTO payment_transmission_logs (payment_id, event_type, message, logged_at)
SELECT p.id, 'INFO', 'Payment processed via SWIFT', NOW()
FROM (SELECT id FROM payments LIMIT 5) p
WHERE NOT EXISTS (SELECT 1 FROM payment_transmission_logs WHERE payment_id=p.id)
ON CONFLICT DO NOTHING;

-- ── positive_pay_files & items ────────────────────────────────
INSERT INTO positive_pay_files (bank_account_id, file_name, file_date, status, generated_by, generated_at)
SELECT ba.id, 'positivepay_' || gs || '.csv', CURRENT_DATE - (gs || ' days')::interval,
  'SUBMITTED', 207, NOW()
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
ON CONFLICT DO NOTHING;

INSERT INTO positive_pay_items (file_id, payment_number, amount, payee_name, issue_date, status)
SELECT pf.id, 'CHQ-2026-' || gs, (gs * 5000.0),
  (ARRAY['Supplier A','Supplier B','Supplier C','Vendor D','Partner E'])[gs],
  CURRENT_DATE - (gs || ' days')::interval, 'ISSUED'
FROM (SELECT id FROM positive_pay_files LIMIT 5) pf
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM positive_pay_items WHERE file_id=pf.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── production_material_issues ────────────────────────────────
INSERT INTO production_material_issues (production_order_id, product_id, required_quantity, issued_quantity, warehouse_id, issued_by, issued_at)
SELECT po.id, p.id, (gs * 100.0), (gs * 98.0), w.id, 207, NOW()
FROM (SELECT id FROM production_orders LIMIT 5) po
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN (SELECT id FROM warehouses LIMIT 1) w
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM production_material_issues WHERE production_order_id=po.id AND product_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── fixed_asset tables ────────────────────────────────────────
INSERT INTO fixed_asset_books (asset_id, depreciation_book_id, original_cost, current_book_value, accumulated_depreciation, last_depreciation_date)
SELECT fa.id, 1, fa.acquisition_cost, fa.current_book_value, fa.accumulated_depreciation, CURRENT_DATE
FROM (SELECT id, acquisition_cost, current_book_value, accumulated_depreciation FROM fixed_assets LIMIT 5) fa
WHERE NOT EXISTS (SELECT 1 FROM fixed_asset_books WHERE asset_id=fa.id)
ON CONFLICT DO NOTHING;

INSERT INTO fixed_asset_depreciation_logs (asset_id, depreciation_date, depreciation_amount, book_value_after, method, created_at)
SELECT fa.id, DATE_TRUNC('month', NOW())::date, (fa.acquisition_cost * 0.05), (fa.acquisition_cost * 0.95), 'STRAIGHT_LINE', NOW()
FROM (SELECT id, acquisition_cost FROM fixed_assets LIMIT 5) fa
WHERE NOT EXISTS (SELECT 1 FROM fixed_asset_depreciation_logs WHERE asset_id=fa.id)
ON CONFLICT DO NOTHING;

INSERT INTO fixed_asset_history (asset_id, change_type, old_value, new_value, changed_by, changed_at)
SELECT fa.id, 'ACQUISITION', '0', fa.acquisition_cost::text, 207, NOW()
FROM (SELECT id, acquisition_cost FROM fixed_assets LIMIT 5) fa
WHERE NOT EXISTS (SELECT 1 FROM fixed_asset_history WHERE asset_id=fa.id)
ON CONFLICT DO NOTHING;

INSERT INTO fixed_asset_assignments (asset_id, employee_id, assigned_from, assigned_by, notes)
SELECT fa.id, 207, CURRENT_DATE, 207, 'Initial assignment'
FROM (SELECT id FROM fixed_assets LIMIT 5) fa
WHERE NOT EXISTS (SELECT 1 FROM fixed_asset_assignments WHERE asset_id=fa.id)
ON CONFLICT DO NOTHING;

-- budget sub-tables
INSERT INTO budget_lines (budget_id, account_id, cost_center_id, period, amount, currency_code)
SELECT b.id, 1, 1, '2026-' || lpad(gs::text,2,'0'), (gs * 50000.0), 'EUR'
FROM (SELECT id FROM budgets LIMIT 5) b
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM budget_lines WHERE budget_id=b.id AND period='2026-' || lpad(gs::text,2,'0'))
LIMIT 5
ON CONFLICT DO NOTHING;

INSERT INTO budget_versions (budget_id, version_number, version_note, created_by, created_at)
SELECT b.id, 1, 'Initial version', 207, NOW()
FROM (SELECT id FROM budgets LIMIT 5) b
WHERE NOT EXISTS (SELECT 1 FROM budget_versions WHERE budget_id=b.id)
ON CONFLICT DO NOTHING;

INSERT INTO budget_approvals (budget_id, approved_by, approval_date, status, comments)
SELECT b.id, 207, CURRENT_DATE, 'APPROVED', 'Budget approved for FY2026'
FROM (SELECT id FROM budgets LIMIT 5) b
WHERE NOT EXISTS (SELECT 1 FROM budget_approvals WHERE budget_id=b.id)
ON CONFLICT DO NOTHING;

-- ── cash / treasury tables ────────────────────────────────────
INSERT INTO cash_pools (company_id, pool_name, pool_type, master_account_id, currency_code, active, created_at)
VALUES
  (1, 'Main EUR Pool',       'NOTIONAL', 1, 'EUR', true, NOW()),
  (1, 'USD Overlay Pool',    'PHYSICAL', 1, 'USD', true, NOW()),
  (1, 'GBP Regional Pool',   'NOTIONAL', 1, 'GBP', true, NOW()),
  (1, 'INR APAC Pool',       'PHYSICAL', 1, 'INR', true, NOW()),
  (1, 'Reserve Pool',        'NOTIONAL', 1, 'EUR', false, NOW())
ON CONFLICT DO NOTHING;

INSERT INTO cash_pool_members (pool_id, bank_account_id, participation_type, contribution_limit, sweep_threshold)
SELECT cp.id, ba.id, 'FULL', 100000.0, 10000.0
FROM (SELECT id FROM cash_pools LIMIT 5) cp
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
WHERE NOT EXISTS (SELECT 1 FROM cash_pool_members WHERE pool_id=cp.id AND bank_account_id=ba.id)
ON CONFLICT DO NOTHING;

INSERT INTO cash_transfers (company_id, from_account_id, to_account_id, amount, currency_code, transfer_date, status, reference, created_by, created_at)
VALUES
  (1, 1, 1, 50000.00, 'EUR', CURRENT_DATE, 'COMPLETED', 'CASH-TXN-001', 207, NOW()),
  (1, 1, 1, 25000.00, 'EUR', CURRENT_DATE, 'COMPLETED', 'CASH-TXN-002', 207, NOW()),
  (1, 1, 1, 75000.00, 'EUR', CURRENT_DATE, 'PENDING',   'CASH-TXN-003', 207, NOW()),
  (1, 1, 1, 10000.00, 'EUR', CURRENT_DATE, 'COMPLETED', 'CASH-TXN-004', 207, NOW()),
  (1, 1, 1, 30000.00, 'EUR', CURRENT_DATE, 'PENDING',   'CASH-TXN-005', 207, NOW())
ON CONFLICT DO NOTHING;

-- ── treasury tables ────────────────────────────────────────────
INSERT INTO treasury_investments (company_id, bank_account_id, investment_type, amount, currency_code, start_date, maturity_date, interest_rate, status)
SELECT 1, ba.id,
  (ARRAY['TERM_DEPOSIT','MONEY_MARKET','BOND','T_BILL','REPO'])[gs],
  (gs * 100000.0), 'EUR', CURRENT_DATE, CURRENT_DATE + (gs * 90 || ' days')::interval,
  (gs * 0.5), 'ACTIVE'
FROM generate_series(1,5) gs
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
ON CONFLICT DO NOTHING;

INSERT INTO treasury_risk_policies (company_id, policy_name, risk_type, limit_amount, limit_percent, currency_code, active, effective_from)
VALUES
  (1, 'FX Risk Policy',        'MARKET', 500.00, 5.00, 'EUR', true, CURRENT_DATE),
  (1, 'Counterparty Policy',   'CREDIT', 750.00, 7.50, 'EUR', true, CURRENT_DATE),
  (1, 'Liquidity Policy',      'LIQUIDITY', 250.00, 2.50, 'EUR', true, CURRENT_DATE),
  (1, 'Concentration Policy',  'CONCENTRATION', 900.00, 9.00, 'EUR', true, CURRENT_DATE),
  (1, 'Interest Rate Policy',  'INTEREST', 100.00, 1.00, 'EUR', false, CURRENT_DATE)
ON CONFLICT DO NOTHING;

INSERT INTO treasury_authorization_matrices (company_id, min_amount, max_amount, required_role, dual_approval, created_at)
VALUES
  (1,       0.00,   5000.00, 'ACCOUNTANT',    false, NOW()),
  (1,    5000.01,  25000.00, 'SUPERVISOR',    false, NOW()),
  (1,   25000.01, 100000.00, 'MGR',           true,  NOW()),
  (1,  100000.01, 500000.00, 'CFO',           true,  NOW()),
  (1,  500000.01, 999999.00, 'CEO',           true,  NOW())
ON CONFLICT DO NOTHING;

-- ── fx_deal_settlements ───────────────────────────────────────
INSERT INTO fx_deal_settlements (deal_id, settlement_date, settlement_amount, settlement_currency, bank_account_id, status)
SELECT d.id, CURRENT_DATE + '2 days'::interval, (gs * 50000.0), 'EUR', 1, 'PENDING'
FROM (SELECT id FROM fx_deals LIMIT 5) d
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM fx_deal_settlements WHERE deal_id=d.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── bi tables ──────────────────────────────────────────────────
INSERT INTO bi_ai_insight_generation (insight_id, model_version, prompt_tokens, completion_tokens, generation_time_ms, status, generated_at)
SELECT bi.id, 'gpt-4o', 1000, 500, 1200, 'SUCCESS', NOW()
FROM (SELECT id FROM bi_insights LIMIT 5) bi
WHERE NOT EXISTS (SELECT 1 FROM bi_ai_insight_generation WHERE insight_id=bi.id)
ON CONFLICT DO NOTHING;

INSERT INTO bi_forecast_prediction (run_id, entity_type, entity_id, period, predicted_value, confidence_interval_low, confidence_interval_high, model_used, predicted_at)
SELECT fr.id, 'PRODUCT', p.id, '2026-' || lpad(gs::text,2,'0'), (gs * 15000.0), (gs * 12000.0), (gs * 18000.0), 'PROPHET', NOW()
FROM (SELECT id FROM bi_forecast_runs LIMIT 1) fr
CROSS JOIN (SELECT id FROM products LIMIT 5) p
CROSS JOIN generate_series(1,5) gs
WHERE NOT EXISTS (SELECT 1 FROM bi_forecast_prediction WHERE run_id=fr.id AND entity_id=p.id)
LIMIT 5
ON CONFLICT DO NOTHING;

-- ── tax_registrations ─────────────────────────────────────────
-- entity_type must match the check constraint
INSERT INTO tax_registrations (company_id, entity_type, tax_authority_id, tax_scheme, registration_number, tax_office, filing_frequency, currency_code, is_auto_file, status, effective_from, effective_to, requires_e_filing)
VALUES
  (1, 'COMPANY', (SELECT id FROM tax_categories LIMIT 1), 'VAT', 'REG-2026-001', 'Paris Tax Office', 'MONTHLY',   'EUR', false, 'ACTIVE',  '2026-01-01', '2026-12-31', true),
  (1, 'COMPANY', (SELECT id FROM tax_categories LIMIT 1), 'CIT', 'REG-2026-002', 'DGFIP HQ',         'ANNUAL',    'EUR', false, 'ACTIVE',  '2026-01-01', '2026-12-31', true),
  (1, 'COMPANY', (SELECT id FROM tax_categories LIMIT 1), 'GST', 'REG-2026-003', 'Lyon Tax Centre',  'QUARTERLY', 'EUR', true,  'ACTIVE',  '2026-01-01', '2026-12-31', false),
  (1, 'COMPANY', (SELECT id FROM tax_categories LIMIT 1), 'WHT', 'REG-2026-004', 'Bordeaux Office',  'MONTHLY',   'EUR', true,  'PENDING', '2026-07-01', '2026-12-31', false),
  (1, 'COMPANY', (SELECT id FROM tax_categories LIMIT 1), 'CET', 'REG-2026-005', 'National HQ',      'ANNUAL',    'EUR', false, 'ACTIVE',  '2026-01-01', '2026-12-31', true)
ON CONFLICT DO NOTHING;

-- ── cash_position_snapshot_lines ──────────────────────────────
INSERT INTO cash_position_snapshot_lines (snapshot_id, bank_account_id, opening_balance, closing_balance, inflows, outflows, currency_code)
SELECT cps.id, ba.id, 500000.0, 480000.0, 50000.0, 70000.0, 'EUR'
FROM (SELECT id FROM cash_position_snapshots LIMIT 5) cps
CROSS JOIN (SELECT id FROM bank_accounts LIMIT 1) ba
WHERE NOT EXISTS (SELECT 1 FROM cash_position_snapshot_lines WHERE snapshot_id=cps.id AND bank_account_id=ba.id)
ON CONFLICT DO NOTHING;

-- Reset replication role
SET session_replication_role = DEFAULT;
