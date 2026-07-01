-- V211: Cache Infrastructure DDL
CREATE TABLE IF NOT EXISTS platform_cache_node (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    node_code           VARCHAR(100) NOT NULL UNIQUE,
    ip_address          VARCHAR(100) NOT NULL,
    port                INT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'UP',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_cache_namespace (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    namespace_name      VARCHAR(100) NOT NULL UNIQUE,
    ttl_seconds         INT NOT NULL DEFAULT 3600,
    eviction_policy     VARCHAR(50) NOT NULL DEFAULT 'LRU',
    compression_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    serialization_type  VARCHAR(50) NOT NULL DEFAULT 'JSON'
);

CREATE TABLE IF NOT EXISTS platform_cache_region (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    region_name         VARCHAR(100) NOT NULL UNIQUE,
    replication_mode    VARCHAR(50) NOT NULL DEFAULT 'ASYNCHRONOUS'
);

CREATE TABLE IF NOT EXISTS platform_cache_invalidation_log (
    id                  BIGSERIAL PRIMARY KEY,
    namespace_name      VARCHAR(100) NOT NULL,
    cache_key           VARCHAR(250) NOT NULL,
    invalidated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_cache_warmup_job (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    target_version      VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'QUEUED', -- QUEUED, RUNNING, COMPLETED, FAILED
    preloaded_keys      INT NOT NULL DEFAULT 0,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP
);
