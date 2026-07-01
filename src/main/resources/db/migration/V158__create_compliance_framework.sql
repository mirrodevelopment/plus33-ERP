-- V158: Compliance Frameworks
CREATE TABLE IF NOT EXISTS grc_compliance_frameworks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS grc_compliance_controls (
    id BIGSERIAL PRIMARY KEY,
    framework_id BIGINT NOT NULL,
    control_ref VARCHAR(50) NOT NULL,
    objective TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner_id BIGINT
);
