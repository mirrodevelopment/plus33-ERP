-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 387
-- File              : V387__enterprise_leave_management_v1.sql
-- Operation Type    : Schema Creation + Seed Data
-- Purpose           : Enterprise Leave Management — Policy, Workflow, Ledger, Audit
--
-- Tables Created    : country_leave_policies, company_leave_policy_overrides,
--                     leave_approval_workflows, leave_approval_history,
--                     employee_leave_balances, employee_leave_transactions,
--                     employee_leave_year_summary, leave_documents,
--                     holiday_calendar, leave_blackout_dates,
--                     leave_audit_log, notification_templates
-- Tables Altered    : leave_types, employee_leaves
-- Seed Data For     : leave_types (10 types), holiday_calendar (2026 FR),
--                     leave_approval_workflows, country_leave_policies,
--                     notification_templates
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================

-- ============================================================
-- SECTION 1: Extend leave_types
-- ============================================================

ALTER TABLE leave_types
    ADD COLUMN IF NOT EXISTS approval_level        VARCHAR(30)    DEFAULT 'SUPERVISOR',
    ADD COLUMN IF NOT EXISTS protected             BOOLEAN        DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS requires_document     BOOLEAN        DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS monthly_accrual       DECIMAL(5,2),
    ADD COLUMN IF NOT EXISTS max_consecutive_days  INTEGER,
    ADD COLUMN IF NOT EXISTS min_notice_days       INTEGER        DEFAULT 0;

-- ============================================================
-- SECTION 2: Extend employee_leaves
-- ============================================================

ALTER TABLE employee_leaves
    ADD COLUMN IF NOT EXISTS leave_session              VARCHAR(20)  DEFAULT 'FULL_DAY',
    ADD COLUMN IF NOT EXISTS approver_role              VARCHAR(30),
    ADD COLUMN IF NOT EXISTS current_approval_level     INTEGER      DEFAULT 1,
    ADD COLUMN IF NOT EXISTS cancelled_at               TIMESTAMP,
    ADD COLUMN IF NOT EXISTS cancellation_requested     BOOLEAN      DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS cancellation_reason        TEXT,
    ADD COLUMN IF NOT EXISTS approval_due_at            TIMESTAMP,
    ADD COLUMN IF NOT EXISTS escalated_at               TIMESTAMP,
    ADD COLUMN IF NOT EXISTS escalated_to               VARCHAR(50);

-- ============================================================
-- SECTION 3: country_leave_policies
-- ============================================================

CREATE TABLE IF NOT EXISTS country_leave_policies (
    id                   BIGSERIAL    PRIMARY KEY,
    country_code         VARCHAR(10)  NOT NULL,
    leave_type_id        BIGINT       NOT NULL,
    annual_entitlement   DECIMAL(5,2),
    monthly_accrual      DECIMAL(5,2),
    max_consecutive_days INTEGER,
    carry_forward_max    DECIMAL(5,2) DEFAULT 0,
    min_notice_days      INTEGER      DEFAULT 0,
    requires_document    BOOLEAN      DEFAULT FALSE,
    approval_level       VARCHAR(30)  DEFAULT 'SUPERVISOR',
    is_paid              BOOLEAN      DEFAULT TRUE,
    is_protected         BOOLEAN      DEFAULT FALSE,
    version              INTEGER      DEFAULT 1,
    effective_from       DATE         NOT NULL DEFAULT CURRENT_DATE,
    effective_to         DATE,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clp_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id)
);

CREATE INDEX IF NOT EXISTS idx_clp_country_type ON country_leave_policies(country_code, leave_type_id);

-- ============================================================
-- SECTION 4: company_leave_policy_overrides
-- ============================================================

CREATE TABLE IF NOT EXISTS company_leave_policy_overrides (
    id                   BIGSERIAL    PRIMARY KEY,
    company_id           BIGINT       NOT NULL DEFAULT 1,
    leave_type_id        BIGINT       NOT NULL,
    country_code         VARCHAR(10)  DEFAULT 'FR',
    annual_entitlement   DECIMAL(5,2),
    monthly_accrual      DECIMAL(5,2),
    max_consecutive_days INTEGER,
    carry_forward_max    DECIMAL(5,2),
    min_notice_days      INTEGER,
    version              INTEGER      DEFAULT 1,
    effective_from       DATE         NOT NULL DEFAULT CURRENT_DATE,
    effective_to         DATE,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clpo_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id)
);

