-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 75
-- File              : V75__fixed_asset_enterprise_refinements.sql
-- Operation Type    : Schema Creation
-- Purpose           : fixed asset enterprise refinements
--
-- Tables Created    : depreciation_books, fixed_asset_books, fixed_asset_revaluations, fixed_asset_impairments, fixed_asset_leases, fixed_asset_relations, fixed_asset_maintenance_plans, fixed_asset_utilization, fixed_asset_attachments, fixed_asset_attachment_versions, fixed_asset_reservations, fixed_asset_insurance_claims, fixed_asset_downtimes, fixed_asset_work_orders, fixed_asset_notifications, fixed_asset_fx_logs, fixed_asset_history
-- Tables Altered    : fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, fixed_assets, asset_categories, fixed_asset_transfers, fixed_asset_transfers, fixed_asset_transfers, fixed_asset_transfers, fixed_asset_audits, fixed_asset_audits, fixed_asset_audit_items, fixed_asset_audit_items
-- Seed Data For     : depreciation_books, permissions, role_permissions
-- Indexes           : idx_fa_books_lookup, idx_fa_history_timeline, idx_fa_relations_lookup, idx_fa_maint_plan_date, idx_fa_utilization_lookup, idx_fa_attachments_lookup, idx_fa_reservations_lookup, idx_fa_downtimes_lookup, idx_fa_work_orders_lookup, idx_fa_notifications_lookup
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V75__fixed_asset_enterprise_refinements.sql
-- PLUS33 ERP — Fixed Asset Management Enterprise Refinements
-- ============================================================

-- 1. Alter fixed_assets and asset_categories tables
ALTER TABLE fixed_assets ADD COLUMN is_cwip BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_assets ADD COLUMN cwip_account_id BIGINT;
ALTER TABLE fixed_assets ADD COLUMN budgeted_cost DECIMAL(15,2) DEFAULT 0.00;
ALTER TABLE fixed_assets ADD COLUMN actual_cost DECIMAL(15,2) DEFAULT 0.00;
ALTER TABLE fixed_assets ADD COLUMN barcode_or_nfc_tag VARCHAR(100);
ALTER TABLE fixed_assets ADD COLUMN warranty_type VARCHAR(50);
ALTER TABLE fixed_assets ADD COLUMN warranty_covered_components TEXT;
ALTER TABLE fixed_assets ADD COLUMN warranty_service_contact VARCHAR(100);

-- Health Score & Replacement Planning
ALTER TABLE fixed_assets ADD COLUMN health_score INTEGER DEFAULT 100;

-- GIS Location Mapping
ALTER TABLE fixed_assets ADD COLUMN latitude DECIMAL(9,6);
ALTER TABLE fixed_assets ADD COLUMN longitude DECIMAL(9,6);
ALTER TABLE fixed_assets ADD COLUMN site VARCHAR(100);
ALTER TABLE fixed_assets ADD COLUMN building VARCHAR(100);
ALTER TABLE fixed_assets ADD COLUMN floor VARCHAR(50);
ALTER TABLE fixed_assets ADD COLUMN room VARCHAR(50);
ALTER TABLE fixed_assets ADD COLUMN region VARCHAR(100);

-- IoT Integration Telemetry (Readiness)
ALTER TABLE fixed_assets ADD COLUMN sensor_id VARCHAR(100);
ALTER TABLE fixed_assets ADD COLUMN device_id VARCHAR(100);
ALTER TABLE fixed_assets ADD COLUMN last_heartbeat TIMESTAMP;
ALTER TABLE fixed_assets ADD COLUMN telemetry_temp DECIMAL(5,2);
ALTER TABLE fixed_assets ADD COLUMN telemetry_vibration DECIMAL(5,2);
ALTER TABLE fixed_assets ADD COLUMN telemetry_runtime_hours DECIMAL(12,2) DEFAULT 0.00;

-- Multi-Currency Support
ALTER TABLE fixed_assets ADD COLUMN acquisition_currency VARCHAR(3) DEFAULT 'AED';
ALTER TABLE fixed_assets ADD COLUMN functional_currency VARCHAR(3) DEFAULT 'AED';
ALTER TABLE fixed_assets ADD COLUMN historical_exchange_rate DECIMAL(18,6) DEFAULT 1.000000;
ALTER TABLE fixed_assets ADD COLUMN reporting_currency VARCHAR(3) DEFAULT 'AED';

-- Disaster Recovery & Compliance
ALTER TABLE fixed_assets ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_assets ADD COLUMN is_legal_hold BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_assets ADD COLUMN retention_expiry_date DATE;

