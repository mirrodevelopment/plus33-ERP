-- V209: Tenant Routing DDL
CREATE TABLE IF NOT EXISTS platform_tenant_routing (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    tenant_id           VARCHAR(50) NOT NULL UNIQUE,
    region              VARCHAR(100) NOT NULL,
    routing_policy      VARCHAR(100) NOT NULL DEFAULT 'ACTIVE_ACTIVE', -- e.g. ACTIVE_ACTIVE, ACTIVE_PASSIVE, REGION_AFFINITY, DISASTER_RECOVERY
    replica_url         VARCHAR(250),
    healthy             BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
