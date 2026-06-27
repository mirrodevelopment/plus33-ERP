-- V79__treasury_advanced_services.sql
-- Phase 8 enterprise additions: Journal Ledger, Risk Policies, Audit Events

-- ════════════════════════════════════════════════════════════════
-- 1. Treasury Journal Entries (Double-Entry Ledger)
-- ════════════════════════════════════════════════════════════════
CREATE TABLE treasury_journal_entries (
    id                   BIGSERIAL    PRIMARY KEY,
    company_id           BIGINT       NOT NULL REFERENCES companies(id),
    event_type           VARCHAR(60)  NOT NULL,           -- CASH_TRANSFER, FX_DEAL, INVESTMENT_PURCHASE, etc.
    event_id             BIGINT       NOT NULL,
    reference_number     VARCHAR(120) NOT NULL UNIQUE,    -- idempotency key
    account_id           BIGINT       NOT NULL REFERENCES chart_of_accounts(id),
    entry_type           VARCHAR(6)   NOT NULL CHECK (entry_type IN ('DEBIT', 'CREDIT')),
    amount               DECIMAL(20,6) NOT NULL,
    currency_code        VARCHAR(3)   NOT NULL,
    base_currency_amount DECIMAL(20,6) NOT NULL,
    base_currency_code   VARCHAR(3)   NOT NULL,
    exchange_rate        DECIMAL(18,6) NOT NULL DEFAULT 1.000000,
    posting_date         DATE         NOT NULL,
    value_date           DATE         NOT NULL,
    description          VARCHAR(500),
    posted_by            VARCHAR(100) NOT NULL,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tje_reference      ON treasury_journal_entries(reference_number);
CREATE INDEX idx_tje_event          ON treasury_journal_entries(event_type, event_id);
CREATE INDEX idx_tje_account        ON treasury_journal_entries(account_id, posting_date);
CREATE INDEX idx_tje_company_date   ON treasury_journal_entries(company_id, posting_date);

COMMENT ON TABLE treasury_journal_entries IS
    'Immutable double-entry GL ledger for all treasury events. Debits + Credits always balance per event.';

-- ════════════════════════════════════════════════════════════════
-- 2. Treasury Risk Policies (Configurable Policy Engine)
-- ════════════════════════════════════════════════════════════════
CREATE TABLE treasury_risk_policies (
    id                       BIGSERIAL     PRIMARY KEY,
    company_id               BIGINT        NOT NULL REFERENCES companies(id),
    policy_category          VARCHAR(50)   NOT NULL,
    -- PAYMENT | FX | INVESTMENT | COUNTERPARTY | BANK | LIQUIDITY | CONCENTRATION | REGULATORY
    policy_name              VARCHAR(100)  NOT NULL,
    -- e.g. SINGLE_PAYMENT, DAILY_OUTFLOW, CURRENCY_EXPOSURE, SINGLE_INVESTMENT
    currency_code            VARCHAR(3),
    bank_id                  BIGINT        REFERENCES banks(id),
    counterparty_ref         VARCHAR(100),
    investment_type          VARCHAR(50),
    limit_amount             DECIMAL(20,2) NOT NULL,
    warning_threshold_pct    DECIMAL(5,2)  NOT NULL DEFAULT 80.00
        CHECK (warning_threshold_pct BETWEEN 0 AND 100),
    breach_action            VARCHAR(20)   NOT NULL DEFAULT 'HARD_BLOCK'
        CHECK (breach_action IN ('HARD_BLOCK', 'SOFT_WARN', 'NOTIFY')),
    effective_from           DATE          NOT NULL,
    effective_to             DATE,
    active                   BOOLEAN       NOT NULL DEFAULT TRUE,
    created_by               VARCHAR(100)  NOT NULL,
    created_at               TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_trp_dates CHECK (effective_to IS NULL OR effective_to >= effective_from)
);

CREATE INDEX idx_trp_company_category ON treasury_risk_policies(company_id, policy_category);
CREATE INDEX idx_trp_active           ON treasury_risk_policies(active, effective_from);
CREATE INDEX idx_trp_lookup           ON treasury_risk_policies(company_id, policy_category, policy_name, currency_code);

COMMENT ON TABLE treasury_risk_policies IS
    'Configurable treasury risk policies. Replaces hard-coded limits. '
    'Most specific rule (currency-scoped) takes precedence over generic (null currency).';

-- ════════════════════════════════════════════════════════════════
-- 3. Treasury Audit Events (Immutable Event Log)
-- ════════════════════════════════════════════════════════════════
CREATE TABLE treasury_audit_events (
    id              BIGSERIAL     PRIMARY KEY,
    company_id      BIGINT        NOT NULL REFERENCES companies(id),
    aggregate_type  VARCHAR(80)   NOT NULL,   -- CashTransfer, PaymentBatch, TreasuryInvestment, etc.
    aggregate_id    BIGINT        NOT NULL,
    event_action    VARCHAR(80)   NOT NULL,   -- CREATED, STATUS_CHANGED, APPROVED, JOURNAL_POSTED, etc.
    previous_state  TEXT,
    new_state       TEXT,
    description     VARCHAR(500),
    actor           VARCHAR(100)  NOT NULL,
    actor_context   VARCHAR(200),
    occurred_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tae_aggregate    ON treasury_audit_events(aggregate_type, aggregate_id);
CREATE INDEX idx_tae_actor        ON treasury_audit_events(actor, occurred_at);
CREATE INDEX idx_tae_company      ON treasury_audit_events(company_id, occurred_at);

COMMENT ON TABLE treasury_audit_events IS
    'Immutable audit trail for every treasury action. Acts as a lightweight event-sourcing read model.';

-- ════════════════════════════════════════════════════════════════
-- 4. Seed default risk policies for demo companies
-- ════════════════════════════════════════════════════════════════
-- These seeds apply only if companies exist (safe for fresh installs)
INSERT INTO treasury_risk_policies
    (company_id, policy_category, policy_name, limit_amount, warning_threshold_pct,
     breach_action, effective_from, created_by)
SELECT c.id, 'PAYMENT', 'SINGLE_PAYMENT', 1000000.00, 80.00, 'HARD_BLOCK', CURRENT_DATE, 'SYSTEM'
FROM companies c
WHERE NOT EXISTS (
    SELECT 1 FROM treasury_risk_policies trp
    WHERE trp.company_id = c.id AND trp.policy_name = 'SINGLE_PAYMENT'
);

INSERT INTO treasury_risk_policies
    (company_id, policy_category, policy_name, limit_amount, warning_threshold_pct,
     breach_action, effective_from, created_by)
SELECT c.id, 'PAYMENT', 'DAILY_OUTFLOW', 5000000.00, 80.00, 'SOFT_WARN', CURRENT_DATE, 'SYSTEM'
FROM companies c
WHERE NOT EXISTS (
    SELECT 1 FROM treasury_risk_policies trp
    WHERE trp.company_id = c.id AND trp.policy_name = 'DAILY_OUTFLOW'
);

INSERT INTO treasury_risk_policies
    (company_id, policy_category, policy_name, limit_amount, warning_threshold_pct,
     breach_action, effective_from, created_by)
SELECT c.id, 'FX', 'SINGLE_TRADE', 500000.00, 80.00, 'HARD_BLOCK', CURRENT_DATE, 'SYSTEM'
FROM companies c
WHERE NOT EXISTS (
    SELECT 1 FROM treasury_risk_policies trp
    WHERE trp.company_id = c.id AND trp.policy_name = 'SINGLE_TRADE'
);

INSERT INTO treasury_risk_policies
    (company_id, policy_category, policy_name, limit_amount, warning_threshold_pct,
     breach_action, effective_from, created_by)
SELECT c.id, 'INVESTMENT', 'SINGLE_INVESTMENT', 2000000.00, 80.00, 'HARD_BLOCK', CURRENT_DATE, 'SYSTEM'
FROM companies c
WHERE NOT EXISTS (
    SELECT 1 FROM treasury_risk_policies trp
    WHERE trp.company_id = c.id AND trp.policy_name = 'SINGLE_INVESTMENT'
);