-- Adjust status check constraint on fixed_assets to include new enterprise statuses
ALTER TABLE fixed_assets DROP CONSTRAINT chk_asset_status;
ALTER TABLE fixed_assets ADD CONSTRAINT chk_asset_status CHECK (status IN (
    'DRAFT', 'UNDER_CONSTRUCTION', 'ACTIVE', 'UNDER_MAINTENANCE', 
    'TRANSFERRED', 'RETIRED', 'DISPOSAL_REQUESTED', 'DISPOSED', 
    'WRITTEN_OFF', 'LOST', 'STOLEN', 'SPLIT', 'MERGED', 'EXPENSED'
));

ALTER TABLE fixed_assets ADD CONSTRAINT fk_fixed_assets_cwip_acc FOREIGN KEY (cwip_account_id) REFERENCES chart_of_accounts(id);

ALTER TABLE asset_categories ADD COLUMN capitalization_threshold DECIMAL(15,2) DEFAULT 0.00;

-- 2. Alter fixed_asset_transfers to support approval workflow and inter-company transfers
ALTER TABLE fixed_asset_transfers ADD COLUMN status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED';
ALTER TABLE fixed_asset_transfers ADD COLUMN to_company_id BIGINT;
ALTER TABLE fixed_asset_transfers ADD CONSTRAINT fk_transfers_to_company FOREIGN KEY (to_company_id) REFERENCES companies(id);
ALTER TABLE fixed_asset_transfers ADD CONSTRAINT chk_transfer_status CHECK (status IN ('REQUESTED', 'MANAGER_APPROVED', 'IN_TRANSIT', 'RECEIVED', 'ACTIVE'));

-- 3. Create depreciation_books metadata table
CREATE TABLE depreciation_books (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_depr_books_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_depr_books_company_code UNIQUE (company_id, code)
);

-- Seed standard depreciation books for all active companies
INSERT INTO depreciation_books (company_id, code, name)
SELECT id, 'FINANCIAL', 'Financial Accounting Book (GL Posted)' FROM companies;
INSERT INTO depreciation_books (company_id, code, name)
SELECT id, 'TAX', 'Tax Depreciation Book (Memo Only)' FROM companies;
INSERT INTO depreciation_books (company_id, code, name)
SELECT id, 'MANAGEMENT', 'Management / Internal Book (Memo Only)' FROM companies;

-- 4. Create fixed_asset_books join table for multi-book/multi-frequency support
CREATE TABLE fixed_asset_books (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    depreciation_book_id BIGINT NOT NULL,
    depreciation_method VARCHAR(30) NOT NULL, -- STRAIGHT_LINE, WDV, USAGE, NONE
    depreciation_frequency VARCHAR(30) NOT NULL DEFAULT 'MONTHLY', -- MONTHLY, QUARTERLY, HALF_YEARLY, YEARLY
    depreciation_rate DECIMAL(5,2) NOT NULL,
    useful_life_years INTEGER NOT NULL,
    salvage_value DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    original_cost DECIMAL(15,2) NOT NULL,
    current_book_value DECIMAL(15,2) NOT NULL,
    accumulated_depreciation DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    last_depreciation_date DATE,

    CONSTRAINT fk_fa_books_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_fa_books_book FOREIGN KEY (depreciation_book_id) REFERENCES depreciation_books(id),
    CONSTRAINT uk_fa_books_asset_book UNIQUE (fixed_asset_id, depreciation_book_id),
    CONSTRAINT chk_depr_freq CHECK (depreciation_frequency IN ('MONTHLY', 'QUARTERLY', 'HALF_YEARLY', 'YEARLY'))
);

-- 5. Create fixed_asset_revaluations table
CREATE TABLE fixed_asset_revaluations (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    revaluation_date DATE NOT NULL,
    previous_value DECIMAL(15,2) NOT NULL,
    new_fair_value DECIMAL(15,2) NOT NULL,
    revaluation_reserve_account_id BIGINT NOT NULL,
    journal_entry_id BIGINT, -- NULL for non-financial books
    reason VARCHAR(255),
    performed_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reval_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_reval_reserve_acc FOREIGN KEY (revaluation_reserve_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT fk_reval_je FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id)
);

-- 6. Create fixed_asset_impairments table
CREATE TABLE fixed_asset_impairments (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    impairment_date DATE NOT NULL,
    impairment_amount DECIMAL(15,2) NOT NULL,
    recoverable_amount DECIMAL(15,2) NOT NULL,
    journal_entry_id BIGINT, -- NULL for non-financial books
    reason VARCHAR(255),
    performed_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_impairment_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_impairment_je FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id)
);

