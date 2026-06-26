-- ============================================================
-- V64__create_ar_module.sql
-- PLUS33 ERP — Accounts Receivable Module Schema
-- ============================================================

-- 1. Sequence for write-off reference numbers (WO-YYYY-000001)
CREATE SEQUENCE IF NOT EXISTS ar_write_off_seq START WITH 1 INCREMENT BY 1;

-- 2. ar_write_offs table
--    Records each bad-debt write-off as an immutable accounting event.
--    Invoice status is NOT changed; the invoice outstanding_balance is
--    reduced to reflect the accounting treatment.
CREATE TABLE ar_write_offs (
    id                  BIGSERIAL PRIMARY KEY,
    write_off_number    VARCHAR(50)     NOT NULL,
    company_id          BIGINT          NOT NULL,
    customer_invoice_id BIGINT          NOT NULL,
    customer_id         BIGINT          NOT NULL,
    write_off_amount    DECIMAL(14,2)   NOT NULL,
    write_off_date      DATE            NOT NULL,
    reason              TEXT,
    journal_entry_id    BIGINT,
    written_off_by      BIGINT          NOT NULL,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_arwo_company   FOREIGN KEY (company_id)          REFERENCES companies(id),
    CONSTRAINT fk_arwo_invoice   FOREIGN KEY (customer_invoice_id) REFERENCES customer_invoices(id),
    CONSTRAINT fk_arwo_customer  FOREIGN KEY (customer_id)         REFERENCES customers(id),
    CONSTRAINT fk_arwo_journal   FOREIGN KEY (journal_entry_id)    REFERENCES journal_entries(id),
    CONSTRAINT fk_arwo_user      FOREIGN KEY (written_off_by)      REFERENCES users(id),

    CONSTRAINT uk_arwo_company_number UNIQUE (company_id, write_off_number),
    CONSTRAINT chk_arwo_amount        CHECK  (write_off_amount > 0)
);

-- 3. Seed Bad Debt Expense account (5300) under Expenses (5000) for all companies
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '5300', 'Bad Debt Expense', 'EXPENSE', parent.id, TRUE
FROM   companies c
JOIN   chart_of_accounts parent
       ON parent.company_id = c.id AND parent.account_code = '5000'
ON CONFLICT (company_id, account_code) DO NOTHING;

-- 4. Performance indexes
CREATE INDEX IF NOT EXISTS idx_ar_write_offs_company  ON ar_write_offs(company_id);
CREATE INDEX IF NOT EXISTS idx_ar_write_offs_customer ON ar_write_offs(customer_id);
CREATE INDEX IF NOT EXISTS idx_ar_write_offs_invoice  ON ar_write_offs(customer_invoice_id);
CREATE INDEX IF NOT EXISTS idx_ar_write_offs_date     ON ar_write_offs(write_off_date);
