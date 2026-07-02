-- V275: Secure SCADA Write Commands
CREATE TABLE IF NOT EXISTS platform_scada_write_command (
    id                  BIGSERIAL PRIMARY KEY,
    device_id           BIGINT NOT NULL,
    register_id         BIGINT NOT NULL,
    command_value       NUMERIC(19,4) NOT NULL,
    command_hash        VARCHAR(150) NOT NULL,
    approved_by         VARCHAR(100) NOT NULL,
    executed_by         VARCHAR(100) NOT NULL,
    signature           VARCHAR(250) NOT NULL,
    execution_time      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rollback_supported  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS platform_scada_audit_trail (
    id                  BIGSERIAL PRIMARY KEY,
    command_id          BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL,
    audit_hash          VARCHAR(150) NOT NULL,
    audited_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
