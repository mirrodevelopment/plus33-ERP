-- V182: Data Catalog Schema
CREATE TABLE IF NOT EXISTS bi_catalog_dataset (
    id                  BIGSERIAL PRIMARY KEY,
    dataset_name        VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT,
    owner_role          VARCHAR(100) NOT NULL,
    steward_user        VARCHAR(100),
    certification_status VARCHAR(50) NOT NULL DEFAULT 'BRONZE',
    last_certified_at   TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_catalog_glossary (
    id                  BIGSERIAL PRIMARY KEY,
    term_code           VARCHAR(100) NOT NULL UNIQUE,
    term_name           VARCHAR(150) NOT NULL,
    definition          TEXT NOT NULL,
    calculation_rule    TEXT,
    domain_area         VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);