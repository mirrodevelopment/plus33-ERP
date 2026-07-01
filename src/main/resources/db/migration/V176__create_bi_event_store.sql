-- V176: BI Event Store & Audit Logs
-- Purpose: Immutable audit trail for all BI platform events, analytics query
--          history, approval audit trail, and BI alert engine tables.

-- -----------------------------------------------------------------------------
-- BI EVENT STORE: platform-wide audit log
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_event_store (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT,
    event_type          VARCHAR(100) NOT NULL,
    entity_type         VARCHAR(100),
    entity_id           BIGINT,
    payload_json        TEXT,
    event_version       VARCHAR(20) NOT NULL DEFAULT '1.0',
    correlation_id      VARCHAR(100),
    idempotency_key     VARCHAR(100) UNIQUE,
    performed_by        VARCHAR(100),
    occurred_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ANALYTICS QUERY HISTORY: user query audit for compliance and optimization
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_analytics_query_history (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT,
    user_name           VARCHAR(100) NOT NULL,
    dataset_name        VARCHAR(200),
    query_type          VARCHAR(50) NOT NULL,
    query_text          TEXT,
    rows_returned       INTEGER DEFAULT 0,
    duration_ms         BIGINT DEFAULT 0,
    filters_applied     TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- APPROVAL HISTORY: audit log for KPI, dataset, and dashboard approvals
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_approval_history (
    id                  BIGSERIAL PRIMARY KEY,
    entity_type         VARCHAR(100) NOT NULL,
    entity_id           BIGINT NOT NULL,
    from_status         VARCHAR(30) NOT NULL,
    to_status           VARCHAR(30) NOT NULL,
    decision            VARCHAR(20) NOT NULL,
    approver            VARCHAR(100) NOT NULL,
    decided_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_approval_comment (
    id                  BIGSERIAL PRIMARY KEY,
    approval_id         BIGINT NOT NULL REFERENCES bi_approval_history(id),
    comment_text        TEXT NOT NULL,
    commented_by        VARCHAR(100) NOT NULL,
    commented_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ALERT RULE: configurable threshold-based alerts
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_alert_rule (
    id                  BIGSERIAL PRIMARY KEY,
    rule_name           VARCHAR(200) NOT NULL UNIQUE,
    kpi_id              BIGINT REFERENCES bi_kpi_definition(id),
    dataset_name        VARCHAR(200),
    condition_type      VARCHAR(30) NOT NULL,
    threshold_value     NUMERIC(19,4) NOT NULL,
    severity            VARCHAR(20) NOT NULL DEFAULT 'WARNING',
    notification_type   VARCHAR(30) NOT NULL DEFAULT 'EMAIL',
    recipients          TEXT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ALERT TRIGGER: fired alert instances
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_alert_trigger (
    id                  BIGSERIAL PRIMARY KEY,
    rule_id             BIGINT NOT NULL REFERENCES bi_alert_rule(id),
    company_id          BIGINT,
    actual_value        NUMERIC(19,4),
    threshold_value     NUMERIC(19,4),
    severity            VARCHAR(20) NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    message             TEXT,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- EXPORT JOB: asynchronous report export queue
-- Lifecycle: REQUESTED -> QUEUED -> RUNNING -> COMPLETED -> DOWNLOADED
-- Exception: FAILED | CANCELLED
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_export_job (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    job_reference       VARCHAR(100) NOT NULL UNIQUE,
    report_name         VARCHAR(200) NOT NULL,
    export_format       VARCHAR(20) NOT NULL,
    parameters_json     TEXT,
    requested_by        VARCHAR(100) NOT NULL,
    status              VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    file_path           VARCHAR(500),
    file_size_bytes     BIGINT,
    error_message       TEXT,
    requested_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    expires_at          TIMESTAMP,
    downloaded_at       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_export_job_item (
    id                  BIGSERIAL PRIMARY KEY,
    export_job_id       BIGINT NOT NULL REFERENCES bi_export_job(id),
    section_name        VARCHAR(200) NOT NULL,
    dataset_name        VARCHAR(200),
    rows_exported       INTEGER DEFAULT 0,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