CREATE INDEX IF NOT EXISTS idx_clpo_company_type ON company_leave_policy_overrides(company_id, leave_type_id);

-- ============================================================
-- SECTION 5: leave_approval_workflows
-- ============================================================

CREATE TABLE IF NOT EXISTS leave_approval_workflows (
    id               BIGSERIAL    PRIMARY KEY,
    company_id       BIGINT       NOT NULL DEFAULT 1,
    leave_type_id    BIGINT       NOT NULL,
    level            INTEGER      NOT NULL DEFAULT 1,
    approver_role    VARCHAR(50)  NOT NULL,
    sla_hours        INTEGER      DEFAULT 24,
    escalation_hours INTEGER      DEFAULT 48,
    escalate_to_role VARCHAR(50),
    can_skip         BOOLEAN      DEFAULT FALSE,
    is_required      BOOLEAN      DEFAULT TRUE,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_law_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id),
    CONSTRAINT uq_law_company_type_level UNIQUE (company_id, leave_type_id, level)
);

-- ============================================================
-- SECTION 6: leave_approval_history
-- ============================================================

CREATE TABLE IF NOT EXISTS leave_approval_history (
    id                    BIGSERIAL    PRIMARY KEY,
    leave_id              BIGINT       NOT NULL,
    level                 INTEGER      NOT NULL DEFAULT 1,
    approver_user_id      BIGINT,
    approver_role         VARCHAR(30),
    decision              VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    supervisor_comment    TEXT,
    store_admin_comment   TEXT,
    hr_comment            TEXT,
    old_status            VARCHAR(30),
    new_status            VARCHAR(30),
    approval_due_at       TIMESTAMP,
    escalated_at          TIMESTAMP,
    escalated_to          VARCHAR(50),
    decided_at            TIMESTAMP,
    session_id            VARCHAR(100),
    device_info           VARCHAR(200),
    CONSTRAINT fk_lah_leave FOREIGN KEY (leave_id) REFERENCES employee_leaves(id),
    CONSTRAINT fk_lah_approver FOREIGN KEY (approver_user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_lah_leave ON leave_approval_history(leave_id);
CREATE INDEX IF NOT EXISTS idx_lah_decision ON leave_approval_history(decision);

-- ============================================================
-- SECTION 7: employee_leave_balances (cached summary)
-- ============================================================

CREATE TABLE IF NOT EXISTS employee_leave_balances (
    id                BIGSERIAL      PRIMARY KEY,
    employee_id       BIGINT         NOT NULL,
    leave_type_id     BIGINT         NOT NULL,
    year              INTEGER        NOT NULL,
    opening_balance   DECIMAL(5,2)   DEFAULT 0,
    accrued           DECIMAL(5,2)   DEFAULT 0,
    carry_forward     DECIMAL(5,2)   DEFAULT 0,
    used              DECIMAL(5,2)   DEFAULT 0,
    pending           DECIMAL(5,2)   DEFAULT 0,
    encashed_days     DECIMAL(5,2)   DEFAULT 0,
    encashed_amount   DECIMAL(10,2)  DEFAULT 0,
    encashment_date   DATE,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_elb_employee  FOREIGN KEY (employee_id)  REFERENCES employees(id),
    CONSTRAINT fk_elb_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id),
    CONSTRAINT uq_elb_emp_type_year UNIQUE (employee_id, leave_type_id, year)
);

CREATE INDEX IF NOT EXISTS idx_elb_employee_year ON employee_leave_balances(employee_id, year);

-- ============================================================
-- SECTION 8: employee_leave_transactions (ledger — source of truth)
-- ============================================================

CREATE TABLE IF NOT EXISTS employee_leave_transactions (
    id                   BIGSERIAL    PRIMARY KEY,
    employee_id          BIGINT       NOT NULL,
    leave_type_id        BIGINT       NOT NULL,
    transaction_type     VARCHAR(30)  NOT NULL,
    -- ACCRUAL / OPENING / APPROVAL / CANCELLATION / CARRY_FORWARD / ENCASHMENT / ADJUSTMENT / EXPIRY
    days                 DECIMAL(5,2) NOT NULL,
    -- positive = credit, negative = debit
    reference_leave_id   BIGINT,
    note                 TEXT,
    created_by           BIGINT,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_elt_employee   FOREIGN KEY (employee_id)  REFERENCES employees(id),
    CONSTRAINT fk_elt_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id),
    CONSTRAINT fk_elt_ref_leave  FOREIGN KEY (reference_leave_id) REFERENCES employee_leaves(id)
);

