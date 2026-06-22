-- ============================================================
-- V54__create_sales_orders.sql
-- PLUS33 ERP — Sales Order Schema
-- ============================================================

CREATE SEQUENCE sales_order_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE sales_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    order_number VARCHAR(50) NOT NULL,
    client_reference_id UUID NOT NULL,
    order_date DATE NOT NULL,
    requested_delivery_date DATE,
    currency_code VARCHAR(3) NOT NULL,
    payment_terms_days INTEGER NOT NULL DEFAULT 0,
    billing_address TEXT NOT NULL,
    shipping_address TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    
    -- Snapshot fields
    customer_name VARCHAR(150) NOT NULL,
    customer_code VARCHAR(50) NOT NULL,
    customer_type VARCHAR(30) NOT NULL,
    pricing_tier VARCHAR(50) NOT NULL,
    discount_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    tax_profile VARCHAR(50) NOT NULL,
    
    -- Monetary fields
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    outstanding_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    credit_override BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Audit fields
    ordered_by BIGINT NOT NULL,
    submitted_by BIGINT,
    approved_by BIGINT,
    cancelled_by BIGINT,
    submitted_at TIMESTAMP,
    approved_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_sales_orders_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_sales_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_sales_orders_ordered_by FOREIGN KEY (ordered_by) REFERENCES users(id),
    CONSTRAINT fk_sales_orders_submitted_by FOREIGN KEY (submitted_by) REFERENCES users(id),
    CONSTRAINT fk_sales_orders_approved_by FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_sales_orders_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(id),
    
    -- Constraints
    CONSTRAINT uk_sales_order_company_number UNIQUE (company_id, order_number),
    CONSTRAINT uk_sales_order_client_reference UNIQUE (company_id, client_reference_id),
    CONSTRAINT chk_sales_order_status CHECK (
        status IN (
            'DRAFT', 'SUBMITTED', 'APPROVED', 'PARTIALLY_FULFILLED', 'FULFILLED', 'INVOICED', 'CLOSED', 'CANCELLED'
        )
    ),
    CONSTRAINT chk_sales_order_subtotal CHECK (subtotal >= 0.00),
    CONSTRAINT chk_sales_order_discount_amount CHECK (discount_amount >= 0.00),
    CONSTRAINT chk_sales_order_tax_amount CHECK (tax_amount >= 0.00),
    CONSTRAINT chk_sales_order_total_amount CHECK (total_amount >= 0.00),
    CONSTRAINT chk_sales_order_outstanding_amount CHECK (outstanding_amount >= 0.00)
);

CREATE TABLE sales_order_items (
    id BIGSERIAL PRIMARY KEY,
    sales_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    ordered_quantity DECIMAL(12,2) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    tax_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    line_total DECIMAL(12,2) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_sales_items_order FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_sales_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uq_sales_order_item_product UNIQUE (sales_order_id, product_id) DEFERRABLE INITIALLY DEFERRED,
    
    -- Constraints
    CONSTRAINT chk_sales_item_quantity CHECK (ordered_quantity > 0.00),
    CONSTRAINT chk_sales_item_price CHECK (unit_price >= 0.00),
    CONSTRAINT chk_sales_item_discount CHECK (discount_percentage BETWEEN 0.00 AND 100.00),
    CONSTRAINT chk_sales_item_tax CHECK (tax_percentage BETWEEN 0.00 AND 100.00),
    CONSTRAINT chk_sales_item_line_total CHECK (line_total >= 0.00)
);

CREATE INDEX idx_sales_orders_company ON sales_orders(company_id);
CREATE INDEX idx_sales_orders_customer ON sales_orders(customer_id);
CREATE INDEX idx_sales_orders_status ON sales_orders(status);
CREATE INDEX idx_sales_items_order ON sales_order_items(sales_order_id);
