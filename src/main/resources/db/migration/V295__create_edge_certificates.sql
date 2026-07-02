-- V295: Edge Certificate Rotation Logs
CREATE TABLE IF NOT EXISTS platform_edge_certificate_log (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT NOT NULL,
    certificate_serial  VARCHAR(100) NOT NULL,
    issuer              VARCHAR(200) NOT NULL,
    valid_from          TIMESTAMP NOT NULL,
    valid_to            TIMESTAMP NOT NULL,
    rotation_reason     VARCHAR(200),
    algorithm           VARCHAR(50) NOT NULL DEFAULT 'RSA',
    key_length          INT NOT NULL DEFAULT 2048,
    rotation_status     VARCHAR(50) NOT NULL, -- PENDING, COMPLETED, FAILED
    rotated_by          VARCHAR(100) NOT NULL,
    rotated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
