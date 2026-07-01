-- V123: RFQ and RFQ Versioning
CREATE TABLE IF NOT EXISTS procurement_rfqs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    rfq_number VARCHAR(50) NOT NULL UNIQUE,
    current_version INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS procurement_rfq_versions (
    id BIGSERIAL PRIMARY KEY,
    rfq_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    effective_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(rfq_id, version_number)
);

CREATE TABLE IF NOT EXISTS procurement_supplier_responses (
    id BIGSERIAL PRIMARY KEY,
    rfq_version_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    bid_amount NUMERIC(18, 2) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
