-- V78__treasury_enterprise_blueprint.sql

-- 1. Multi-Level Treasury Entities
CREATE TABLE treasury_entities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    org_level VARCHAR(30) NOT NULL, -- GROUP, REGIONAL, COUNTRY, COMPANY
    parent_id BIGINT REFERENCES treasury_entities(id),
    country_code VARCHAR(3),
    currency_code VARCHAR(3) DEFAULT 'AED',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_treasury_entities_parent ON treasury_entities(parent_id);

-- Associate Company to Treasury Entity
ALTER TABLE companies ADD COLUMN treasury_entity_id BIGINT REFERENCES treasury_entities(id);

-- 2. Bank Account Authorization Matrix
CREATE TABLE treasury_authorization_matrices (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    min_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    max_amount DECIMAL(15,2) NOT NULL,
    required_role VARCHAR(50) NOT NULL, -- TREASURY_OFFICER, TREASURY_MANAGER, CFO, CFO_CEO
    dual_control BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_auth_matrix_limit CHECK (min_amount < max_amount)
);

CREATE INDEX idx_tam_lookup ON treasury_authorization_matrices(company_id, min_amount, max_amount);

-- 3. Extend Bank Accounts for SoD, restricted, and activation auditing
ALTER TABLE bank_accounts ADD COLUMN created_by VARCHAR(100);
ALTER TABLE bank_accounts ADD COLUMN activated_by VARCHAR(100);
ALTER TABLE bank_accounts ADD COLUMN restricted BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE bank_accounts ADD COLUMN overdraft_limit DECIMAL(15,2) NOT NULL DEFAULT 0.00;

