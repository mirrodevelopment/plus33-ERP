-- V312: Compliance & Drift Logs
CREATE TABLE IF NOT EXISTS platform_device_compliance_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    policy_id               BIGINT NOT NULL,
    result                  VARCHAR(50) NOT NULL, -- PASS, FAIL, WARNING, UNKNOWN
    execution_time          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_ms             BIGINT NOT NULL,
    details                 TEXT
);

CREATE TABLE IF NOT EXISTS platform_device_drift_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    baseline_hash           VARCHAR(64) NOT NULL,
    current_hash            VARCHAR(64) NOT NULL,
    changed_files           TEXT,
    registry_changes        TEXT,
    package_changes         TEXT,
    service_changes         TEXT,
    detected_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
