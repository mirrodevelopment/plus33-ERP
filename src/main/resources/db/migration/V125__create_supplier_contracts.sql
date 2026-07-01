-- V125: Supplier Blanket Contracts and Amendment History
CREATE TABLE IF NOT EXISTS procurement_supplier_contracts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    contract_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    version_number INT NOT NULL DEFAULT 1,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS procurement_contract_amendments (
    id BIGSERIAL PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    amended_by BIGINT NOT NULL,
    amended_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);
