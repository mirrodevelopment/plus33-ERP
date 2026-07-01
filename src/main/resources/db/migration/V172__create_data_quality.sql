-- V172: Data Quality, Lineage & SCD Infrastructure
-- Purpose: Validation rules, quality issues, data lineage tracking,
--          transformation versioning, and SCD change history.

-- -----------------------------------------------------------------------------
-- DATA QUALITY RULE: configurable validation rules per source/target
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_quality_rule (
    id                  BIGSERIAL PRIMARY KEY,
    rule_name           VARCHAR(200) NOT NULL UNIQUE,
    rule_type           VARCHAR(50) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    column_name         VARCHAR(100),
    rule_expression     TEXT NOT NULL,
    severity            VARCHAR(20) NOT NULL DEFAULT 'ERROR',
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DATA QUALITY RESULT: outcome per rule execution
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_quality_result (
    id                  BIGSERIAL PRIMARY KEY,
    rule_id             BIGINT NOT NULL REFERENCES bi_quality_rule(id),
    job_run_id          BIGINT REFERENCES bi_etl_job_run(id),
    batch_id            VARCHAR(100),
    records_checked     INTEGER NOT NULL DEFAULT 0,
    records_passed      INTEGER NOT NULL DEFAULT 0,
    records_failed      INTEGER NOT NULL DEFAULT 0,
    pass_rate           NUMERIC(5,2),
    status              VARCHAR(20) NOT NULL DEFAULT 'PASSED',
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DATA QUALITY ISSUE: individual data anomalies detected
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_quality_issue (
    id                  BIGSERIAL PRIMARY KEY,
    result_id           BIGINT NOT NULL REFERENCES bi_quality_result(id),
    source_table        VARCHAR(100) NOT NULL,
    source_record_id    BIGINT,
    column_name         VARCHAR(100),
    issue_type          VARCHAR(50) NOT NULL,
    issue_description   TEXT NOT NULL,
    raw_value           TEXT,
    severity            VARCHAR(20) NOT NULL DEFAULT 'ERROR',
    status              VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DATA LINEAGE: end-to-end provenance tracking
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_data_lineage (
    id                  BIGSERIAL PRIMARY KEY,
    lineage_key         VARCHAR(200) NOT NULL UNIQUE,
    source_module       VARCHAR(100) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    target_table        VARCHAR(100) NOT NULL,
    transformation_type VARCHAR(50) NOT NULL DEFAULT 'ETL',
    job_run_id          BIGINT REFERENCES bi_etl_job_run(id),
    batch_id            VARCHAR(100),
    records_processed   INTEGER DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- LINEAGE STEP: individual steps within a lineage chain
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_lineage_step (
    id                  BIGSERIAL PRIMARY KEY,
    lineage_id          BIGINT NOT NULL REFERENCES bi_data_lineage(id),
    step_order          INTEGER NOT NULL,
    step_name           VARCHAR(200) NOT NULL,
    step_type           VARCHAR(50) NOT NULL,
    input_table         VARCHAR(100),
    output_table        VARCHAR(100),
    records_in          INTEGER DEFAULT 0,
    records_out         INTEGER DEFAULT 0,
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- TRANSFORMATION RULE: versioned transformation logic definitions
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_transformation_rule (
    id                  BIGSERIAL PRIMARY KEY,
    rule_name           VARCHAR(200) NOT NULL,
    rule_version        INTEGER NOT NULL DEFAULT 1,
    source_table        VARCHAR(100) NOT NULL,
    target_table        VARCHAR(100) NOT NULL,
    rule_expression     TEXT NOT NULL,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_transform_rule_version UNIQUE (rule_name, rule_version)
);

-- -----------------------------------------------------------------------------
-- SCD CHANGE LOG: audit trail for Type 2 dimension changes
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_scd_change_log (
    id                  BIGSERIAL PRIMARY KEY,
    dimension_table     VARCHAR(100) NOT NULL,
    source_id           BIGINT NOT NULL,
    dim_id_expired      BIGINT,
    dim_id_created      BIGINT,
    change_type         VARCHAR(30) NOT NULL,
    changed_columns     TEXT,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    job_run_id          BIGINT REFERENCES bi_etl_job_run(id),
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DATASET FRESHNESS: per-dataset freshness monitoring
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dataset_freshness (
    id                  BIGSERIAL PRIMARY KEY,
    dataset_name        VARCHAR(200) NOT NULL UNIQUE,
    last_updated_at     TIMESTAMP,
    expected_refresh    VARCHAR(50),
    delay_minutes       INTEGER DEFAULT 0,
    health_status       VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN',
    checked_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
