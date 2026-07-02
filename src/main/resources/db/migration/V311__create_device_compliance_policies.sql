-- V311: Device Compliance Policies
CREATE TABLE IF NOT EXISTS platform_device_compliance_policy (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    policy_code             VARCHAR(100) NOT NULL UNIQUE,
    policy_name             VARCHAR(200) NOT NULL,
    policy_type             VARCHAR(100) NOT NULL,
    required_os             VARCHAR(100),
    minimum_agent_version   VARCHAR(50),
    required_packages       TEXT,
    required_services       TEXT,
    required_ports          VARCHAR(200),
    required_kernel_version VARCHAR(100),
    required_certificate    VARCHAR(200),
    severity                VARCHAR(50) NOT NULL, -- Low, Medium, High, Critical
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    created_by              VARCHAR(100) NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
