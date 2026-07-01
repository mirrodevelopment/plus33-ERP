-- V145: Employee Lifecycle and Document Expirations
CREATE TABLE IF NOT EXISTS hcm_employee_lifecycle (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL UNIQUE,
    lifecycle_status VARCHAR(30) NOT NULL DEFAULT 'CANDIDATE',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hcm_employee_documents (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL, -- PASSPORT, VISA, etc.
    document_number VARCHAR(50) NOT NULL,
    expiry_date DATE NOT NULL,
    notified BOOLEAN NOT NULL DEFAULT FALSE
);
