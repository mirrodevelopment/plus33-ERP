-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 58
-- File              : V58__create_customer_invoices.sql
-- Operation Type    : Schema Creation
-- Purpose           : create customer invoices
--
-- Tables Created    : customer_invoices, customer_invoice_items
-- Tables Altered    : payment_allocations, payment_allocations, payment_allocations, payment_allocations, payments, payments, payments
-- Seed Data For     : chart_of_accounts
-- Indexes           : idx_customer_invoice_status, idx_customer_invoice_company, idx_customer_invoice_customer, idx_customer_invoice_items_invoice
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V58__create_customer_invoices.sql
-- PLUS33 ERP — Customer Invoice Schema
-- ============================================================

CREATE SEQUENCE customer_invoice_seq START WITH 1 INCREMENT BY 1;

-- Seed Accounts Receivable (1400) under Assets (1000) for all companies
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '1400', 'Accounts Receivable', 'ASSET', parent.id, TRUE
FROM companies c
JOIN chart_of_accounts parent ON parent.company_id = c.id AND parent.account_code = '1000'
ON CONFLICT (company_id, account_code) DO NOTHING;

-- Seed Tax Payable (2200) under Liabilities (2000) for all companies
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '2200', 'Tax Payable', 'LIABILITY', parent.id, TRUE
FROM companies c
JOIN chart_of_accounts parent ON parent.company_id = c.id AND parent.account_code = '2000'
ON CONFLICT (company_id, account_code) DO NOTHING;

CREATE TABLE customer_invoices (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    sales_order_id BIGINT,
    invoice_number VARCHAR(50) NOT NULL,
    client_reference_id UUID NOT NULL,
    invoice_date DATE NOT NULL,
    due_date DATE,
    subtotal_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    paid_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    outstanding_balance DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    journal_entry_id BIGINT,
    created_by BIGINT NOT NULL,
    submitted_by BIGINT,
    approved_by BIGINT,
    cancelled_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP,
    approved_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_customer_invoices_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_customer_invoices_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_customer_invoices_order FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    CONSTRAINT fk_customer_invoices_journal FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id),
    CONSTRAINT fk_customer_invoices_created FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_customer_invoices_submitted FOREIGN KEY (submitted_by) REFERENCES users(id),
    CONSTRAINT fk_customer_invoices_approved FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_customer_invoices_cancelled FOREIGN KEY (cancelled_by) REFERENCES users(id),

    CONSTRAINT uk_customer_invoice_company_number UNIQUE (company_id, invoice_number),
    CONSTRAINT uk_customer_invoice_client_reference UNIQUE (company_id, client_reference_id),
    CONSTRAINT uk_customer_invoice_journal_entry UNIQUE (journal_entry_id),
    
    CONSTRAINT chk_customer_invoice_date CHECK (due_date IS NULL OR due_date >= invoice_date),
    CONSTRAINT chk_customer_invoice_status CHECK (
        status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'PARTIALLY_PAID', 'PAID', 'CANCELLED', 'VOID')
    )
);

CREATE TABLE customer_invoice_items (
    id BIGSERIAL PRIMARY KEY,
    customer_invoice_id BIGINT NOT NULL,
    sales_order_item_id BIGINT,
    pick_list_item_id BIGINT,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    tax_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    net_amount DECIMAL(14,2) NOT NULL,
    tax_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(14,2) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_invoice_items_invoice FOREIGN KEY (customer_invoice_id) REFERENCES customer_invoices(id) ON DELETE CASCADE,
    CONSTRAINT fk_invoice_items_so_item FOREIGN KEY (sales_order_item_id) REFERENCES sales_order_items(id),
    CONSTRAINT fk_invoice_items_pick_item FOREIGN KEY (pick_list_item_id) REFERENCES pick_list_items(id),
    CONSTRAINT fk_invoice_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    
    CONSTRAINT chk_cust_invoice_item_qty CHECK (quantity > 0.00),
    CONSTRAINT chk_cust_invoice_item_price CHECK (unit_price >= 0.00),
    CONSTRAINT uk_customer_invoice_item_so_item UNIQUE (customer_invoice_id, sales_order_item_id) DEFERRABLE INITIALLY DEFERRED
);

-- Alter payment_allocations to make supplier_invoice_id nullable and add customer_invoice_id
ALTER TABLE payment_allocations ALTER COLUMN supplier_invoice_id DROP NOT NULL;
ALTER TABLE payment_allocations ADD COLUMN customer_invoice_id BIGINT;
ALTER TABLE payment_allocations ADD CONSTRAINT fk_payment_allocations_cust_invoice 
    FOREIGN KEY (customer_invoice_id) REFERENCES customer_invoices(id);
ALTER TABLE payment_allocations ADD CONSTRAINT chk_payment_allocation_target 
    CHECK (
        (supplier_invoice_id IS NOT NULL AND customer_invoice_id IS NULL)
        OR
        (supplier_invoice_id IS NULL AND customer_invoice_id IS NOT NULL)
    );

