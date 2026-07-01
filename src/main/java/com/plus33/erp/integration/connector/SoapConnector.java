package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;

@Component
public class SoapConnector implements Connector {
    @Override
    public String getConnectorType() { return "SOAP"; }

    @Override
    public ConnectorResult execute(String host, int port, String credentialRef, String payload) {
        return new ConnectorResult(true, "<response><status>SUCCESS</status></response>", null);
    }
}