CREATE INDEX IF NOT EXISTS idx_elt_employee_type ON employee_leave_transactions(employee_id, leave_type_id);
CREATE INDEX IF NOT EXISTS idx_elt_transaction_type ON employee_leave_transactions(transaction_type);

-- ============================================================
-- SECTION 9: employee_leave_year_summary (historical snapshot)
-- ============================================================

CREATE TABLE IF NOT EXISTS employee_leave_year_summary (
    id                   BIGSERIAL    PRIMARY KEY,
    employee_id          BIGINT       NOT NULL,
    year                 INTEGER      NOT NULL,
    annual_entitlement   DECIMAL(5,2) DEFAULT 0,
    carry_forward        DECIMAL(5,2) DEFAULT 0,
    accrued              DECIMAL(5,2) DEFAULT 0,
    used                 DECIMAL(5,2) DEFAULT 0,
    pending              DECIMAL(5,2) DEFAULT 0,
    encashed             DECIMAL(5,2) DEFAULT 0,
    remaining            DECIMAL(5,2) DEFAULT 0,
    snapshot_date        DATE,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_elys_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT uq_elys_emp_year UNIQUE (employee_id, year)
);

-- ============================================================
-- SECTION 10: leave_documents
-- ============================================================

CREATE TABLE IF NOT EXISTS leave_documents (
    id            BIGSERIAL    PRIMARY KEY,
    leave_id      BIGINT       NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    file_path     VARCHAR(500),
    mime_type     VARCHAR(100),
    uploaded_by   BIGINT,
    uploaded_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified      BOOLEAN      DEFAULT FALSE,
    verified_by   BIGINT,
    verified_at   TIMESTAMP,
    CONSTRAINT fk_ld_leave       FOREIGN KEY (leave_id)    REFERENCES employee_leaves(id),
    CONSTRAINT fk_ld_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id),
    CONSTRAINT fk_ld_verified_by FOREIGN KEY (verified_by) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_ld_leave ON leave_documents(leave_id);

-- ============================================================
-- SECTION 11: holiday_calendar
-- ============================================================

CREATE TABLE IF NOT EXISTS holiday_calendar (
    id             BIGSERIAL    PRIMARY KEY,
    country_code   VARCHAR(10)  NOT NULL DEFAULT 'FR',
    region         VARCHAR(50),
    holiday_name   VARCHAR(150) NOT NULL,
    holiday_date   DATE         NOT NULL,
    holiday_year   INTEGER      NOT NULL,
    is_recurring   BOOLEAN      DEFAULT FALSE,
    is_working_day BOOLEAN      DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_hc_country_date UNIQUE (country_code, holiday_date)
);

CREATE INDEX IF NOT EXISTS idx_hc_country_year ON holiday_calendar(country_code, holiday_year);

-- ============================================================
-- SECTION 12: leave_blackout_dates
-- ============================================================

