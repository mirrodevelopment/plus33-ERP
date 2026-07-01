package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;

@Component
public class MqttConnector implements Connector {
    @Override
    public String getConnectorType() { return "MQTT"; }

    @Override
    public ConnectorResult execute(String host, int port, String credentialRef, String payload) {
        return new ConnectorResult(true, "MQTT message published", null);
    }
}