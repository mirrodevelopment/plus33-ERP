-- V252: Semantic Twin Relationships
CREATE TABLE IF NOT EXISTS platform_twin_relation (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    source_instance_id  BIGINT NOT NULL,
    target_instance_id  BIGINT NOT NULL,
    relationship_type   VARCHAR(100) NOT NULL, -- DependsOn, LocatedIn, ProducedBy, Consumes, Controls, DerivedFrom, OwnedBy
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
