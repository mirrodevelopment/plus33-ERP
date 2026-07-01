-- V245: Conformance rules & SLA tracking
CREATE TABLE IF NOT EXISTS platform_conformance_rule (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    process_name        VARCHAR(150) NOT NULL,
    expected_activity   VARCHAR(150) NOT NULL,
    sequence_order      INT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_conformance_deviation (
    id                  BIGSERIAL PRIMARY KEY,
    case_id             BIGINT NOT NULL,
    rule_id             BIGINT NOT NULL,
    deviation_details   TEXT NOT NULL,
    sla_breach_risk     BOOLEAN NOT NULL DEFAULT FALSE,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