CREATE TABLE IF NOT EXISTS leave_blackout_dates (
    id                       BIGSERIAL    PRIMARY KEY,
    company_id               BIGINT       NOT NULL DEFAULT 1,
    name                     VARCHAR(150) NOT NULL,
    start_date               DATE         NOT NULL,
    end_date                 DATE         NOT NULL,
    applies_to_all_types     BOOLEAN      DEFAULT TRUE,
    reason                   TEXT,
    active                   BOOLEAN      DEFAULT TRUE,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_lbd_company_dates ON leave_blackout_dates(company_id, start_date, end_date);

-- ============================================================
-- SECTION 13: leave_audit_log (permanent — never deleted)
-- ============================================================

CREATE TABLE IF NOT EXISTS leave_audit_log (
    id              BIGSERIAL    PRIMARY KEY,
    leave_id        BIGINT,
    actor_user_id   BIGINT,
    action          VARCHAR(50)  NOT NULL,
    old_value       TEXT,
    new_value       TEXT,
    note            TEXT,
    ip_address      VARCHAR(50),
    session_id      VARCHAR(100),
    device_info     VARCHAR(200),
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lal_leave FOREIGN KEY (leave_id) REFERENCES employee_leaves(id)
);

CREATE INDEX IF NOT EXISTS idx_lal_leave ON leave_audit_log(leave_id);
CREATE INDEX IF NOT EXISTS idx_lal_actor ON leave_audit_log(actor_user_id);
CREATE INDEX IF NOT EXISTS idx_lal_action ON leave_audit_log(action);

-- ============================================================
-- SECTION 14: notification_templates
-- ============================================================

CREATE TABLE IF NOT EXISTS notification_templates (
    id             BIGSERIAL     PRIMARY KEY,
    code           VARCHAR(100)  NOT NULL,
    language_code  VARCHAR(10)   NOT NULL DEFAULT 'en',
    title          VARCHAR(255)  NOT NULL,
    body           TEXT          NOT NULL,
    active         BOOLEAN       DEFAULT TRUE,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_nt_code_lang UNIQUE (code, language_code)
);

-- ============================================================
-- SECTION 15: Seed — 10 Leave Types (UPDATE existing + INSERT new)
-- ============================================================

-- Update existing 5 leave types with new policy columns
UPDATE leave_types SET
    approval_level = 'STORE_ADMIN', protected = FALSE, requires_document = FALSE,
    monthly_accrual = 2.5, max_consecutive_days = 30, min_notice_days = 7
WHERE code = 'ANNUAL';

UPDATE leave_types SET
    approval_level = 'SUPERVISOR', protected = FALSE, requires_document = TRUE,
    monthly_accrual = NULL, max_consecutive_days = NULL, min_notice_days = 0
WHERE code = 'SICK';

UPDATE leave_types SET
    approval_level = 'SUPERVISOR', protected = FALSE, requires_document = FALSE,
    annual_limit = 3, monthly_accrual = NULL, max_consecutive_days = 3, min_notice_days = 0
WHERE code = 'CASUAL';

UPDATE leave_types SET
    approval_level = 'STORE_ADMIN', protected = FALSE, requires_document = FALSE,
    monthly_accrual = NULL, max_consecutive_days = NULL, min_notice_days = 0
WHERE code = 'UNPAID';

UPDATE leave_types SET
    approval_level = 'SYSTEM', protected = TRUE, requires_document = TRUE,
    annual_limit = 182, monthly_accrual = NULL, max_consecutive_days = 182, min_notice_days = 0
WHERE code = 'MATERNITY';

-- Insert 5 new leave types
INSERT INTO leave_types (code, name, paid, annual_limit, carry_forward, active, approval_level, protected, requires_document, monthly_accrual, max_consecutive_days, min_notice_days)
VALUES
    ('PERSONAL',     'Personal Leave',     TRUE,  3,    FALSE, TRUE, 'SUPERVISOR',  FALSE, FALSE, NULL, 3,   0),
    ('EMERGENCY',    'Emergency Leave',    TRUE,  3,    FALSE, TRUE, 'SUPERVISOR',  FALSE, FALSE, NULL, 5,   0),
    ('MARRIAGE',     'Marriage Leave',     TRUE,  4,    FALSE, TRUE, 'STORE_ADMIN', FALSE, FALSE, NULL, 4,   0),
    ('BEREAVEMENT',  'Bereavement Leave',  TRUE,  14,   FALSE, TRUE, 'SYSTEM',      TRUE,  FALSE, NULL, 14,  0),
    ('PATERNITY',    'Paternity Leave',    TRUE,  25,   FALSE, TRUE, 'SYSTEM',      TRUE,  TRUE,  NULL, 25,  0)
ON CONFLICT (code) DO UPDATE SET
    approval_level = EXCLUDED.approval_level,
    protected = EXCLUDED.protected,
    requires_document = EXCLUDED.requires_document,
    min_notice_days = EXCLUDED.min_notice_days;

-- ============================================================
-- SECTION 16: Seed — 2026 French Public Holidays (11 jours fériés)
-- ============================================================

INSERT INTO holiday_calendar (country_code, holiday_name, holiday_date, holiday_year, is_recurring, is_working_day)
VALUES
    ('FR', 'Jour de l''An (New Year''s Day)',          '2026-01-01', 2026, TRUE,  FALSE),
    ('FR', 'Lundi de Pâques (Easter Monday)',          '2026-04-06', 2026, FALSE, FALSE),
    ('FR', 'Fête du Travail (Labour Day)',             '2026-05-01', 2026, TRUE,  FALSE),
    ('FR', 'Victoire 1945 (Victory in Europe Day)',   '2026-05-08', 2026, TRUE,  FALSE),
    ('FR', 'Ascension Day',                           '2026-05-14', 2026, FALSE, FALSE),
    ('FR', 'Lundi de Pentecôte (Whit Monday)',        '2026-05-25', 2026, FALSE, FALSE),
    ('FR', 'Fête Nationale (Bastille Day)',            '2026-07-14', 2026, TRUE,  FALSE),
    ('FR', 'Assomption (Assumption)',                  '2026-08-15', 2026, TRUE,  FALSE),
    ('FR', 'Toussaint (All Saints'' Day)',             '2026-11-01', 2026, TRUE,  FALSE),
    ('FR', 'Armistice Day',                            '2026-11-11', 2026, TRUE,  FALSE),
    ('FR', 'Noël (Christmas Day)',                    '2026-12-25', 2026, TRUE,  FALSE)
ON CONFLICT (country_code, holiday_date) DO NOTHING;

-- ============================================================
-- SECTION 17: Seed — Leave Approval Workflows
-- ============================================================

-- Supervisor-level leave types (Level 1 = SUPERVISOR)
INSERT INTO leave_approval_workflows (company_id, leave_type_id, level, approver_role, sla_hours, escalation_hours, escalate_to_role, can_skip, is_required)
SELECT 1, id, 1, 'SUPERVISOR', 24, 48, 'STORE_ADMIN', FALSE, TRUE
FROM leave_types WHERE code IN ('SICK', 'PERSONAL', 'CASUAL', 'EMERGENCY')
ON CONFLICT (company_id, leave_type_id, level) DO UPDATE SET
    approver_role = EXCLUDED.approver_role, sla_hours = EXCLUDED.sla_hours;

-- Store Admin level leave types (Level 1 = STORE_ADMIN)
INSERT INTO leave_approval_workflows (company_id, leave_type_id, level, approver_role, sla_hours, escalation_hours, escalate_to_role, can_skip, is_required)
SELECT 1, id, 1, 'STORE_ADMIN', 48, 72, NULL, FALSE, TRUE
FROM leave_types WHERE code IN ('ANNUAL', 'MARRIAGE', 'UNPAID')
ON CONFLICT (company_id, leave_type_id, level) DO UPDATE SET
    approver_role = EXCLUDED.approver_role, sla_hours = EXCLUDED.sla_hours;

-- Protected leave types (Level 1 = SYSTEM — auto-approve immediately)
INSERT INTO leave_approval_workflows (company_id, leave_type_id, level, approver_role, sla_hours, escalation_hours, escalate_to_role, can_skip, is_required)
SELECT 1, id, 1, 'SYSTEM', 0, 0, NULL, FALSE, TRUE
FROM leave_types WHERE code IN ('BEREAVEMENT', 'MATERNITY', 'PATERNITY')
ON CONFLICT (company_id, leave_type_id, level) DO UPDATE SET
    approver_role = EXCLUDED.approver_role, sla_hours = EXCLUDED.sla_hours;

-- ============================================================
-- SECTION 18: Seed — French Leave Policy (country_leave_policies)
-- ============================================================

INSERT INTO country_leave_policies
    (country_code, leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
     carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from)
SELECT
    'FR', id,
    CASE code
        WHEN 'ANNUAL'      THEN 30.0
        WHEN 'SICK'        THEN NULL
        WHEN 'PERSONAL'    THEN 3.0
        WHEN 'CASUAL'      THEN 3.0
        WHEN 'EMERGENCY'   THEN 3.0
        WHEN 'MARRIAGE'    THEN 4.0
        WHEN 'BEREAVEMENT' THEN 14.0
        WHEN 'MATERNITY'   THEN 182.0
        WHEN 'PATERNITY'   THEN 25.0
        WHEN 'UNPAID'      THEN NULL
    END,
    CASE code WHEN 'ANNUAL' THEN 2.5 ELSE NULL END,
    CASE code
        WHEN 'ANNUAL'      THEN 30
        WHEN 'SICK'        THEN NULL
        WHEN 'PERSONAL'    THEN 3
        WHEN 'CASUAL'      THEN 3
        WHEN 'EMERGENCY'   THEN 5
        WHEN 'MARRIAGE'    THEN 4
        WHEN 'BEREAVEMENT' THEN 14
        WHEN 'MATERNITY'   THEN 182
        WHEN 'PATERNITY'   THEN 25
        WHEN 'UNPAID'      THEN NULL
    END,
    CASE code WHEN 'ANNUAL' THEN 5.0 ELSE 0.0 END,
    CASE code WHEN 'ANNUAL' THEN 7 ELSE 0 END,
    CASE code WHEN 'SICK' THEN TRUE WHEN 'MATERNITY' THEN TRUE WHEN 'PATERNITY' THEN TRUE ELSE FALSE END,
    approval_level,
    paid,
    protected,
    1,
    '2026-01-01'
FROM leave_types
WHERE code IN ('ANNUAL','SICK','PERSONAL','CASUAL','EMERGENCY','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','UNPAID')
  AND active = TRUE
ON CONFLICT DO NOTHING;

-- ============================================================
-- SECTION 19: Seed — Notification Templates
-- ============================================================

INSERT INTO notification_templates (code, language_code, title, body) VALUES
('LEAVE_SUBMITTED',               'en', 'Leave Request Submitted',        'Your {leave_type} request for {total_days} day(s) from {start_date} to {end_date} has been submitted and is pending approval.'),
('LEAVE_APPROVED',                'en', 'Leave Request Approved',         'Your {leave_type} request for {total_days} day(s) from {start_date} to {end_date} has been approved.'),
('LEAVE_REJECTED',                'en', 'Leave Request Rejected',         'Your {leave_type} request for {total_days} day(s) from {start_date} to {end_date} has been rejected. Reason: {rejection_reason}'),
('LEAVE_CANCELLED',               'en', 'Leave Request Cancelled',        'Your {leave_type} request for {start_date} to {end_date} has been successfully cancelled.'),
('LEAVE_DOCUMENT_REQUIRED',       'en', 'Document Required for Approval', 'Your Sick Leave request requires a medical certificate before it can be approved. Please upload the certificate.'),
('LEAVE_CANCELLATION_REQUESTED',  'en', 'Cancellation Request Received',  '{employee_name} has requested cancellation of their approved {leave_type} from {start_date} to {end_date}.'),
('LEAVE_CANCELLATION_APPROVED',   'en', 'Leave Cancellation Approved',    'Your cancellation request for {leave_type} from {start_date} to {end_date} has been approved.'),
('LEAVE_ACCRUAL_CREDITED',        'en', 'Annual Leave Accrual Credited',  '{accrual_days} day(s) of Annual Leave have been credited to your balance for {month_year}.'),
('LEAVE_REMINDER_START',          'en', 'Leave Starting Tomorrow',        'Reminder: Your approved {leave_type} starts tomorrow ({start_date}). Enjoy your time off!'),
('LEAVE_REMINDER_END',            'en', 'Leave Ending Tomorrow',          'Reminder: Your {leave_type} ends tomorrow ({end_date}). Welcome back!'),
('LEAVE_BLACKOUT_BLOCKED',        'en', 'Leave Blocked — Blackout Period','Your {leave_type} request was blocked because it falls within a company blackout period: {blackout_name}.'),
('LEAVE_SHIFT_CONFLICT',          'en', 'Shift Conflict Warning',         'Your leave request overlaps with an assigned shift on {conflict_date}. Your manager will be notified for reassignment.'),
('LEAVE_SLA_REMINDER',            'en', 'Leave Approval Reminder',        'A leave request from {employee_name} ({leave_type}, {total_days} days) has been pending your approval for {hours_pending} hour(s).'),
('LEAVE_ESCALATED',               'en', 'Leave Request Escalated',        'A leave request from {employee_name} has been escalated to you because the previous approver did not respond within the SLA window.'),
('LEAVE_SYSTEM_AUTO_APPROVED',    'en', 'Leave Auto-Approved (Protected)','Your {leave_type} request has been automatically approved as it is a legally protected leave type under French labour law.')
ON CONFLICT (code, language_code) DO NOTHING;

-- ============================================================
-- SECTION 20: Indexes on extended employee_leaves columns
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_el_leave_session ON employee_leaves(leave_session);
CREATE INDEX IF NOT EXISTS idx_el_approval_level ON employee_leaves(current_approval_level);
CREATE INDEX IF NOT EXISTS idx_el_approval_due ON employee_leaves(approval_due_at);
CREATE INDEX IF NOT EXISTS idx_el_cancellation ON employee_leaves(cancellation_requested);
