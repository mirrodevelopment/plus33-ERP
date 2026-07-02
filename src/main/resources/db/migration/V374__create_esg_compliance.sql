-- V374: ESG Compliance Logs
CREATE TABLE IF NOT EXISTS platform_esg_compliance_log (
    id                          BIGSERIAL PRIMARY KEY,
    framework                   VARCHAR(100) NOT NULL, -- GRI, SASB, CSRD
    compliance_score            NUMERIC(5,2) NOT NULL,
    status                      VARCHAR(50) NOT NULL, -- COMPLIANT, WARNING, NON_COMPLIANT
    finding_summary             TEXT NOT NULL,
    corrective_action           TEXT NOT NULL,
    owner_name                  VARCHAR(100) NOT NULL,
    next_review_date            TIMESTAMP NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
