-- V118: Service Billing State Machine
CREATE TABLE IF NOT EXISTS esm_billing_records (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    work_order_id BIGINT NOT NULL,
    billing_method VARCHAR(30) NOT NULL DEFAULT 'T_AND_M',
    amount NUMERIC(18, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'READY_TO_BILL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
