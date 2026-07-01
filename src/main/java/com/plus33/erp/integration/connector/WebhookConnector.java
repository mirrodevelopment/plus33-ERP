package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;

@Component
public class WebhookConnector implements Connector {
    @Override
    public String getConnectorType() { return "WEBHOOK"; }

    @Override
    public ConnectorResult execute(String host, int port, String credentialRef, String payload) {
        return new ConnectorResult(true, "Webhook triggered", null);
    }
}