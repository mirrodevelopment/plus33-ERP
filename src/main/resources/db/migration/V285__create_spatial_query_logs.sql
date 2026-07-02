-- V285: Spatial Query Logs
CREATE TABLE IF NOT EXISTS platform_spatial_query_log (
    id                  BIGSERIAL PRIMARY KEY,
    executed_query      TEXT NOT NULL,
    execution_time_ms   BIGINT NOT NULL,
    returned_rows       INT NOT NULL,
    spatial_index_used  VARCHAR(100),
    bounding_box_wkt    TEXT,
    query_type          VARCHAR(100) NOT NULL, -- BBOX, RADIUS, POLYGON_CONTAINMENT
    queried_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
