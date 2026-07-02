-- V375: ESG Report Audits
CREATE TABLE IF NOT EXISTS platform_esg_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    report_version              VARCHAR(50) NOT NULL,
    report_hash                 VARCHAR(256) NOT NULL,
    generated_by                VARCHAR(100) NOT NULL,
    approved_by                 VARCHAR(100) NOT NULL,
    approval_date               TIMESTAMP NOT NULL,
    digital_signature           TEXT NOT NULL,
    trace_id                    VARCHAR(100) NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
