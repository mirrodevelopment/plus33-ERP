-- V186: Schema Data Contracts Governance DDL
CREATE TABLE IF NOT EXISTS bi_data_contract (
    id                  BIGSERIAL PRIMARY KEY,
    contract_name       VARCHAR(100) NOT NULL UNIQUE,
    version_offset      INTEGER NOT NULL DEFAULT 1,
    schema_definition   TEXT NOT NULL,
    compatibility_level VARCHAR(50) NOT NULL DEFAULT 'FULL',
    status              VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    created_by          VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);