-- ============================================================
-- V392__enterprise_leave_policies_seed.sql
-- PLUS33 ERP — Extended Leave Policies and Attendance Sync
-- ============================================================

-- SECTION 1: Insert Parental and Comp Off leave types
INSERT INTO leave_types (code, name, paid, annual_limit, carry_forward, active, approval_level, protected, requires_document, monthly_accrual, max_consecutive_days, min_notice_days)
VALUES
    ('PARENTAL', 'Parental Leave', TRUE, NULL, FALSE, TRUE, 'SYSTEM', TRUE, TRUE, NULL, NULL, 0),
    ('COMP_OFF', 'Comp Off', TRUE, NULL, FALSE, TRUE, 'SUPERVISOR', FALSE, FALSE, NULL, NULL, 0)
ON CONFLICT (code) DO NOTHING;

-- SECTION 2: Extend attendance table
ALTER TABLE attendance
    ADD COLUMN IF NOT EXISTS paid_leave BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS leave_type_id BIGINT,
    ADD COLUMN IF NOT EXISTS leave_id BIGINT,
    ADD COLUMN IF NOT EXISTS deduction BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS payroll_status VARCHAR(30) DEFAULT 'Pending',
    ADD COLUMN IF NOT EXISTS leave_minutes INTEGER DEFAULT 0;

ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id),
    ADD CONSTRAINT fk_attendance_leave FOREIGN KEY (leave_id) REFERENCES employee_leaves(id);

-- SECTION 3: Clear old configurations to keep seeds clean and idempotent
DELETE FROM country_leave_policies WHERE country_code IN ('IN', 'AE', 'EU', 'FR');

-- SECTION 4: Seed India (IN) Leave Policies
INSERT INTO country_leave_policies
    (country_code, leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
     carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from)
SELECT
    'IN', id,
    CASE code
        WHEN 'ANNUAL'      THEN 18.0
        WHEN 'SICK'        THEN 12.0
        WHEN 'PERSONAL'    THEN 3.0
        WHEN 'CASUAL'      THEN 12.0
        WHEN 'EMERGENCY'   THEN 3.0
        WHEN 'MARRIAGE'    THEN 5.0
        WHEN 'BEREAVEMENT' THEN 7.0
        WHEN 'MATERNITY'   THEN 182.0
        WHEN 'PATERNITY'   THEN 15.0
        WHEN 'PARENTAL'    THEN NULL
        WHEN 'COMP_OFF'    THEN NULL
        WHEN 'UNPAID'      THEN NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 1.50
        WHEN 'CASUAL'      THEN 1.00
        WHEN 'SICK'        THEN 1.00
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 18
        WHEN 'CASUAL'      THEN 3
        WHEN 'PERSONAL'    THEN 3
        WHEN 'MARRIAGE'    THEN 5
        WHEN 'BEREAVEMENT' THEN 7
        WHEN 'MATERNITY'   THEN 182
        WHEN 'PATERNITY'   THEN 15
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 30.0
        ELSE 0.0
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 7
        WHEN 'MARRIAGE'    THEN 14
        WHEN 'PATERNITY'   THEN 14
        WHEN 'MATERNITY'   THEN 30
        ELSE 0
    END,
    CASE code
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        ELSE FALSE
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 'STORE_ADMIN'
        WHEN 'MARRIAGE'    THEN 'STORE_ADMIN'
        WHEN 'UNPAID'      THEN 'STORE_ADMIN'
        WHEN 'MATERNITY'   THEN 'SYSTEM'
        WHEN 'PATERNITY'   THEN 'SYSTEM'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM'
        WHEN 'PARENTAL'    THEN 'SYSTEM'
        ELSE 'SUPERVISOR'
    END,
    CASE code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    CASE code WHEN 'MATERNITY' THEN TRUE WHEN 'PATERNITY' THEN TRUE WHEN 'BEREAVEMENT' THEN TRUE WHEN 'PARENTAL' THEN TRUE ELSE FALSE END,
    1,
    '2026-01-01'
FROM leave_types
WHERE code IN ('ANNUAL','SICK','PERSONAL','CASUAL','EMERGENCY','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','PARENTAL','COMP_OFF','UNPAID');

-- SECTION 5: Seed EU & FR Leave Policies
INSERT INTO country_leave_policies
    (country_code, leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
     carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from)
