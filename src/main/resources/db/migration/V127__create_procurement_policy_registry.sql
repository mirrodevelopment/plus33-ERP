-- V127: Procurement Policy Registry
CREATE TABLE IF NOT EXISTS procurement_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    policy_type VARCHAR(50) NOT NULL,
    threshold_amount NUMERIC(18, 2),
    preferred_supplier_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
