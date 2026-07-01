-- V122: Advanced Procurement Requisitions and Purchase Orders
CREATE TABLE IF NOT EXISTS procurement_requisitions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    requisition_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUISITION_DRAFT',
    total_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
