-- V102: Enterprise CRM and Omnichannel tables, event store, forecasts, and commissions
CREATE TABLE IF NOT EXISTS crm_leads (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    organization_name VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(30),
    status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    score INT NOT NULL DEFAULT 0,
    source VARCHAR(50),
    campaign_attribution VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_opportunities (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT,
    lead_id BIGINT,
    title VARCHAR(150) NOT NULL,
    stage VARCHAR(30) NOT NULL DEFAULT 'NEW',
    amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    probability NUMERIC(5, 2) NOT NULL DEFAULT 0,
    close_date DATE,
    owner_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_quotes (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    quote_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    opportunity_id BIGINT,
    active_version_number INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_quote_versions (
    id BIGSERIAL PRIMARY KEY,
    quote_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    subtotal NUMERIC(18, 2) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    tax_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    total_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_quote_version_lines (
    id BIGSERIAL PRIMARY KEY,
    quote_version_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity NUMERIC(18, 6) NOT NULL,
    unit_price NUMERIC(18, 2) NOT NULL,
    discount_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0,
    tax_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0,
    line_total NUMERIC(18, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS crm_territories (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    region_name VARCHAR(100) NOT NULL,
    postal_code_range VARCHAR(50),
    sales_rep_id BIGINT,
    effective_from DATE NOT NULL,
    effective_to DATE,
    version_number INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_timeline_events (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    description TEXT,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_commissions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    sales_rep_id BIGINT NOT NULL,
    opportunity_id BIGINT NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'CALCULATED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_event_store (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload_json TEXT NOT NULL,
    event_version VARCHAR(20) NOT NULL,
    schema_version VARCHAR(20) NOT NULL,
    correlation_id VARCHAR(100),
    causation_id VARCHAR(100),
    idempotency_key VARCHAR(100) UNIQUE,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crm_cases (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    case_number VARCHAR(50) NOT NULL UNIQUE,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    category VARCHAR(50),
    description TEXT,
    sla_breached BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
