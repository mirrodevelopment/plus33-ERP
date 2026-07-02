-- V272: SCADA Devices
CREATE TABLE IF NOT EXISTS platform_scada_device (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    device_code         VARCHAR(100) NOT NULL UNIQUE,
    device_type         VARCHAR(100) NOT NULL,
    opc_ua_namespace    VARCHAR(150),
    node_id             VARCHAR(150),
    plc_address         VARCHAR(100),
    modbus_unit_id      INT
);

CREATE TABLE IF NOT EXISTS platform_scada_signal_register (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    device_id           BIGINT NOT NULL,
    register_code       VARCHAR(100) NOT NULL UNIQUE,
    register_type       VARCHAR(50) NOT NULL, -- Holding Register, Input Register, Discrete Input, Coil
    scaling_factor      NUMERIC(5,2) NOT NULL DEFAULT 1.00
);
