-- V163: Enterprise Policy Management
CREATE TABLE IF NOT EXISTS grc_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    policy_code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_policy_versions (
    id BIGSERIAL PRIMARY KEY,
    policy_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    content_hash VARCHAR(64) NOT NULL,
    approved_at TIMESTAMP,
    published_at TIMESTAMP,
    UNIQUE(policy_id, version_number)
);

CREATE TABLE IF NOT EXISTS grc_policy_acknowledgements (
    id BIGSERIAL PRIMARY KEY,
    policy_version_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    acknowledged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(policy_version_id, employee_id)
);

CREATE TABLE IF NOT EXISTS grc_policy_exceptions (
    id BIGSERIAL PRIMARY KEY,
    policy_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    justification TEXT NOT NULL,
    approved_by_id BIGINT,
    expiry_date DATE NOT NULL
);
