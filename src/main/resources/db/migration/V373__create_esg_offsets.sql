-- V373: Carbon Offsets Registry
CREATE TABLE IF NOT EXISTS platform_esg_carbon_offset (
    id                          BIGSERIAL PRIMARY KEY,
    offset_provider             VARCHAR(150) NOT NULL,
    certificate_number          VARCHAR(150) NOT NULL UNIQUE,
    verification_standard       VARCHAR(100) NOT NULL, -- GoldStandard, VCS
    credit_amount_t             NUMERIC(10,2) NOT NULL,
    retirement_date             TIMESTAMP NOT NULL,
    project_country             VARCHAR(100) NOT NULL,
    project_type                VARCHAR(100) NOT NULL, -- Reforestation, Renewable, BlueCarbon
    registered_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
