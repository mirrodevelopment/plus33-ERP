-- V251: Knowledge Graph
CREATE TABLE IF NOT EXISTS platform_graph_node (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    node_type           VARCHAR(100) NOT NULL,
    business_key        VARCHAR(150) NOT NULL UNIQUE,
    display_name        VARCHAR(200) NOT NULL,
    module              VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    metadata_json       TEXT
);

CREATE TABLE IF NOT EXISTS platform_graph_edge (
    id                  BIGSERIAL PRIMARY KEY,
    source_node         BIGINT NOT NULL,
    target_node         BIGINT NOT NULL,
    relationship_type   VARCHAR(100) NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL DEFAULT 100.00,
    weight              NUMERIC(5,2) NOT NULL DEFAULT 1.00,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_edge_source FOREIGN KEY (source_node) REFERENCES platform_graph_node(id),
    CONSTRAINT fk_edge_target FOREIGN KEY (target_node) REFERENCES platform_graph_node(id)
);
