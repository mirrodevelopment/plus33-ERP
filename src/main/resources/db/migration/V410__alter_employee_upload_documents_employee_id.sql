-- Migration: Alter employee_upload_documents columns to BIGINT to match JPA validation
DROP TABLE IF EXISTS employee_upload_documents CASCADE;

CREATE TABLE employee_upload_documents (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    document_type VARCHAR(100) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