SELECT
    'FR', id,
    CASE code
        WHEN 'ANNUAL'      THEN 25.0
        WHEN 'SICK'        THEN 180.0
        WHEN 'PERSONAL'    THEN 3.0
        WHEN 'CASUAL'      THEN 3.0
        WHEN 'EMERGENCY'   THEN 3.0
        WHEN 'MARRIAGE'    THEN 5.0
        WHEN 'BEREAVEMENT' THEN 5.0
        WHEN 'MATERNITY'   THEN 112.0
        WHEN 'PATERNITY'   THEN 25.0
        WHEN 'PARENTAL'    THEN 365.0
        WHEN 'COMP_OFF'    THEN NULL
        WHEN 'UNPAID'      THEN NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 2.08
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 30
        WHEN 'PERSONAL'    THEN 3
        WHEN 'CASUAL'      THEN 3
        WHEN 'MARRIAGE'    THEN 5
        WHEN 'BEREAVEMENT' THEN 5
        WHEN 'MATERNITY'   THEN 112
        WHEN 'PATERNITY'   THEN 25
        WHEN 'PARENTAL'    THEN 365
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 0.0
        ELSE 0.0
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 14
        WHEN 'MARRIAGE'    THEN 30
        WHEN 'PATERNITY'   THEN 30
        WHEN 'MATERNITY'   THEN 60
        WHEN 'PARENTAL'    THEN 60
        ELSE 0
    END,
    CASE code
        WHEN 'SICK'        THEN TRUE
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        WHEN 'PARENTAL'    THEN TRUE
        ELSE FALSE
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 'STORE_ADMIN'
        WHEN 'MARRIAGE'    THEN 'STORE_ADMIN'
        WHEN 'UNPAID'      THEN 'STORE_ADMIN'
        WHEN 'MATERNITY'   THEN 'SYSTEM'
        WHEN 'PATERNITY'   THEN 'SYSTEM'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM'
        WHEN 'PARENTAL'    THEN 'SYSTEM'
        ELSE 'SUPERVISOR'
    END,
    CASE code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    CASE code WHEN 'MATERNITY' THEN TRUE WHEN 'PATERNITY' THEN TRUE WHEN 'BEREAVEMENT' THEN TRUE WHEN 'PARENTAL' THEN TRUE ELSE FALSE END,
    1,
    '2026-01-01'
FROM leave_types
WHERE code IN ('ANNUAL','SICK','PERSONAL','CASUAL','EMERGENCY','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','PARENTAL','COMP_OFF','UNPAID');

-- Duplicate seed for 'EU' country code to ensure compatibility
INSERT INTO country_leave_policies
    (country_code, leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
     carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from)
SELECT 'EU', leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
       carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from
FROM country_leave_policies WHERE country_code = 'FR';

-- SECTION 6: Seed UAE (AE) Leave Policies
INSERT INTO country_leave_policies
    (country_code, leave_type_id, annual_entitlement, monthly_accrual, max_consecutive_days,
     carry_forward_max, min_notice_days, requires_document, approval_level, is_paid, is_protected, version, effective_from)
SELECT
    'AE', id,
    CASE code
        WHEN 'ANNUAL'      THEN 30.0
        WHEN 'SICK'        THEN 90.0
        WHEN 'PERSONAL'    THEN 3.0
        WHEN 'CASUAL'      THEN 3.0
        WHEN 'EMERGENCY'   THEN 3.0
        WHEN 'MARRIAGE'    THEN 5.0
        WHEN 'BEREAVEMENT' THEN 5.0
        WHEN 'MATERNITY'   THEN 60.0
        WHEN 'PATERNITY'   THEN 5.0
        WHEN 'PARENTAL'    THEN NULL
        WHEN 'COMP_OFF'    THEN NULL
        WHEN 'UNPAID'      THEN NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 2.50
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 30
        WHEN 'SICK'        THEN 90
        WHEN 'PERSONAL'    THEN 3
        WHEN 'CASUAL'      THEN 3
        WHEN 'MARRIAGE'    THEN 5
        WHEN 'BEREAVEMENT' THEN 5
        WHEN 'MATERNITY'   THEN 60
        WHEN 'PATERNITY'   THEN 5
        ELSE NULL
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 10.0
        ELSE 0.0
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 7
        WHEN 'MARRIAGE'    THEN 14
        WHEN 'PATERNITY'   THEN 14
        WHEN 'MATERNITY'   THEN 30
        ELSE 0
    END,
    CASE code
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        ELSE FALSE
    END,
    CASE code
        WHEN 'ANNUAL'      THEN 'STORE_ADMIN'
        WHEN 'MARRIAGE'    THEN 'STORE_ADMIN'
        WHEN 'UNPAID'      THEN 'STORE_ADMIN'
        WHEN 'MATERNITY'   THEN 'SYSTEM'
        WHEN 'PATERNITY'   THEN 'SYSTEM'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM'
        WHEN 'PARENTAL'    THEN 'SYSTEM'
        ELSE 'SUPERVISOR'
    END,
    CASE code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    CASE code WHEN 'MATERNITY' THEN TRUE WHEN 'PATERNITY' THEN TRUE WHEN 'BEREAVEMENT' THEN TRUE WHEN 'PARENTAL' THEN TRUE ELSE FALSE END,
    1,
    '2026-01-01'
FROM leave_types
WHERE code IN ('ANNUAL','SICK','PERSONAL','CASUAL','EMERGENCY','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','PARENTAL','COMP_OFF','UNPAID');
