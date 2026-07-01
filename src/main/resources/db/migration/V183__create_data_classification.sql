-- V183: Data Classification & Privacy Governance Schema
CREATE TABLE IF NOT EXISTS bi_governance_classification (
    id                  BIGSERIAL PRIMARY KEY,
    table_name          VARCHAR(100) NOT NULL,
    column_name         VARCHAR(100) NOT NULL,
    classification_level VARCHAR(50) NOT NULL,
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(table_name, column_name)
);

CREATE TABLE IF NOT EXISTS bi_governance_masking_rule (
    id                  BIGSERIAL PRIMARY KEY,
    rule_name           VARCHAR(100) NOT NULL UNIQUE,
    classification_level VARCHAR(50) NOT NULL,
    masking_type        VARCHAR(50) NOT NULL,
    masking_pattern     VARCHAR(100),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);