-- 4. Bank Relationship Management
CREATE TABLE bank_relationships (
    id BIGSERIAL PRIMARY KEY,
    bank_id BIGINT NOT NULL REFERENCES banks(id),
    relationship_manager VARCHAR(150) NOT NULL,
    credit_rating VARCHAR(10) NOT NULL DEFAULT 'A',
    banking_products TEXT,
    contract_expiry DATE,
    sla_details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_br_bank ON bank_relationships(bank_id);

-- 5. Credit Facility Management
CREATE TABLE credit_facilities (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    facility_name VARCHAR(150) NOT NULL,
    facility_type VARCHAR(50) NOT NULL, -- REVOLVING, OVERDRAFT, TERM_LOAN
    limit_amount DECIMAL(15,2) NOT NULL,
    utilized_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    interest_rate DECIMAL(5,2) NOT NULL,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE credit_drawdowns (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES credit_facilities(id),
    drawdown_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    reference_number VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE credit_repayments (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES credit_facilities(id),
    repayment_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    reference_number VARCHAR(100) NOT NULL
);

-- 6. FX Deal Management & Hedge Accounting
CREATE TABLE fx_deals (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    deal_type VARCHAR(30) NOT NULL, -- SPOT, FORWARD, SWAP, OPTION, NDF
    buy_currency VARCHAR(3) NOT NULL,
    buy_amount DECIMAL(15,2) NOT NULL,
    sell_currency VARCHAR(3) NOT NULL,
    sell_amount DECIMAL(15,2) NOT NULL,
    exchange_rate DECIMAL(12,6) NOT NULL,
    value_date DATE NOT NULL,
    hedge_designation VARCHAR(50) NOT NULL DEFAULT 'NONE', -- CASH_FLOW, FAIR_VALUE, NET_INVESTMENT, NONE
    hedged_item_desc VARCHAR(255),
    effective_percentage DECIMAL(5,2) DEFAULT 100.00,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, SETTLED, CANCELLED
    dealer_username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fx_deal_settlements (
    id BIGSERIAL PRIMARY KEY,
    fx_deal_id BIGINT NOT NULL REFERENCES fx_deals(id),
    settlement_date DATE NOT NULL,
    bank_account_id BIGINT NOT NULL REFERENCES bank_accounts(id),
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE hedge_effectiveness_tests (
    id BIGSERIAL PRIMARY KEY,
    hedge_deal_id BIGINT NOT NULL REFERENCES fx_deals(id),
    testing_date DATE NOT NULL,
    prospective_result VARCHAR(100) NOT NULL,
    retrospective_result VARCHAR(100) NOT NULL,
    effectiveness_percentage DECIMAL(5,2) NOT NULL,
    passed BOOLEAN NOT NULL DEFAULT TRUE,
    journal_entry_id BIGINT,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_het_deal ON hedge_effectiveness_tests(hedge_deal_id);

-- 7. Positive Pay Support
CREATE TABLE positive_pay_files (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL REFERENCES bank_accounts(id),
    file_name VARCHAR(150) NOT NULL,
    file_content TEXT NOT NULL,
    generated_by VARCHAR(100) NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE positive_pay_items (
    id BIGSERIAL PRIMARY KEY,
    positive_pay_file_id BIGINT NOT NULL REFERENCES positive_pay_files(id),
    payment_number VARCHAR(50) NOT NULL,
    cheque_number VARCHAR(50) NOT NULL,
    issue_date DATE NOT NULL,
    payee_name VARCHAR(150) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ISSUED' -- ISSUED, CLEARED, VOID, STOPPED
);

CREATE INDEX idx_ppi_file ON positive_pay_items(positive_pay_file_id);

-- 8. Extend Payment Files for Digital Signatures and Business Continuity
ALTER TABLE payment_files ADD COLUMN digital_signature TEXT;
ALTER TABLE payment_files ADD COLUMN signature_timestamp TIMESTAMP;
ALTER TABLE payment_files ADD COLUMN certificate_alias VARCHAR(150);

-- 9. Payment Transmission Queue with failover & idempotency parameters
CREATE TABLE payment_transmission_queue (
    id BIGSERIAL PRIMARY KEY,
    batch_id BIGINT NOT NULL REFERENCES payment_batches(id),
    status VARCHAR(30) NOT NULL DEFAULT 'QUEUED', -- QUEUED, TRANSMITTING, TRANSMITTED, FAILED, CONFIRMED
    retry_count INTEGER NOT NULL DEFAULT 0,
    max_attempts INTEGER NOT NULL DEFAULT 3,
    primary_endpoint VARCHAR(255),
    failover_endpoint VARCHAR(255),
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    last_error TEXT,
    next_attempt_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ptq_status_next ON payment_transmission_queue(status, next_attempt_at) WHERE status = 'QUEUED';

CREATE TABLE payment_transmission_history (
    id BIGSERIAL PRIMARY KEY,
    queue_id BIGINT NOT NULL REFERENCES payment_transmission_queue(id),
    attempt_number INTEGER NOT NULL,
    endpoint_used VARCHAR(255) NOT NULL,
    response_code VARCHAR(50),
    response_body TEXT,
    success BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pth_queue ON payment_transmission_history(queue_id);

-- 10. Enterprise Alert Engine
CREATE TABLE treasury_alerts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    alert_type VARCHAR(50) NOT NULL, -- LOW_CASH, HIGH_CHARGES, FAILED_TRANSMISSION, RECON_BELOW_THRESHOLD, LARGE_FX_MOVEMENT, MATURITY, OVERDRAFT_REACHED, CONNECTOR_OFFLINE
    severity VARCHAR(30) NOT NULL, -- INFO, WARNING, CRITICAL
    message TEXT NOT NULL,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    resolved_by VARCHAR(100)
);

CREATE INDEX idx_alerts_lookup ON treasury_alerts(company_id, resolved) WHERE resolved = FALSE;

-- 11. Extend Treasury Investments for Portfolio Types, Float Rates, Classifications & Hedges
ALTER TABLE treasury_investments ADD COLUMN dealer_username VARCHAR(100);
ALTER TABLE treasury_investments ADD COLUMN settled_by_username VARCHAR(100);
ALTER TABLE treasury_investments ADD COLUMN second_approver_username VARCHAR(100);
ALTER TABLE treasury_investments ADD COLUMN hedged_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00;
ALTER TABLE treasury_investments ADD COLUMN ytm DECIMAL(5,2);
ALTER TABLE treasury_investments ADD COLUMN market_value DECIMAL(15,2);
ALTER TABLE treasury_investments ADD COLUMN book_value DECIMAL(15,2);
ALTER TABLE treasury_investments ADD COLUMN risk_rating VARCHAR(10);
ALTER TABLE treasury_investments ADD COLUMN duration_days INTEGER;
ALTER TABLE treasury_investments ADD COLUMN modified_duration DECIMAL(5,2);
ALTER TABLE treasury_investments ADD COLUMN asset_classification VARCHAR(30) DEFAULT 'AMORTIZED_COST'; -- AMORTIZED_COST, FVTPL, FVOCI

-- 12. Extend Bank Statements for auditing & SoD
ALTER TABLE bank_statements ADD COLUMN imported_by VARCHAR(100);
ALTER TABLE bank_statements ADD COLUMN reconciled_by VARCHAR(100);

-- 13. In-House Banking Intercompany Interest Logs
CREATE TABLE intercompany_interest_logs (
    id BIGSERIAL PRIMARY KEY,
    loan_id BIGINT NOT NULL REFERENCES intercompany_loans(id),
    interest_period_start DATE NOT NULL,
    interest_period_end DATE NOT NULL,
    accrued_amount DECIMAL(15,2) NOT NULL,
    posted_journal_entry_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 14. Advanced Counterparty Management
CREATE TABLE treasury_counterparties (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    counterparty_type VARCHAR(50) NOT NULL, -- BANK, BROKER, INVESTMENT_INSTITUTION, FX_DEALER, CLEARING_HOUSE
    credit_rating VARCHAR(10) NOT NULL DEFAULT 'A',
    risk_score INTEGER NOT NULL DEFAULT 1, -- 1 to 5
    exposure_limit DECIMAL(15,2) NOT NULL DEFAULT 10000000.00,
    current_exposure DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    contact_email VARCHAR(150),
    regulatory_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 15. Interest Rate Curve Engine
CREATE TABLE interest_rate_indices (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE, -- SOFR, EURIBOR, LIBOR, MIBOR
    name VARCHAR(150) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE interest_rate_history (
    id BIGSERIAL PRIMARY KEY,
    index_id BIGINT NOT NULL REFERENCES interest_rate_indices(id),
    rate_date DATE NOT NULL,
    interest_rate DECIMAL(7,4) NOT NULL, -- 5.1234 %
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_ir_history UNIQUE (index_id, rate_date)
);

CREATE INDEX idx_irh_date ON interest_rate_history(rate_date);

-- 16. Payment Sanctions & Compliance Screening Logs
CREATE TABLE payment_sanctions_logs (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL REFERENCES payments(id),
    checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sanction_status VARCHAR(30) NOT NULL, -- PASSED, SUSPECTED, BLOCKED
    beneficiary_match_score DECIMAL(5,2),
    sanction_list_hits TEXT,
    decision_reason TEXT,
    decided_by VARCHAR(100)
);

CREATE INDEX idx_psl_payment ON payment_sanctions_logs(payment_id);

-- 17. Treasury Workflow SLA & Escalation
CREATE TABLE treasury_workflow_escalations (
    id BIGSERIAL PRIMARY KEY,
    workflow_type VARCHAR(50) NOT NULL, -- PAYMENT_BATCH, CASH_TRANSFER, INVESTMENT
    target_id BIGINT NOT NULL,
    escalated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_supervisor VARCHAR(100) NOT NULL,
    original_approver VARCHAR(100) NOT NULL,
    sla_hours INTEGER NOT NULL,
    resolved BOOLEAN NOT NULL DEFAULT FALSE
);

-- 18. Multi-Tenant Treasury Policies & Cutoffs
CREATE TABLE company_treasury_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL UNIQUE REFERENCES companies(id),
    cutoff_time TIME NOT NULL DEFAULT '17:00:00',
    default_fx_provider_id BIGINT REFERENCES treasury_counterparties(id),
    default_reconciliation_rule_id BIGINT,
    default_bank_connector_id VARCHAR(100),
    max_daily_transfer_limit DECIMAL(15,2) NOT NULL DEFAULT 5000000.00
);

-- 19. Forecast Accuracy Logs
CREATE TABLE forecast_accuracy_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    forecast_date DATE NOT NULL,
    scenario_type VARCHAR(30) NOT NULL, -- BASE, OPTIMISTIC, CONSERVATIVE
    forecasted_amount DECIMAL(15,2) NOT NULL,
    actual_amount DECIMAL(15,2) NOT NULL,
    absolute_error DECIMAL(15,2) NOT NULL,
    percentage_error DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_fal_lookup ON forecast_accuracy_logs(company_id, forecast_date);

-- 20. Enterprise Scheduler Dashboard Logs
CREATE TABLE scheduler_job_runs (
    id BIGSERIAL PRIMARY KEY,
    job_name VARCHAR(150) NOT NULL,
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    runtime_ms BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL, -- SUCCESS, FAILED
    retry_count INTEGER NOT NULL DEFAULT 0,
    error_message TEXT
);

CREATE INDEX idx_sjr_lookup ON scheduler_job_runs(job_name, executed_at);

-- 21. Treasury Calendar
CREATE TABLE treasury_calendar (
    id BIGSERIAL PRIMARY KEY,
    holiday_date DATE NOT NULL,
    description VARCHAR(150) NOT NULL,
    holiday_type VARCHAR(30) NOT NULL, -- BANK, CURRENCY, REGIONAL
    currency_code VARCHAR(3),
    region_code VARCHAR(10),
    CONSTRAINT uk_treasury_calendar UNIQUE (holiday_date, holiday_type, currency_code, region_code)
);

CREATE INDEX idx_calendar_date ON treasury_calendar(holiday_date);

-- 22. Multi-Entity Netting
CREATE TABLE intercompany_netting_runs (
    id BIGSERIAL PRIMARY KEY,
    netting_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, CALCULATED, POSTED
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE intercompany_netting_lines (
    id BIGSERIAL PRIMARY KEY,
    run_id BIGINT NOT NULL REFERENCES intercompany_netting_runs(id) ON DELETE CASCADE,
    source_company_id BIGINT NOT NULL REFERENCES companies(id),
    target_company_id BIGINT NOT NULL REFERENCES companies(id),
    amount DECIMAL(15,2) NOT NULL,
    netting_type VARCHAR(30) NOT NULL, -- RECEIVABLE, PAYABLE
    reference_number VARCHAR(100) NOT NULL
);

-- 23. Exception Management Dashboard
CREATE TABLE treasury_exceptions (
    id BIGSERIAL PRIMARY KEY,
    exception_type VARCHAR(50) NOT NULL, -- TRANSMISSION_FAILURE, RECONCILIATION_EXCEPTION, COMPLIANCE_HOLD, CONNECTOR_OUTAGE
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, INVESTIGATING, RESOLVED
    message TEXT NOT NULL,
    assigned_user VARCHAR(100),
    sla_deadline TIMESTAMP,
    resolution_details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

CREATE INDEX idx_exceptions_lookup ON treasury_exceptions(status, exception_type);

-- 24. Cash Position Snapshot Audits
ALTER TABLE cash_position_snapshots ADD COLUMN version INTEGER NOT NULL DEFAULT 1;

CREATE TABLE cash_position_snapshot_audits (
    id BIGSERIAL PRIMARY KEY,
    snapshot_id BIGINT NOT NULL REFERENCES cash_position_snapshots(id) ON DELETE CASCADE,
    version INTEGER NOT NULL,
    action_type VARCHAR(30) NOT NULL, -- RECALCULATION, MANUAL_ADJUSTMENT
    initiated_by VARCHAR(100) NOT NULL,
    reason TEXT NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 25. Treasury Risk Limits & Configuration Policies
CREATE TABLE treasury_risk_limits (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    policy_name VARCHAR(150) NOT NULL,
    limit_category VARCHAR(50) NOT NULL, -- PAYMENT, FX, INVESTMENT, COUNTERPARTY, BANK, LIQUIDITY, CONCENTRATION, REGULATORY
    limit_type VARCHAR(50) NOT NULL, -- SINGLE_PAYMENT, DAILY_PAYMENT_TOTAL, DAILY_FX_TRADING, DAILY_INVESTMENT, BANK_EXPOSURE_PERCENT, COUNTERPARTY_EXPOSURE_PERCENT, CURRENCY_EXPOSURE_PERCENT, MINIMUM_LIQUIDITY, MAX_OVERDRAFT_UTILIZATION
    limit_value DECIMAL(15,2) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'AED',
    bank_id BIGINT REFERENCES banks(id),
    counterparty_id BIGINT REFERENCES treasury_counterparties(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_trl_policy ON treasury_risk_limits(company_id, limit_category, limit_type, active) WHERE active = TRUE;

-- 26. Treasury Document Repository
CREATE TABLE treasury_documents (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    document_type VARCHAR(50) NOT NULL, -- BANK_AGREEMENT, FACILITY_CONTRACT, FX_CONFIRMATION, INVESTMENT_CERTIFICATE, GUARANTEE, LETTER_OF_CREDIT, KYC, COMPLIANCE
    file_name VARCHAR(150) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, EXPIRED
    uploaded_by VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_td_lookup ON treasury_documents(company_id, document_type);

-- 27. Cash Flow Statement Classification for Snapshots & Lines
ALTER TABLE cash_position_snapshot_lines ADD COLUMN cash_flow_type VARCHAR(30) DEFAULT 'OPERATING'; -- OPERATING, INVESTING, FINANCING, TAX, EXTRAORDINARY

-- 28. Treasury Accounting Profiles
CREATE TABLE treasury_accounting_profiles (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL UNIQUE REFERENCES companies(id),
    profile_code VARCHAR(50) NOT NULL,
    cash_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    bank_fee_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    fx_gain_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    fx_loss_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    interest_income_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    interest_expense_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    hedge_reserve_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    investment_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    overdraft_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 29. Bank Account Signatories (Maker/Checker)
CREATE TABLE bank_account_signatories (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL REFERENCES bank_accounts(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    signing_level VARCHAR(30) NOT NULL, -- MAKER, CHECKER, APPROVER
    transaction_limit DECIMAL(15,2) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_bas_lookup ON bank_account_signatories(bank_account_id, employee_id) WHERE active = TRUE;

-- Event Store for Event Sourcing Replays
CREATE TABLE treasury_event_store (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL, -- PAYMENT, FX_DEAL, INVESTMENT, CREDIT_FACILITY
    aggregate_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL, -- PaymentCreated, PaymentApproved, etc.
    event_payload TEXT NOT NULL, -- JSON Payload
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tes_aggregate ON treasury_event_store(aggregate_type, aggregate_id);

-- 30. AI-Ready Extensibility additions
ALTER TABLE cash_position_snapshots ADD COLUMN forecast_confidence_score DECIMAL(5,2);
ALTER TABLE cash_position_snapshots ADD COLUMN fx_risk_prediction VARCHAR(100);
ALTER TABLE cash_position_snapshots ADD COLUMN liquidity_recommendation TEXT;
ALTER TABLE cash_position_snapshots ADD COLUMN investment_recommendation TEXT;
ALTER TABLE cash_position_snapshots ADD COLUMN liquidity_shortage_prediction TEXT;
ALTER TABLE cash_position_snapshots ADD COLUMN fx_volatility_prediction TEXT;
ALTER TABLE cash_position_snapshots ADD COLUMN fee_optimization_recommendation TEXT;
ALTER TABLE cash_position_snapshots ADD COLUMN counterparty_risk_prediction TEXT;
ALTER TABLE payment_batches ADD COLUMN fraud_risk_score DECIMAL(5,2);
ALTER TABLE payment_batches ADD COLUMN fraud_anomaly_details TEXT;

-- Materialized Views
CREATE OR REPLACE VIEW mv_cash_positions_raw AS
SELECT 
    ba.company_id,
    b.name AS bank_name,
    bb.name AS branch_name,
    ba.id AS bank_account_id,
    ba.account_number,
    ba.currency_code,
    ba.current_balance,
    ba.restricted,
    te.name AS treasury_entity_name
FROM bank_accounts ba
JOIN bank_branches bb ON ba.branch_id = bb.id
JOIN banks b ON bb.bank_id = b.id
JOIN companies c ON ba.company_id = c.id
LEFT JOIN treasury_entities te ON c.treasury_entity_id = te.id
WHERE ba.active = TRUE;

DROP MATERIALIZED VIEW IF EXISTS mv_cash_positions;
CREATE MATERIALIZED VIEW mv_cash_positions AS
SELECT * FROM mv_cash_positions_raw;

CREATE UNIQUE INDEX idx_mv_cash_positions_id ON mv_cash_positions(bank_account_id);

DROP MATERIALIZED VIEW IF EXISTS mv_bank_balances;
CREATE MATERIALIZED VIEW mv_bank_balances AS
SELECT 
    id AS bank_account_id,
    account_number,
    current_balance,
    reconciled_balance,
    projected_closing_balance,
    overdraft_limit,
    (current_balance - reconciled_balance) AS unreconciled_variance
FROM bank_accounts;

CREATE UNIQUE INDEX idx_mv_bank_balances_id ON mv_bank_balances(bank_account_id);

DROP MATERIALIZED VIEW IF EXISTS mv_liquidity_forecast;
CREATE MATERIALIZED VIEW mv_liquidity_forecast AS
SELECT 
    company_id,
    currency_code,
    snapshot_date,
    SUM(CASE WHEN current_balance >= 0 THEN current_balance ELSE 0.00 END) AS total_inflow,
    SUM(CASE WHEN current_balance < 0 THEN ABS(current_balance) ELSE 0.00 END) AS total_outflow,
    SUM(current_balance) AS net_cash_change
FROM cash_position_snapshot_lines l
JOIN cash_position_snapshots s ON l.snapshot_id = s.id
GROUP BY company_id, currency_code, snapshot_date;

CREATE UNIQUE INDEX idx_mv_liquidity_forecast_composite ON mv_liquidity_forecast(company_id, currency_code, snapshot_date);

DROP MATERIALIZED VIEW IF EXISTS mv_bank_reconciliation;
CREATE MATERIALIZED VIEW mv_bank_reconciliation AS
SELECT 
    s.bank_account_id,
    s.id AS statement_id,
    s.statement_number,
    COUNT(l.id) AS total_lines,
    SUM(CASE WHEN l.reconciled = TRUE THEN 1 ELSE 0 END) AS reconciled_lines,
    SUM(CASE WHEN l.reconciled = FALSE THEN 1 ELSE 0 END) AS unreconciled_lines,
    CASE 
        WHEN COUNT(l.id) > 0 THEN (SUM(CASE WHEN l.reconciled = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(l.id))
        ELSE 100.0
    END AS reconciliation_percentage
FROM bank_statements s
LEFT JOIN bank_statement_lines l ON l.statement_id = s.id
GROUP BY s.bank_account_id, s.id, s.statement_number;

CREATE UNIQUE INDEX idx_mv_bank_reconciliation_stmt ON mv_bank_reconciliation(statement_id);

DROP MATERIALIZED VIEW IF EXISTS mv_treasury_dashboard;
CREATE MATERIALIZED VIEW mv_treasury_dashboard AS
SELECT 
    ba.company_id,
    SUM(CASE WHEN ba.restricted = FALSE THEN ba.current_balance ELSE 0 END) AS total_available_cash,
    SUM(CASE WHEN ba.restricted = TRUE THEN ba.current_balance ELSE 0 END) AS total_restricted_cash,
    SUM(ti.principal_amount) AS total_investment_value,
    COUNT(ti.id) AS active_investments_count,
    MAX(ba.current_balance) AS largest_bank_exposure,
    SUM(ba.overdraft_limit) AS total_overdraft_limit
FROM bank_accounts ba
LEFT JOIN treasury_investments ti ON ti.bank_account_id = ba.id AND ti.status = 'ACTIVE'
GROUP BY ba.company_id;

CREATE UNIQUE INDEX idx_mv_treasury_dashboard_comp ON mv_treasury_dashboard(company_id);
