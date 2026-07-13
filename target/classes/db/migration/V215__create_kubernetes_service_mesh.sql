-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 215
-- File              : V215__create_kubernetes_service_mesh.sql
-- Operation Type    : Schema Creation
-- Purpose           : create kubernetes service mesh
--
-- Tables Created    : IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V215: Kubernetes & Service Mesh DDL
CREATE TABLE IF NOT EXISTS platform_k8s_resource (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    resource_name       VARCHAR(100) NOT NULL,
    resource_type       VARCHAR(50) NOT NULL, -- Deployment, Service, ConfigMap, Ingress, etc
    namespace           VARCHAR(100) NOT NULL,
    manifest_yaml       TEXT,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(resource_name, resource_type, namespace)
);

CREATE TABLE IF NOT EXISTS platform_k8s_pod_status (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    pod_name            VARCHAR(100) NOT NULL UNIQUE,
    namespace           VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'CREATED', -- CREATED, PENDING, READY, SCALING, DRAINING, TERMINATING, FAILED
    node_ip             VARCHAR(100),
    restarts            INT NOT NULL DEFAULT 0,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_service_mesh_endpoint (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    service_name        VARCHAR(100) NOT NULL UNIQUE,
    sidecar_proxy_ip    VARCHAR(100) NOT NULL,
    mtls_enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    proxy_status        VARCHAR(50) NOT NULL DEFAULT 'CONNECTED'
);

CREATE TABLE IF NOT EXISTS platform_mesh_certificate (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    alias               VARCHAR(100) NOT NULL UNIQUE,
    issued_at           TIMESTAMP NOT NULL,
    expires_at          TIMESTAMP NOT NULL,
    issuer              VARCHAR(100) NOT NULL,
    rotation_date       TIMESTAMP NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'VALID'
);

CREATE TABLE IF NOT EXISTS platform_distributed_lock (
    id                  BIGSERIAL PRIMARY KEY,
    lock_name           VARCHAR(150) NOT NULL UNIQUE,
    owner_node          VARCHAR(100) NOT NULL,
    expires_at          TIMESTAMP NOT NULL,
    heartbeat           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
