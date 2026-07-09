-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 52
-- File              : V52__create_sales_customers.sql
-- Operation Type    : Schema Creation
-- Purpose           : create sales customers
--
-- Tables Created    : customers
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_customers_company, idx_customers_status
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V52__create_sales_customers.sql
-- PLUS33 ERP — Sales Customer Schema
-- ============================================================

CREATE SEQUENCE customer_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    customer_type VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    contact_person VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(30),
    billing_address TEXT,
    shipping_address TEXT,
    tax_number VARCHAR(100),
    tax_profile VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    credit_limit DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    outstanding_balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    pricing_tier VARCHAR(50) NOT NULL DEFAULT 'RETAIL',
    discount_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    payment_terms_days INTEGER NOT NULL DEFAULT 0,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'INR',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_customers_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT uk_customer_company_code
        UNIQUE (company_id, code),

    CONSTRAINT uk_customer_company_email
        UNIQUE (company_id, email),

    CONSTRAINT chk_customer_credit_limit
        CHECK (credit_limit >= 0.00),

    CONSTRAINT chk_customer_outstanding_balance
        CHECK (outstanding_balance >= 0.00),

    CONSTRAINT chk_customer_discount_rate
        CHECK (discount_rate >= 0.00 AND discount_rate <= 100.00),

    CONSTRAINT chk_customer_payment_terms
        CHECK (payment_terms_days >= 0)
);

CREATE INDEX idx_customers_company ON customers(company_id);
CREATE INDEX idx_customers_status ON customers(status);
