-- V144: Recruitment and ATS Job Requisitions
CREATE TABLE IF NOT EXISTS hcm_job_requisitions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    requisition_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hcm_job_requisition_versions (
    id BIGSERIAL PRIMARY KEY,
    requisition_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    effective_date DATE NOT NULL,
    UNIQUE(requisition_id, version_number)
);

CREATE TABLE IF NOT EXISTS hcm_candidates (
    id BIGSERIAL PRIMARY KEY,
    requisition_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED'
);
