-- =============================================================================
-- V430: Enterprise Leave Management — Policy Groups, Rules, Pay Tiers,
--       Weekly Off Rules, and Balance History Snapshots
--
-- This migration introduces a fully normalized, multi-country leave policy
-- architecture. It replaces the country_code-based approach with named
-- policy groups (INDIA, EU, UAE) that can be assigned to companies/stores.
-- All values are stored in the database so HR can override them without
-- a code deployment.
-- =============================================================================

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. LEAVE POLICY GROUPS
--    Named groups: INDIA, EU, UAE (extensible).
--    Companies and stores reference a policy_group_code.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leave_policy_groups (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(30)  NOT NULL UNIQUE,   -- INDIA | EU | UAE
    name                VARCHAR(100) NOT NULL,
    description         TEXT,
    -- Working week configuration (comma-separated day names)
    working_days        VARCHAR(100) NOT NULL DEFAULT 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY',
    -- Standard hours
    hours_per_day       NUMERIC(4,2) NOT NULL DEFAULT 8.00,
    hours_per_week      NUMERIC(5,2) NOT NULL DEFAULT 40.00,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE  leave_policy_groups              IS 'Top-level leave policy groups: INDIA, EU, UAE. Companies/stores inherit from their assigned group.';
COMMENT ON COLUMN leave_policy_groups.working_days IS 'Comma-separated ISO day names, e.g. MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY';
COMMENT ON COLUMN leave_policy_groups.hours_per_day IS 'Standard contracted hours per working day.';

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. WEEKLY OFF RULES
--    Separate rows for each weekly off day per policy group.
--    Allows: India/UAE → SUNDAY only; EU → SATURDAY + SUNDAY.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS weekly_off_rules (
    id                  BIGSERIAL PRIMARY KEY,
    policy_group_id     BIGINT NOT NULL REFERENCES leave_policy_groups(id) ON DELETE CASCADE,
    day_of_week         VARCHAR(15) NOT NULL,  -- MONDAY ... SUNDAY
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (policy_group_id, day_of_week)
);

COMMENT ON TABLE weekly_off_rules IS 'Weekly off days per policy group. Used by the working-day calculator instead of hardcoded country checks.';

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. LEAVE POLICY RULES
--    Replaces country_leave_policies. Linked to leave_policy_groups.
--    Contains all configurable parameters for each leave type per group.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leave_policy_rules (
    id                              BIGSERIAL PRIMARY KEY,
    policy_group_id                 BIGINT NOT NULL REFERENCES leave_policy_groups(id) ON DELETE CASCADE,
    leave_type_id                   BIGINT NOT NULL REFERENCES leave_types(id) ON DELETE CASCADE,

    -- Core entitlement
    default_entitlement             NUMERIC(7,2),               -- NULL = unlimited (e.g. EU Sick)
    monthly_accrual                 NUMERIC(5,2),               -- NULL = lump sum
    entitlement_unit                VARCHAR(20) NOT NULL DEFAULT 'WORKING_DAYS',  -- WORKING_DAYS | CALENDAR_DAYS | WEEKS

    -- Consecutive day constraints
    max_consecutive_days            INT,
    max_per_year                    INT,                        -- Hard cap per calendar year (e.g. UAE Sick = 90)
    min_notice_days                 INT NOT NULL DEFAULT 0,

    -- Document requirements
    document_required_after_days    INT NOT NULL DEFAULT 0,     -- 0 = never; 2 = cert needed if > 2 days

    -- Half-day support
    allow_half_day                  BOOLEAN NOT NULL DEFAULT TRUE,
    minimum_leave_unit              NUMERIC(3,2) NOT NULL DEFAULT 1.00,   -- 0.5 | 1.0

    -- Carry forward
    carry_forward_allowed           BOOLEAN NOT NULL DEFAULT FALSE,
    carry_forward_limit             NUMERIC(5,2),               -- NULL = unlimited if allowed
    carry_forward_expiry_months     INT,                        -- months after year-end before carry-forward expires

    -- Encashment
    encashment_allowed              BOOLEAN NOT NULL DEFAULT FALSE,
    maximum_encashment_days         NUMERIC(5,2),
    minimum_balance_for_encashment  NUMERIC(5,2),

    -- Approval workflow
    -- Enum-enforced via CHECK: SHIFT_SUPERVISOR | STORE_ADMIN | REGIONAL_ADMIN | HR | AUTO_APPROVED
    approval_level                  VARCHAR(30) NOT NULL DEFAULT 'SHIFT_SUPERVISOR'
                                        CHECK (approval_level IN ('SHIFT_SUPERVISOR','STORE_ADMIN','REGIONAL_ADMIN','HR','AUTO_APPROVED')),
    -- Enum-enforced via CHECK: MANAGER_APPROVAL | HR_APPROVAL | AUTO_APPROVE | SYSTEM_APPROVAL
    approval_mode                   VARCHAR(30) NOT NULL DEFAULT 'MANAGER_APPROVAL'
                                        CHECK (approval_mode IN ('MANAGER_APPROVAL','HR_APPROVAL','AUTO_APPROVE','SYSTEM_APPROVAL')),

    -- Financial treatment
    is_paid                         BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected                    BOOLEAN NOT NULL DEFAULT FALSE,  -- Cannot be rejected; triggers AUTO_APPROVE

    -- Lifetime restriction (e.g. Marriage Leave = once per employment)
    lifetime_limit                  INT,   -- NULL = no lifetime restriction; 1 = once per employment

    -- Versioning & validity
    version                         INT NOT NULL DEFAULT 1,
    effective_from                  DATE NOT NULL DEFAULT '2025-01-01',
    effective_to                    DATE,

    created_at                      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP NOT NULL DEFAULT NOW(),

    UNIQUE (policy_group_id, leave_type_id, version)
);

COMMENT ON TABLE  leave_policy_rules IS 'Normalized leave policy rules per policy group and leave type. All configurable; no hardcoded logic.';
COMMENT ON COLUMN leave_policy_rules.document_required_after_days IS '0 = no document ever required. 2 = doctor cert mandatory for sick leave exceeding 2 consecutive days.';
COMMENT ON COLUMN leave_policy_rules.approval_level  IS 'Role that must approve. Enum: SHIFT_SUPERVISOR | STORE_ADMIN | REGIONAL_ADMIN | HR | AUTO_APPROVED';
COMMENT ON COLUMN leave_policy_rules.approval_mode   IS 'Workflow mode. AUTO_APPROVE = no human action; SYSTEM_APPROVAL = triggered automatically for protected leave.';
COMMENT ON COLUMN leave_policy_rules.is_protected    IS 'TRUE → leave cannot be rejected. Sets approval_mode = SYSTEM_APPROVAL automatically.';
COMMENT ON COLUMN leave_policy_rules.lifetime_limit  IS 'Total number of times this leave can be approved over an employee''s lifetime. NULL = unlimited.';

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. LEAVE PAY RULES (Tiered salary during leave — replaces hardcoded UAE logic)
--    Multiple rows per policy rule create salary brackets.
--    Payroll engine reads these tiers instead of switching on country code.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leave_pay_rules (
    id                  BIGSERIAL PRIMARY KEY,
    policy_rule_id      BIGINT NOT NULL REFERENCES leave_policy_rules(id) ON DELETE CASCADE,
    tier_label          VARCHAR(50),              -- Optional label: "Full Pay", "Half Pay", "Unpaid"
    day_from            INT NOT NULL,             -- Inclusive start day of this tier (1-based)
    day_to              INT,                      -- Inclusive end day; NULL = open-ended
    pay_percentage      NUMERIC(5,2) NOT NULL     -- 100.00 | 50.00 | 0.00
                            CHECK (pay_percentage >= 0 AND pay_percentage <= 100),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE  leave_pay_rules IS 'Tiered pay schedules per leave policy rule. E.g. UAE Sick: days 1-15 → 100%, 16-45 → 50%, 46-90 → 0%.';
COMMENT ON COLUMN leave_pay_rules.day_from IS '1-based day index from the start of the leave instance (not the calendar year).';

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. STORE / COMPANY POLICY GROUP MAPPING
--    Associates companies and stores to a policy group code.
--    Overrides cascade: Store > Region > Company > Policy Group.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS entity_policy_group_mappings (
    id                  BIGSERIAL PRIMARY KEY,
    entity_type         VARCHAR(20) NOT NULL     -- COMPANY | REGION | STORE | EMPLOYEE
                            CHECK (entity_type IN ('COMPANY','REGION','STORE','EMPLOYEE')),
    entity_id           BIGINT NOT NULL,
    policy_group_id     BIGINT NOT NULL REFERENCES leave_policy_groups(id),
    effective_from      DATE NOT NULL DEFAULT CURRENT_DATE,
    effective_to        DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (entity_type, entity_id, effective_from)
);

COMMENT ON TABLE entity_policy_group_mappings IS 'Maps companies, regions, stores, or individual employees to a leave policy group. Resolution: Employee > Store > Region > Company > System Default.';

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. EMPLOYEE LEAVE BALANCE HISTORY (Year-end snapshots for audit)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employee_leave_balance_history (
    id                  BIGSERIAL PRIMARY KEY,
    employee_id         BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    leave_type_id       BIGINT NOT NULL REFERENCES leave_types(id) ON DELETE CASCADE,
    year                INT NOT NULL,
    snapshot_date       DATE NOT NULL DEFAULT CURRENT_DATE,
    opening             NUMERIC(7,2) NOT NULL DEFAULT 0,
    earned              NUMERIC(7,2) NOT NULL DEFAULT 0,   -- accrued during year
    manual_adjustments  NUMERIC(7,2) NOT NULL DEFAULT 0,
    used                NUMERIC(7,2) NOT NULL DEFAULT 0,
    pending             NUMERIC(7,2) NOT NULL DEFAULT 0,
    expired             NUMERIC(7,2) NOT NULL DEFAULT 0,   -- carry-forward that lapsed
    carry_forward       NUMERIC(7,2) NOT NULL DEFAULT 0,   -- rolled to next year
    encashed            NUMERIC(7,2) NOT NULL DEFAULT 0,
    closing             NUMERIC(7,2) NOT NULL DEFAULT 0,   -- computed: opening + earned + adjustments - used - expired - encashed
    notes               TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (employee_id, leave_type_id, year, snapshot_date)
);

COMMENT ON TABLE employee_leave_balance_history IS 'Year-end (or mid-year) leave balance snapshots per employee and leave type. Used for audit trails, HR reports, and carry-forward calculations.';

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. INDEXES FOR PERFORMANCE
-- ─────────────────────────────────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_leave_policy_rules_group_type
    ON leave_policy_rules (policy_group_id, leave_type_id);

CREATE INDEX IF NOT EXISTS idx_leave_pay_rules_policy_rule
    ON leave_pay_rules (policy_rule_id, day_from);

CREATE INDEX IF NOT EXISTS idx_entity_policy_group_entity
    ON entity_policy_group_mappings (entity_type, entity_id);

CREATE INDEX IF NOT EXISTS idx_leave_balance_history_emp_year
    ON employee_leave_balance_history (employee_id, year);

-- ─────────────────────────────────────────────────────────────────────────────
-- 8. SEED DATA — Policy Groups
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO leave_policy_groups (code, name, description, working_days, hours_per_day, hours_per_week)
VALUES
    ('INDIA', 'India Policy',
     'Applies to all India-based companies and stores. Monday to Saturday working week.',
     'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY', 8.00, 48.00),

    ('EU', 'European Union Policy',
     'Applies to all EU-based companies and stores (France, Germany, Italy, Spain, etc.). Monday to Friday working week.',
     'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY', 7.00, 35.00),

    ('UAE', 'UAE Policy',
     'Applies to all UAE-based companies and stores. Monday to Saturday working week.',
     'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY', 8.00, 48.00)
ON CONFLICT (code) DO UPDATE SET
    name        = EXCLUDED.name,
    description = EXCLUDED.description,
    working_days  = EXCLUDED.working_days,
    hours_per_day = EXCLUDED.hours_per_day,
    hours_per_week = EXCLUDED.hours_per_week,
    updated_at    = NOW();

-- ─────────────────────────────────────────────────────────────────────────────
-- 9. SEED DATA — Weekly Off Rules
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO weekly_off_rules (policy_group_id, day_of_week)
SELECT g.id, d.day_of_week
FROM leave_policy_groups g
CROSS JOIN (VALUES ('SUNDAY')) AS d(day_of_week)
WHERE g.code IN ('INDIA', 'UAE')
ON CONFLICT DO NOTHING;

INSERT INTO weekly_off_rules (policy_group_id, day_of_week)
SELECT g.id, d.day_of_week
FROM leave_policy_groups g
CROSS JOIN (VALUES ('SATURDAY'), ('SUNDAY')) AS d(day_of_week)
WHERE g.code = 'EU'
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 10. SEED DATA — Leave Policy Rules (INDIA Group)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO leave_policy_rules (
    policy_group_id, leave_type_id,
    default_entitlement, monthly_accrual, entitlement_unit,
    max_consecutive_days, max_per_year,
    min_notice_days, document_required_after_days,
    allow_half_day, minimum_leave_unit,
    carry_forward_allowed, carry_forward_limit, carry_forward_expiry_months,
    encashment_allowed, maximum_encashment_days, minimum_balance_for_encashment,
    approval_level, approval_mode,
    is_paid, is_protected, lifetime_limit,
    effective_from
)
SELECT
    (SELECT id FROM leave_policy_groups WHERE code = 'INDIA'),
    lt.id,
    -- default_entitlement
    CASE lt.code
        WHEN 'ANNUAL'      THEN 18
        WHEN 'SICK'        THEN 12
        WHEN 'CASUAL'      THEN 12
        WHEN 'PERSONAL'    THEN 3
        WHEN 'MARRIAGE'    THEN 5
        WHEN 'BEREAVEMENT' THEN 7
        WHEN 'MATERNITY'   THEN 182  -- 26 weeks × 7 days
        WHEN 'PATERNITY'   THEN 15
        WHEN 'ADOPTION'    THEN 84   -- 12 weeks × 7 days
        ELSE NULL
    END,
    -- monthly_accrual
    CASE lt.code WHEN 'ANNUAL' THEN 1.5 ELSE NULL END,
    -- entitlement_unit
    CASE lt.code
        WHEN 'MATERNITY' THEN 'CALENDAR_DAYS'
        WHEN 'ADOPTION'  THEN 'CALENDAR_DAYS'
        ELSE 'WORKING_DAYS'
    END,
    -- max_consecutive_days (NULL = no limit)
    NULL,
    -- max_per_year
    CASE lt.code
        WHEN 'ANNUAL'   THEN 18
        WHEN 'SICK'     THEN 12
        WHEN 'CASUAL'   THEN 12
        WHEN 'PERSONAL' THEN 3
        WHEN 'MARRIAGE' THEN 5
        WHEN 'BEREAVEMENT' THEN 7
        ELSE NULL
    END,
    -- min_notice_days
    CASE lt.code WHEN 'ANNUAL' THEN 7 ELSE 0 END,
    -- document_required_after_days
    CASE lt.code WHEN 'SICK' THEN 2 ELSE 0 END,
    -- allow_half_day
    TRUE,
    -- minimum_leave_unit
    CASE lt.code WHEN 'MATERNITY' THEN 1.0 WHEN 'PATERNITY' THEN 1.0 ELSE 0.5 END,
    -- carry_forward_allowed
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    -- carry_forward_limit
    CASE lt.code WHEN 'ANNUAL' THEN 10 ELSE NULL END,
    -- carry_forward_expiry_months
    CASE lt.code WHEN 'ANNUAL' THEN 6 ELSE NULL END,
    -- encashment_allowed
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    -- maximum_encashment_days
    CASE lt.code WHEN 'ANNUAL' THEN 10 ELSE NULL END,
    -- minimum_balance_for_encashment
    CASE lt.code WHEN 'ANNUAL' THEN 5 ELSE NULL END,
    -- approval_level
    CASE lt.code
        WHEN 'SICK'        THEN 'SHIFT_SUPERVISOR'
        WHEN 'CASUAL'      THEN 'SHIFT_SUPERVISOR'
        WHEN 'PERSONAL'    THEN 'SHIFT_SUPERVISOR'
        WHEN 'MATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'PATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'BEREAVEMENT' THEN 'AUTO_APPROVED'
        WHEN 'ADOPTION'    THEN 'AUTO_APPROVED'
        ELSE 'STORE_ADMIN'
    END,
    -- approval_mode
    CASE lt.code
        WHEN 'MATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'PATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM_APPROVAL'
        WHEN 'ADOPTION'    THEN 'SYSTEM_APPROVAL'
        ELSE 'MANAGER_APPROVAL'
    END,
    -- is_paid
    CASE lt.code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    -- is_protected
    CASE lt.code
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        WHEN 'BEREAVEMENT' THEN TRUE
        WHEN 'ADOPTION'    THEN TRUE
        ELSE FALSE
    END,
    -- lifetime_limit
    CASE lt.code WHEN 'MARRIAGE' THEN 1 ELSE NULL END,
    '2025-01-01'
FROM leave_types lt
WHERE lt.code IN ('ANNUAL','SICK','CASUAL','PERSONAL','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','ADOPTION','UNPAID')
ON CONFLICT (policy_group_id, leave_type_id, version) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 11. SEED DATA — Leave Policy Rules (EU Group)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO leave_policy_rules (
    policy_group_id, leave_type_id,
    default_entitlement, monthly_accrual, entitlement_unit,
    max_consecutive_days, max_per_year,
    min_notice_days, document_required_after_days,
    allow_half_day, minimum_leave_unit,
    carry_forward_allowed, carry_forward_limit, carry_forward_expiry_months,
    encashment_allowed, maximum_encashment_days, minimum_balance_for_encashment,
    approval_level, approval_mode,
    is_paid, is_protected, lifetime_limit,
    effective_from
)
SELECT
    (SELECT id FROM leave_policy_groups WHERE code = 'EU'),
    lt.id,
    CASE lt.code
        WHEN 'ANNUAL'      THEN 30
        WHEN 'SICK'        THEN NULL  -- EU: unlimited
        WHEN 'PERSONAL'    THEN 3
        WHEN 'MARRIAGE'    THEN 4
        WHEN 'BEREAVEMENT' THEN 14    -- up to 14 days (country-specific min 3; EU uses max)
        WHEN 'MATERNITY'   THEN NULL  -- country legal requirement
        WHEN 'PATERNITY'   THEN NULL  -- country legal requirement
        WHEN 'ADOPTION'    THEN NULL  -- country legal requirement
        ELSE NULL
    END,
    CASE lt.code WHEN 'ANNUAL' THEN 2.5 ELSE NULL END,
    CASE lt.code
        WHEN 'MATERNITY' THEN 'CALENDAR_DAYS'
        WHEN 'ADOPTION'  THEN 'CALENDAR_DAYS'
        ELSE 'WORKING_DAYS'
    END,
    NULL,
    NULL,  -- EU Sick: no year cap
    CASE lt.code WHEN 'ANNUAL' THEN 14 ELSE 0 END,
    CASE lt.code WHEN 'SICK' THEN 2 ELSE 0 END,
    TRUE,
    CASE lt.code WHEN 'MATERNITY' THEN 1.0 WHEN 'PATERNITY' THEN 1.0 ELSE 0.5 END,
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    CASE lt.code WHEN 'ANNUAL' THEN 5 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN 3 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    CASE lt.code WHEN 'ANNUAL' THEN 5 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN 5 ELSE NULL END,
    CASE lt.code
        WHEN 'SICK'        THEN 'SHIFT_SUPERVISOR'
        WHEN 'PERSONAL'    THEN 'SHIFT_SUPERVISOR'
        WHEN 'MATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'PATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'BEREAVEMENT' THEN 'AUTO_APPROVED'
        WHEN 'ADOPTION'    THEN 'AUTO_APPROVED'
        ELSE 'STORE_ADMIN'
    END,
    CASE lt.code
        WHEN 'MATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'PATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM_APPROVAL'
        WHEN 'ADOPTION'    THEN 'SYSTEM_APPROVAL'
        ELSE 'MANAGER_APPROVAL'
    END,
    CASE lt.code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    CASE lt.code
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        WHEN 'BEREAVEMENT' THEN TRUE
        WHEN 'ADOPTION'    THEN TRUE
        ELSE FALSE
    END,
    CASE lt.code WHEN 'MARRIAGE' THEN 1 ELSE NULL END,
    '2025-01-01'
FROM leave_types lt
WHERE lt.code IN ('ANNUAL','SICK','PERSONAL','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','ADOPTION','UNPAID')
ON CONFLICT (policy_group_id, leave_type_id, version) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 12. SEED DATA — Leave Policy Rules (UAE Group)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO leave_policy_rules (
    policy_group_id, leave_type_id,
    default_entitlement, monthly_accrual, entitlement_unit,
    max_consecutive_days, max_per_year,
    min_notice_days, document_required_after_days,
    allow_half_day, minimum_leave_unit,
    carry_forward_allowed, carry_forward_limit, carry_forward_expiry_months,
    encashment_allowed, maximum_encashment_days, minimum_balance_for_encashment,
    approval_level, approval_mode,
    is_paid, is_protected, lifetime_limit,
    effective_from
)
SELECT
    (SELECT id FROM leave_policy_groups WHERE code = 'UAE'),
    lt.id,
    CASE lt.code
        WHEN 'ANNUAL'      THEN 30
        WHEN 'SICK'        THEN 90
        WHEN 'PERSONAL'    THEN 3
        WHEN 'MARRIAGE'    THEN 5
        WHEN 'BEREAVEMENT' THEN 5     -- up to 5 days
        WHEN 'MATERNITY'   THEN 60
        WHEN 'PATERNITY'   THEN 5
        ELSE NULL
    END,
    CASE lt.code WHEN 'ANNUAL' THEN 2.5 ELSE NULL END,
    CASE lt.code
        WHEN 'ANNUAL'    THEN 'CALENDAR_DAYS'
        WHEN 'MATERNITY' THEN 'CALENDAR_DAYS'
        WHEN 'SICK'      THEN 'CALENDAR_DAYS'
        ELSE 'WORKING_DAYS'
    END,
    NULL,
    CASE lt.code
        WHEN 'ANNUAL'   THEN 30
        WHEN 'SICK'     THEN 90
        WHEN 'PERSONAL' THEN 3
        WHEN 'MARRIAGE' THEN 5
        WHEN 'BEREAVEMENT' THEN 5
        WHEN 'MATERNITY'   THEN 60
        WHEN 'PATERNITY'   THEN 5
        ELSE NULL
    END,
    CASE lt.code WHEN 'ANNUAL' THEN 30 ELSE 0 END,
    CASE lt.code WHEN 'SICK' THEN 2 ELSE 0 END,
    TRUE,
    CASE lt.code WHEN 'MATERNITY' THEN 1.0 WHEN 'PATERNITY' THEN 1.0 ELSE 0.5 END,
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    CASE lt.code WHEN 'ANNUAL' THEN 15 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN 6 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN TRUE ELSE FALSE END,
    CASE lt.code WHEN 'ANNUAL' THEN 15 ELSE NULL END,
    CASE lt.code WHEN 'ANNUAL' THEN 5 ELSE NULL END,
    CASE lt.code
        WHEN 'SICK'        THEN 'SHIFT_SUPERVISOR'
        WHEN 'PERSONAL'    THEN 'SHIFT_SUPERVISOR'
        WHEN 'MATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'PATERNITY'   THEN 'AUTO_APPROVED'
        WHEN 'BEREAVEMENT' THEN 'AUTO_APPROVED'
        ELSE 'STORE_ADMIN'
    END,
    CASE lt.code
        WHEN 'MATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'PATERNITY'   THEN 'SYSTEM_APPROVAL'
        WHEN 'BEREAVEMENT' THEN 'SYSTEM_APPROVAL'
        ELSE 'MANAGER_APPROVAL'
    END,
    CASE lt.code WHEN 'UNPAID' THEN FALSE ELSE TRUE END,
    CASE lt.code
        WHEN 'MATERNITY'   THEN TRUE
        WHEN 'PATERNITY'   THEN TRUE
        WHEN 'BEREAVEMENT' THEN TRUE
        ELSE FALSE
    END,
    CASE lt.code WHEN 'MARRIAGE' THEN 1 ELSE NULL END,
    '2025-01-01'
FROM leave_types lt
WHERE lt.code IN ('ANNUAL','SICK','PERSONAL','MARRIAGE','BEREAVEMENT','MATERNITY','PATERNITY','ADOPTION','UNPAID')
ON CONFLICT (policy_group_id, leave_type_id, version) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 13. SEED DATA — UAE Sick Leave Tiered Pay Rules
--     Day 1-15: Full Pay (100%)
--     Day 16-45: Half Pay (50%)
--     Day 46-90: Unpaid (0%)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO leave_pay_rules (policy_rule_id, tier_label, day_from, day_to, pay_percentage)
SELECT r.id, t.tier_label, t.day_from, t.day_to, t.pay_pct
FROM leave_policy_rules r
JOIN leave_policy_groups g ON g.id = r.policy_group_id
JOIN leave_types lt ON lt.id = r.leave_type_id
CROSS JOIN (VALUES
    ('Full Pay',  1, 15,  100.00),
    ('Half Pay', 16, 45,   50.00),
    ('Unpaid',   46, 90,    0.00)
) AS t(tier_label, day_from, day_to, pay_pct)
WHERE g.code = 'UAE' AND lt.code = 'SICK'
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 14. ADD policy_group_code TO EXISTING TABLES (non-breaking, nullable)
--     Allows companies and stores to declare their policy group inline.
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE companies
    ADD COLUMN IF NOT EXISTS leave_policy_group_code VARCHAR(30)
        REFERENCES leave_policy_groups(code);

-- If you have a stores table in the organization schema, add there too:
ALTER TABLE stores
    ADD COLUMN IF NOT EXISTS leave_policy_group_code VARCHAR(30)
        REFERENCES leave_policy_groups(code);
