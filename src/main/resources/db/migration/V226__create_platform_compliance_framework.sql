-- V226: Compliance Framework DDL
CREATE TABLE IF NOT EXISTS platform_compliance_framework (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    framework_code      VARCHAR(100) NOT NULL UNIQUE, -- ISO27001, SOC2, PCI_DSS
    framework_name      VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_compliance_control (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    framework_id        BIGINT NOT NULL,
    control_code        VARCHAR(100) NOT NULL,
    control_name        VARCHAR(250) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'COMPLIANT'
);
