-- ============================================================
-- V38__create_procurement_analytics.sql
-- PLUS33 ERP — Procurement Analytics & Dashboards Materialized Views
-- ============================================================

-- 1. Create permissions and assign to roles
INSERT INTO permissions (code, name, description)
VALUES ('ANALYTICS_VIEW', 'View Analytics', 'Allows viewing analytics dashboards')
ON CONFLICT (code) DO NOTHING;

INSERT INTO roles (code, name, description) VALUES
('ULTIMATE_ADMIN', 'Ultimate Admin', 'Full system access'),
('REGIONAL_MANAGER', 'Regional Manager', 'Regional management access'),
('PROCUREMENT_MANAGER', 'Procurement Manager', 'Procurement operations management'),
('FINANCE_MANAGER', 'Finance Manager', 'Finance operations management'),
('WAREHOUSE_MANAGER', 'Warehouse Manager', 'Warehouse operations management')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'PROCUREMENT_MANAGER', 'FINANCE_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code = 'ANALYTICS_VIEW'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 2. Materialized Views (WITH NO DATA)

-- View 1: mv_procurement_summary
CREATE MATERIALIZED VIEW mv_procurement_summary AS
WITH pr_stats AS (
    SELECT 
        company_id,
        COUNT(*) AS total_purchase_requests,
        AVG(EXTRACT(EPOCH FROM (approved_at - requested_at)) / 86400.0) AS avg_pr_cycle_time_days
    FROM purchase_requests
    WHERE status IN ('APPROVED', 'CONVERTED_TO_PO') AND approved_at IS NOT NULL
    GROUP BY company_id
),
po_stats AS (
    SELECT 
        company_id,
        COUNT(*) AS total_purchase_orders,
        SUM(total_amount) AS total_spend
    FROM purchase_orders
    WHERE status <> 'CANCELLED'
    GROUP BY company_id
),
gr_stats AS (
    SELECT 
        company_id,
        COUNT(*) AS total_goods_receipts
    FROM goods_receipts
    WHERE status = 'COMPLETED'
    GROUP BY company_id
),
si_stats AS (
    SELECT 
        company_id,
        COUNT(*) AS total_supplier_invoices
    FROM supplier_invoices
    WHERE status <> 'CANCELLED'
    GROUP BY company_id
),
pay_stats AS (
    SELECT 
        company_id,
        COUNT(*) AS total_payments
    FROM payments
    WHERE status = 'COMPLETED'
    GROUP BY company_id
)
SELECT 
    c.id AS company_id,
    COALESCE(pr.total_purchase_requests, 0) AS total_purchase_requests,
    COALESCE(po.total_purchase_orders, 0) AS total_purchase_orders,
    COALESCE(gr.total_goods_receipts, 0) AS total_goods_receipts,
    COALESCE(si.total_supplier_invoices, 0) AS total_supplier_invoices,
    COALESCE(pay.total_payments, 0) AS total_payments,
    COALESCE(po.total_spend, 0.00) AS total_spend,
    COALESCE(pr.avg_pr_cycle_time_days, 0.00) AS avg_pr_cycle_time_days
FROM companies c
LEFT JOIN pr_stats pr ON pr.company_id = c.id
LEFT JOIN po_stats po ON po.company_id = c.id
LEFT JOIN gr_stats gr ON gr.company_id = c.id
LEFT JOIN si_stats si ON si.company_id = c.id
LEFT JOIN pay_stats pay ON pay.company_id = c.id
WITH NO DATA;

-- View 2: mv_supplier_performance
CREATE MATERIALIZED VIEW mv_supplier_performance AS
WITH po_metrics AS (
    SELECT 
        company_id,
        supplier_id,
        COUNT(*) AS total_orders,
        SUM(total_amount) AS total_spend,
        COUNT(CASE WHEN status IN ('RECEIVED', 'CLOSED') AND (received_at IS NULL OR expected_delivery_date IS NULL OR DATE(received_at) <= expected_delivery_date) THEN 1 END) * 100.0 / NULLIF(COUNT(CASE WHEN status IN ('RECEIVED', 'CLOSED') THEN 1 END), 0) AS on_time_delivery_rate,
        AVG(CASE WHEN status IN ('RECEIVED', 'CLOSED') AND received_at IS NOT NULL THEN EXTRACT(EPOCH FROM (received_at - created_at)) / 86400.0 END) AS avg_lead_time_days
    FROM purchase_orders
    WHERE status <> 'CANCELLED'
    GROUP BY company_id, supplier_id
)
SELECT 
    po.company_id,
    po.supplier_id,
    s.name AS supplier_name,
    po.total_orders,
    COALESCE(po.total_spend, 0.00) AS total_spend,
    COALESCE(po.on_time_delivery_rate, 100.00) AS on_time_delivery_rate,
    COALESCE(po.avg_lead_time_days, 0.00) AS avg_lead_time_days
FROM po_metrics po
JOIN suppliers s ON s.id = po.supplier_id
WITH NO DATA;

