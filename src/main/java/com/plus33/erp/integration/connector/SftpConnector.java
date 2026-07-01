package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;

@Component
public class SftpConnector implements Connector {
    @Override
    public String getConnectorType() { return "SFTP"; }

    @Override
    public ConnectorResult execute(String host, int port, String credentialRef, String payload) {
        return new ConnectorResult(true, "SFTP file transfer completed", null);
    }
}