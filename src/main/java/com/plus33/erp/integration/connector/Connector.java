package com.plus33.erp.integration.connector;

public interface Connector {
    String getConnectorType();
    ConnectorResult execute(String host, int port, String credentialRef, String payload);
}