-- View 3: mv_payables_aging
CREATE MATERIALIZED VIEW mv_payables_aging AS
SELECT 
    si.company_id,
    si.supplier_id,
    s.name AS supplier_name,
    SUM(si.outstanding_balance) AS total_outstanding,
    SUM(CASE WHEN si.due_date >= CURRENT_DATE OR si.due_date IS NULL THEN si.outstanding_balance ELSE 0.00 END) AS aging_current,
    SUM(CASE WHEN si.due_date < CURRENT_DATE AND si.due_date >= CURRENT_DATE - 30 THEN si.outstanding_balance ELSE 0.00 END) AS aging_1_30,
    SUM(CASE WHEN si.due_date < CURRENT_DATE - 30 AND si.due_date >= CURRENT_DATE - 60 THEN si.outstanding_balance ELSE 0.00 END) AS aging_31_60,
    SUM(CASE WHEN si.due_date < CURRENT_DATE - 60 AND si.due_date >= CURRENT_DATE - 90 THEN si.outstanding_balance ELSE 0.00 END) AS aging_61_90,
    SUM(CASE WHEN si.due_date < CURRENT_DATE - 90 THEN si.outstanding_balance ELSE 0.00 END) AS aging_90_plus
FROM supplier_invoices si
JOIN suppliers s ON s.id = si.supplier_id
WHERE si.status IN ('APPROVED', 'PARTIALLY_PAID') AND si.outstanding_balance > 0
GROUP BY si.company_id, si.supplier_id, s.name
WITH NO DATA;

-- View 4: mv_po_fulfilment
CREATE MATERIALIZED VIEW mv_po_fulfilment AS
SELECT 
    po.company_id,
    po.id AS purchase_order_id,
    po.order_number,
    s.name AS supplier_name,
    po.status,
    po.total_amount,
    po.expected_delivery_date,
    COUNT(poi.id) AS total_items_ordered,
    SUM(poi.ordered_quantity) AS total_quantity_ordered,
    SUM(poi.received_quantity) AS total_quantity_received,
    CASE 
        WHEN SUM(poi.ordered_quantity) > 0 THEN 
            (SUM(poi.received_quantity) / SUM(poi.ordered_quantity)) * 100.0 
        ELSE 0.00 
    END AS fulfillment_rate
FROM purchase_orders po
JOIN suppliers s ON s.id = po.supplier_id
LEFT JOIN purchase_order_items poi ON poi.purchase_order_id = po.id
WHERE po.status <> 'CANCELLED'
GROUP BY po.company_id, po.id, po.order_number, s.name, po.status, po.total_amount, po.expected_delivery_date
WITH NO DATA;

-- View 5: mv_invoice_matching
CREATE MATERIALIZED VIEW mv_invoice_matching AS
WITH invoice_item_checks AS (
    SELECT 
        sii.supplier_invoice_id,
        MAX(CASE 
            WHEN sii.quantity <> poi.ordered_quantity 
                 OR (sii.goods_receipt_item_id IS NOT NULL AND sii.quantity > COALESCE(gri.received_quantity, 0))
            THEN 1 ELSE 0 
        END) AS qty_mismatch_flag,
        MAX(CASE WHEN sii.unit_price <> poi.unit_price THEN 1 ELSE 0 END) AS price_mismatch_flag
    FROM supplier_invoice_items sii
    JOIN purchase_order_items poi ON poi.id = sii.purchase_order_item_id
    LEFT JOIN goods_receipt_items gri ON gri.id = sii.goods_receipt_item_id
    GROUP BY sii.supplier_invoice_id
)
SELECT 
    si.company_id,
    si.id AS supplier_invoice_id,
    si.invoice_number,
    po.id AS purchase_order_id,
    po.order_number,
    s.name AS supplier_name,
    si.total_amount AS invoice_total_amount,
    po.total_amount AS po_total_amount,
    COALESCE(c.qty_mismatch_flag, 0) = 1 AS has_quantity_mismatch,
    COALESCE(c.price_mismatch_flag, 0) = 1 AS has_price_mismatch
FROM supplier_invoices si
JOIN purchase_orders po ON po.id = si.purchase_order_id
JOIN suppliers s ON s.id = si.supplier_id
LEFT JOIN invoice_item_checks c ON c.supplier_invoice_id = si.id
WHERE si.status <> 'CANCELLED'
WITH NO DATA;

-- 3. Unique Indexes for Concurrent Refresh
CREATE UNIQUE INDEX uidx_mv_procurement_summary ON mv_procurement_summary (company_id);
CREATE UNIQUE INDEX uidx_mv_supplier_performance ON mv_supplier_performance (company_id, supplier_id);
CREATE UNIQUE INDEX uidx_mv_payables_aging ON mv_payables_aging (company_id, supplier_id);
CREATE UNIQUE INDEX uidx_mv_po_fulfilment ON mv_po_fulfilment (purchase_order_id);
CREATE UNIQUE INDEX uidx_mv_invoice_matching ON mv_invoice_matching (supplier_invoice_id);
