-- V315: Compliance Audits Logs
CREATE TABLE IF NOT EXISTS platform_compliance_audit_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    operator                VARCHAR(100) NOT NULL,
    action_type             VARCHAR(100) NOT NULL, -- UPDATE_POLICY, REMEDIATION, OVERRIDE
    previous_state          TEXT,
    new_state               TEXT,
    approval_id             VARCHAR(100),
    trace_id                VARCHAR(100) NOT NULL,
    reason                  VARCHAR(500),
    ip_address              VARCHAR(45),
    audited_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
