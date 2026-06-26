-- ============================================================
-- V73__create_fixed_assets_module.sql
-- PLUS33 ERP — Fixed Asset Management Tables
-- ============================================================

-- 1. Create sequence for automatic asset number generation
CREATE SEQUENCE fixed_asset_code_seq START WITH 1 INCREMENT BY 1;

-- 2. Create asset_categories table
CREATE TABLE asset_categories (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    depreciation_method VARCHAR(30) NOT NULL, -- 'STRAIGHT_LINE', 'WDV', 'NONE'
    depreciation_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00, -- Percentage (e.g. 20.00 for 20%)
    useful_life_years INTEGER NOT NULL DEFAULT 0,
    asset_account_id BIGINT NOT NULL,
    accumulated_depreciation_account_id BIGINT NOT NULL,
    depreciation_expense_account_id BIGINT NOT NULL,
    gain_loss_account_id BIGINT NOT NULL, -- GL account for disposal gain/loss

    CONSTRAINT fk_asset_categories_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_asset_categories_asset_acc FOREIGN KEY (asset_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT fk_asset_categories_accum_acc FOREIGN KEY (accumulated_depreciation_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT fk_asset_categories_expense_acc FOREIGN KEY (depreciation_expense_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT fk_asset_categories_gain_loss_acc FOREIGN KEY (gain_loss_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT uk_asset_category_company_code UNIQUE (company_id, code),
    CONSTRAINT chk_depr_method CHECK (depreciation_method IN ('STRAIGHT_LINE', 'WDV', 'NONE'))
);

-- 3. Create fixed_assets register table with component, document, warranty, and insurance fields
CREATE TABLE fixed_assets (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    parent_asset_id BIGINT, -- Component reference (parent-child relationship)
    asset_code VARCHAR(50) NOT NULL, -- Auto-generated: AST-YYYY-000001
    name VARCHAR(100) NOT NULL,
    description TEXT,
    acquisition_date DATE NOT NULL,
    acquisition_cost DECIMAL(15,2) NOT NULL,
    salvage_value DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    useful_life_years INTEGER NOT NULL,
    depreciation_rate DECIMAL(5,2) NOT NULL,
    depreciation_method VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, ACTIVE, UNDER_MAINTENANCE, TRANSFERRED, DISPOSED, WRITTEN_OFF, LOST, STOLEN
    warehouse_id BIGINT, -- Current physical location
    store_id BIGINT,     -- Current physical location
    supplier_invoice_id BIGINT, -- Source purchase invoice linkage
    original_cost DECIMAL(15,2) NOT NULL, -- acquisition cost + capitalized maintenance costs
    current_book_value DECIMAL(15,2) NOT NULL,
    accumulated_depreciation DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    last_depreciation_date DATE,
    
    -- Document & Image References (Stored as URLs/Paths)
    purchase_invoice_url VARCHAR(255),
    warranty_doc_url VARCHAR(255),
    insurance_doc_url VARCHAR(255),
    photo_url VARCHAR(255),
    manual_url VARCHAR(255),
    
    -- Warranty & AMC Tracking
    warranty_start_date DATE,
    warranty_end_date DATE,
    warranty_vendor VARCHAR(100),
    amc_expiry_date DATE,
    amc_renewal_date DATE,
    
    -- Insurance Tracking
    insurance_policy_number VARCHAR(50),
    insurance_company VARCHAR(100),
    insurance_premium DECIMAL(15,2),
    insurance_expiry_date DATE,
    insured_value DECIMAL(15,2),

    -- Current Assignments
    assigned_employee_id BIGINT,
    assigned_department VARCHAR(100),

    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fixed_assets_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_fixed_assets_category FOREIGN KEY (category_id) REFERENCES asset_categories(id),
    CONSTRAINT fk_fixed_assets_parent FOREIGN KEY (parent_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT fk_fixed_assets_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_fixed_assets_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_fixed_assets_invoice FOREIGN KEY (supplier_invoice_id) REFERENCES supplier_invoices(id),
    CONSTRAINT fk_fixed_assets_employee FOREIGN KEY (assigned_employee_id) REFERENCES employees(id),
    CONSTRAINT uk_fixed_asset_company_code UNIQUE (company_id, asset_code),
    CONSTRAINT chk_asset_status CHECK (status IN ('DRAFT', 'ACTIVE', 'UNDER_MAINTENANCE', 'TRANSFERRED', 'DISPOSED', 'WRITTEN_OFF', 'LOST', 'STOLEN')),
    CONSTRAINT chk_cost_positive CHECK (acquisition_cost >= 0),
    CONSTRAINT chk_book_value_positive CHECK (current_book_value >= 0)
);

-- 4. Create fixed_asset_depreciation_logs table
CREATE TABLE fixed_asset_depreciation_logs (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    depreciation_date DATE NOT NULL,
    depreciation_amount DECIMAL(15,2) NOT NULL,
    book_value_before DECIMAL(15,2) NOT NULL,
    book_value_after DECIMAL(15,2) NOT NULL,
    journal_entry_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_depr_logs_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT fk_depr_logs_je FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id)
);

-- 5. Create fixed_asset_transfers table
CREATE TABLE fixed_asset_transfers (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    transfer_date DATE NOT NULL,
    from_warehouse_id BIGINT,
    to_warehouse_id BIGINT,
    from_store_id BIGINT,
    to_store_id BIGINT,
    reason VARCHAR(255),
    transferred_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfers_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT fk_transfers_from_wh FOREIGN KEY (from_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_transfers_to_wh FOREIGN KEY (to_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_transfers_from_store FOREIGN KEY (from_store_id) REFERENCES stores(id),
    CONSTRAINT fk_transfers_to_store FOREIGN KEY (to_store_id) REFERENCES stores(id)
);

-- 6. Create fixed_asset_maintenances table
CREATE TABLE fixed_asset_maintenances (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    maintenance_date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    cost DECIMAL(15,2) NOT NULL,
    capitalize BOOLEAN NOT NULL DEFAULT FALSE,
    journal_entry_id BIGINT, -- Nullable if expensed externally or not yet posted
    performed_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_maintenance_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT fk_maintenance_je FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id)
);

-- 7. Create fixed_asset_assignments history table
CREATE TABLE fixed_asset_assignments (
    id BIGSERIAL PRIMARY KEY,
    fixed_asset_id BIGINT NOT NULL,
    assigned_employee_id BIGINT,
    assigned_department VARCHAR(100),
    assigned_warehouse_id BIGINT,
    assigned_store_id BIGINT,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at TIMESTAMP,
    assigned_by VARCHAR(100) NOT NULL,

    CONSTRAINT fk_assignments_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT fk_assignments_employee FOREIGN KEY (assigned_employee_id) REFERENCES employees(id),
    CONSTRAINT fk_assignments_warehouse FOREIGN KEY (assigned_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_assignments_store FOREIGN KEY (assigned_store_id) REFERENCES stores(id)
);

-- 8. Create fixed_asset_audits (physical verification) tables
CREATE TABLE fixed_asset_audits (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    audit_date DATE NOT NULL,
    auditor_name VARCHAR(100) NOT NULL,
    warehouse_id BIGINT, -- physical location audited
    store_id BIGINT,     -- physical location audited
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audits_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_audits_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_audits_store FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE TABLE fixed_asset_audit_items (
    id BIGSERIAL PRIMARY KEY,
    audit_id BIGINT NOT NULL,
    fixed_asset_id BIGINT NOT NULL,
    result VARCHAR(30) NOT NULL, -- 'FOUND_OK', 'DAMAGED', 'MISSING'
    remarks VARCHAR(255),
    photo_evidence_url VARCHAR(255),

    CONSTRAINT fk_audit_items_audit FOREIGN KEY (audit_id) REFERENCES fixed_asset_audits(id),
    CONSTRAINT fk_audit_items_asset FOREIGN KEY (fixed_asset_id) REFERENCES fixed_assets(id),
    CONSTRAINT chk_audit_result CHECK (result IN ('FOUND_OK', 'DAMAGED', 'MISSING'))
);

-- 9. Create indexes for reports, dashboards, and audit verification queries
CREATE INDEX idx_asset_company_status ON fixed_assets (company_id, status);
CREATE INDEX idx_asset_category ON fixed_assets (category_id);
CREATE INDEX idx_asset_parent ON fixed_assets (parent_asset_id);
CREATE INDEX idx_asset_assigned ON fixed_assets (assigned_employee_id);
CREATE INDEX idx_depr_log_asset ON fixed_asset_depreciation_logs (fixed_asset_id);
CREATE INDEX idx_transfer_asset ON fixed_asset_transfers (fixed_asset_id);
CREATE INDEX idx_maintenance_asset ON fixed_asset_maintenances (fixed_asset_id);
CREATE INDEX idx_assignment_asset ON fixed_asset_assignments (fixed_asset_id);
CREATE INDEX idx_audit_item_audit ON fixed_asset_audit_items (audit_id);
