-- V159: Enterprise Control Library
CREATE TABLE IF NOT EXISTS grc_control_library (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    control_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_control_mappings (
    id BIGSERIAL PRIMARY KEY,
    control_library_id BIGINT NOT NULL,
    framework_id BIGINT NOT NULL,
    control_ref VARCHAR(50) NOT NULL,
    UNIQUE(control_library_id, framework_id)
);
