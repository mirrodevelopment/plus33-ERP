-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 37
-- File              : V37__alter_payments_schema.sql
-- Operation Type    : Schema Alteration
-- Purpose           : alter payments schema
--
-- Tables Created    : N/A
-- Tables Altered    : payments, payments, payments, payments, payments, payments, payments, payments
-- Seed Data For     : permissions, role_permissions
-- Indexes           : IF, IF
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V37__alter_payments_schema.sql
-- PLUS33 ERP — Supplier Payments Schema Enhancements & Permissions
-- ============================================================

-- 1. Alter payments table
ALTER TABLE payments ADD COLUMN IF NOT EXISTS supplier_id BIGINT;
ALTER TABLE payments ADD COLUMN IF NOT EXISTS status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED';
ALTER TABLE payments ADD COLUMN IF NOT EXISTS cancelled_at TIMESTAMP;
ALTER TABLE payments ADD COLUMN IF NOT EXISTS cancelled_by BIGINT;
ALTER TABLE payments ADD COLUMN IF NOT EXISTS cancellation_reason TEXT;

-- 2. Add foreign keys and constraints
ALTER TABLE payments ADD CONSTRAINT fk_payments_supplier
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id);

ALTER TABLE payments ADD CONSTRAINT fk_payments_cancelled_by
    FOREIGN KEY (cancelled_by) REFERENCES users(id);

ALTER TABLE payments ADD CONSTRAINT chk_payment_status 
    CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED'));

CREATE SEQUENCE IF NOT EXISTS payment_seq START WITH 1 INCREMENT BY 1;

-- 3. Create performance index
CREATE INDEX IF NOT EXISTS idx_payments_supplier ON payments(supplier_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);

-- 4. Seed Permissions
INSERT INTO permissions (code, name, description)
VALUES
('PAYMENT_CREATE',   'Create Supplier Payments',    'Create and allocate new supplier payments'),
('PAYMENT_VIEW',     'View Supplier Payments',      'View details and allocations of supplier payments'),
('PAYMENT_UPDATE',   'Update Supplier Payments',    'Modify payment details'),
('PAYMENT_CANCEL',   'Cancel Supplier Payments',    'Cancel supplier payments and reverse allocations/journal entries');

-- 5. Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'PAYMENT_CREATE', 'PAYMENT_VIEW', 'PAYMENT_UPDATE', 'PAYMENT_CANCEL'
  );