-- 7. Create fixed_asset_leases table
CREATE TABLE fixed_asset_leases (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL UNIQUE,
    lease_type VARCHAR(30) NOT NULL, -- 'OPERATING', 'FINANCE'
    lease_start_date DATE NOT NULL,
    lease_end_date DATE NOT NULL,
    monthly_lease_payment DECIMAL(15,2) NOT NULL,
    lessor_name VARCHAR(100) NOT NULL,
    lease_liability_account_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_lease_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_lease_liability_acc FOREIGN KEY (lease_liability_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT chk_lease_type CHECK (lease_type IN ('OPERATING', 'FINANCE'))
);

-- 8. Create fixed_asset_relations table for splits, merges, replacements, and IAS 16 component accounting
CREATE TABLE fixed_asset_relations (
    id BIGSERIAL PRIMARY KEY,
    source_asset_id BIGINT NOT NULL,
    target_asset_id BIGINT NOT NULL,
    relation_type VARCHAR(50) NOT NULL, -- PARENT_CHILD, SPARE, REPLACEMENT, SUCCESSOR, SPLIT_FROM, MERGED_INTO
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_relations_source FOREIGN KEY (source_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_relations_target FOREIGN KEY (target_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT chk_relation_type CHECK (relation_type IN ('PARENT_CHILD', 'SPARE', 'REPLACEMENT', 'SUCCESSOR', 'SPLIT_FROM', 'MERGED_INTO'))
);

-- 9. Create fixed_asset_maintenance_plans table for preventive planning
CREATE TABLE fixed_asset_maintenance_plans (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    maintenance_type VARCHAR(50) NOT NULL, -- PREVENTIVE, CORRECTIVE
    service_interval_days INTEGER NOT NULL,
    next_maintenance_date DATE NOT NULL,
    maintenance_vendor VARCHAR(100),
    estimated_cost DECIMAL(15,2) DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_maint_plans_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 10. Create fixed_asset_utilization table for usage-based depreciation
CREATE TABLE fixed_asset_utilization (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    reading_date DATE NOT NULL,
    usage_hours DECIMAL(10,2) DEFAULT 0.00,
    production_count INTEGER DEFAULT 0,
    mileage DECIMAL(12,2) DEFAULT 0.00,
    runtime_seconds BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_utilization_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 11. Create fixed_asset_attachments table for multi-document attachments
CREATE TABLE fixed_asset_attachments (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    file_name VARCHAR(100) NOT NULL,
    file_type VARCHAR(50) NOT NULL, -- PURCHASE_INVOICE, WARRANTY_CARD, INSURANCE_POLICY, IMAGE, MANUAL, CERTIFICATE, MAINTENANCE_REPORT
    active_version_number INTEGER DEFAULT 1,
    uploaded_by VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attachments_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 12. Create fixed_asset_attachment_versions table for document versioning
CREATE TABLE fixed_asset_attachment_versions (
    id BIGSERIAL PRIMARY KEY,
    attachment_id BIGINT NOT NULL,
    version_number INTEGER NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attachment_versions FOREIGN KEY (attachment_id) REFERENCES fixed_asset_attachments(id) ON DELETE CASCADE,
    CONSTRAINT uk_attachment_version UNIQUE (attachment_id, version_number)
);

-- 13. Create fixed_asset_reservations table for shared asset checkout
CREATE TABLE fixed_asset_reservations (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    assigned_employee_id BIGINT NOT NULL,
    reservation_date DATE NOT NULL,
    expected_checkout_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED', -- REQUESTED, APPROVED, CHECKED_OUT, RETURNED, CANCELLED, OVERDUE
    checked_out_at TIMESTAMP,
    returned_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservations_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservations_employee FOREIGN KEY (assigned_employee_id) REFERENCES employees(id),
    CONSTRAINT chk_res_status CHECK (status IN ('REQUESTED', 'APPROVED', 'CHECKED_OUT', 'RETURNED', 'CANCELLED', 'OVERDUE'))
);

-- 14. Create fixed_asset_insurance_claims table for tracking claims history
CREATE TABLE fixed_asset_insurance_claims (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    claim_number VARCHAR(50) NOT NULL,
    claim_date DATE NOT NULL,
    claim_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL, -- PENDING, APPROVED, REJECTED, PAID
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_claims_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 15. Create fixed_asset_downtimes table for downtime tracking
CREATE TABLE fixed_asset_downtimes (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    reason VARCHAR(255) NOT NULL,
    responsible_department VARCHAR(100) NOT NULL,
    lost_production_hours DECIMAL(10,2) DEFAULT 0.00,
    lost_revenue DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_downtimes_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 16. Create fixed_asset_work_orders table for maintenance work orders
CREATE TABLE fixed_asset_work_orders (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    work_order_number VARCHAR(50) NOT NULL,
    technician_name VARCHAR(100),
    vendor_name VARCHAR(100),
    labor_cost DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    parts_cost DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    downtime_hours DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, APPROVED, IN_PROGRESS, COMPLETED, CANCELLED
    description TEXT,
    completion_remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_work_orders_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE,
    CONSTRAINT chk_work_order_status CHECK (status IN ('DRAFT', 'APPROVED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- 17. Create fixed_asset_notifications table for notification logs
CREATE TABLE fixed_asset_notifications (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notifications_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- 18. Create fixed_asset_fx_logs table for tracking multi-currency revaluation changes
CREATE TABLE fixed_asset_fx_logs (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    valuation_date DATE NOT NULL,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    exchange_rate DECIMAL(18,6) NOT NULL,
    fx_gain_loss DECIMAL(15,2) NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fx_logs_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 19. Create fixed_asset_history table (Immutable Chronological Timeline)
CREATE TABLE fixed_asset_history (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL, -- ACQUISITION, ASSIGNMENT, TRANSFER, MAINTENANCE, CAPITALIZATION, DEPRECIATION, REVALUATION, IMPAIRMENT, DISPOSAL, SPLIT, MERGE, RETIREMENT, CHECKOUT, RETURN, CLAIM, ATTACHMENT, DOWNTIME, WORK_ORDER
    event_date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15,2),
    reference_id BIGINT,
    performed_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_history_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id) ON DELETE CASCADE
);

-- 20. Adjust audits and audits workflow status, plus items
ALTER TABLE fixed_asset_audits ADD COLUMN status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED';
ALTER TABLE fixed_asset_audits ADD CONSTRAINT chk_audit_status CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'VERIFIED', 'DISCREPANCY_REPORT', 'ADJUSTMENT_APPROVED'));

ALTER TABLE fixed_asset_audit_items ADD COLUMN location_mismatch BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_asset_audit_items ADD COLUMN barcode_scanned VARCHAR(100);

-- 21. Seed permissions
INSERT INTO permissions (code, name) VALUES
('FIXED_ASSET_REVALUE', 'Perform asset revaluation and revalue equity reserve postings'),
('FIXED_ASSET_IMPAIR', 'Record asset impairment losses'),
('FIXED_ASSET_LEASE', 'Manage leased assets and liability mappings'),
('FIXED_ASSET_SPLIT_MERGE', 'Split or merge assets and maintain relationships'),
('FIXED_ASSET_APPROVE_DISPOSAL', 'Approve asset disposal requests and GL derecognition'),
('FIXED_ASSET_RESERVE', 'Reserve and check out shared assets'),
('FIXED_ASSET_CLAIM', 'Manage asset insurance claims'),
('FIXED_ASSET_TRANSFER_APPROVE', 'Approve asset physical transfer requests'),
('FIXED_ASSET_DOWNTIME', 'Log asset operational downtime'),
('FIXED_ASSET_BULK', 'Execute bulk operations on fixed assets'),
('FIXED_ASSET_WORK_ORDER', 'Manage asset maintenance work orders'),
('FIXED_ASSET_COMPLIANCE', 'Manage data retention, legal holds, and audit recoveries')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'FINANCE_MANAGER')
  AND p.code IN ('FIXED_ASSET_REVALUE', 'FIXED_ASSET_IMPAIR', 'FIXED_ASSET_LEASE', 'FIXED_ASSET_SPLIT_MERGE', 'FIXED_ASSET_APPROVE_DISPOSAL', 'FIXED_ASSET_RESERVE', 'FIXED_ASSET_CLAIM', 'FIXED_ASSET_TRANSFER_APPROVE', 'FIXED_ASSET_DOWNTIME', 'FIXED_ASSET_BULK', 'FIXED_ASSET_WORK_ORDER', 'FIXED_ASSET_COMPLIANCE')
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 22. Create indexes
CREATE INDEX idx_fa_books_lookup ON fixed_asset_books (fixed_asset_id, depreciation_book_id);
CREATE INDEX idx_fa_history_timeline ON fixed_asset_history (fixed_asset_id, event_date);
CREATE INDEX idx_fa_relations_lookup ON fixed_asset_relations (source_asset_id, target_asset_id);
CREATE INDEX idx_fa_maint_plan_date ON fixed_asset_maintenance_plans (next_maintenance_date);
CREATE INDEX idx_fa_utilization_lookup ON fixed_asset_utilization (fixed_asset_id, reading_date);
CREATE INDEX idx_fa_attachments_lookup ON fixed_asset_attachments (fixed_asset_id);
CREATE INDEX idx_fa_reservations_lookup ON fixed_asset_reservations (fixed_asset_id, status);
CREATE INDEX idx_fa_downtimes_lookup ON fixed_asset_downtimes (fixed_asset_id);
CREATE INDEX idx_fa_work_orders_lookup ON fixed_asset_work_orders (fixed_asset_id, status);
CREATE INDEX idx_fa_notifications_lookup ON fixed_asset_notifications (company_id, read);