-- Alter payments to add customer_id and update check constraints
ALTER TABLE payments ADD COLUMN customer_id BIGINT;
ALTER TABLE payments ADD CONSTRAINT fk_payments_customer 
    FOREIGN KEY (customer_id) REFERENCES customers(id);
ALTER TABLE payments ADD CONSTRAINT chk_payment_partner
    CHECK (
        (supplier_id IS NOT NULL AND customer_id IS NULL)
        OR
        (supplier_id IS NULL AND customer_id IS NOT NULL)
    );

-- Validation trigger for customer invoices (cross-company, tenant security)
CREATE OR REPLACE FUNCTION validate_customer_invoice()
RETURNS TRIGGER AS $$
DECLARE
    so_company_id BIGINT;
    so_customer_id BIGINT;
    cust_company_id BIGINT;
    je_company_id BIGINT;
BEGIN
    -- Verify customer company
    SELECT company_id INTO cust_company_id FROM customers WHERE id = NEW.customer_id;
    IF cust_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Customer company does not match invoice company';
    END IF;

    -- Verify Sales Order company and customer
    IF NEW.sales_order_id IS NOT NULL THEN
        SELECT company_id, customer_id INTO so_company_id, so_customer_id 
        FROM sales_orders 
        WHERE id = NEW.sales_order_id;
        
        IF so_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Invoice company does not match sales order company';
        END IF;
        
        IF so_customer_id <> NEW.customer_id THEN
            RAISE EXCEPTION 'Invoice customer does not match sales order customer';
        END IF;
    END IF;

    -- Verify Journal Entry company
    IF NEW.journal_entry_id IS NOT NULL THEN
        SELECT company_id INTO je_company_id FROM journal_entries WHERE id = NEW.journal_entry_id;
        IF je_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Journal entry company does not match invoice company';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_customer_invoice
BEFORE INSERT OR UPDATE ON customer_invoices
FOR EACH ROW
EXECUTE FUNCTION validate_customer_invoice();

-- Materialized Views for Sales Analytics
CREATE MATERIALIZED VIEW mv_sales_kpis AS
SELECT 
    company_id,
    COUNT(*) AS total_invoices,
    SUM(total_amount) AS total_invoiced_amount,
    SUM(paid_amount) AS total_paid_amount,
    SUM(outstanding_balance) AS total_outstanding_amount
FROM customer_invoices
WHERE status NOT IN ('CANCELLED', 'VOID')
GROUP BY company_id
WITH NO DATA;

CREATE MATERIALIZED VIEW mv_receivables_dashboard AS
SELECT 
    company_id,
    status,
    COUNT(*) AS invoice_count,
    SUM(total_amount) AS total_amount,
    SUM(outstanding_balance) AS outstanding_amount
FROM customer_invoices
GROUP BY company_id, status
WITH NO DATA;

CREATE MATERIALIZED VIEW mv_customer_aging AS
SELECT 
    ci.company_id,
    ci.customer_id,
    c.name AS customer_name,
    SUM(ci.outstanding_balance) AS total_outstanding,
    SUM(CASE WHEN ci.due_date >= CURRENT_DATE OR ci.due_date IS NULL THEN ci.outstanding_balance ELSE 0.00 END) AS aging_current,
    SUM(CASE WHEN ci.due_date < CURRENT_DATE AND ci.due_date >= CURRENT_DATE - 30 THEN ci.outstanding_balance ELSE 0.00 END) AS aging_1_30,
    SUM(CASE WHEN ci.due_date < CURRENT_DATE - 30 AND ci.due_date >= CURRENT_DATE - 60 THEN ci.outstanding_balance ELSE 0.00 END) AS aging_31_60,
    SUM(CASE WHEN ci.due_date < CURRENT_DATE - 60 AND ci.due_date >= CURRENT_DATE - 90 THEN ci.outstanding_balance ELSE 0.00 END) AS aging_61_90,
    SUM(CASE WHEN ci.due_date < CURRENT_DATE - 90 THEN ci.outstanding_balance ELSE 0.00 END) AS aging_90_plus
FROM customer_invoices ci
JOIN customers c ON c.id = ci.customer_id
WHERE ci.status IN ('APPROVED', 'PARTIALLY_PAID') AND ci.outstanding_balance > 0
GROUP BY ci.company_id, ci.customer_id, c.name
WITH NO DATA;

CREATE INDEX idx_customer_invoice_status ON customer_invoices(status);
CREATE INDEX idx_customer_invoice_company ON customer_invoices(company_id);
CREATE INDEX idx_customer_invoice_customer ON customer_invoices(customer_id);
CREATE INDEX idx_customer_invoice_items_invoice ON customer_invoice_items(customer_invoice_id);
