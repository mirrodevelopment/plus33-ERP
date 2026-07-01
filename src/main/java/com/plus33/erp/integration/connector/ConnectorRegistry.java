package com.plus33.erp.integration.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectorRegistry {
    private final Map<String, Connector> registry = new HashMap<>();

    @Autowired
    public ConnectorRegistry(RestConnector rest, SoapConnector soap, SftpConnector sftp, MqttConnector mqtt, WebhookConnector webhook) {
        registry.put(rest.getConnectorType(), rest);
        registry.put(soap.getConnectorType(), soap);
        registry.put(sftp.getConnectorType(), sftp);
        registry.put(mqtt.getConnectorType(), mqtt);
        registry.put(webhook.getConnectorType(), webhook);
    }

    public Connector getConnector(String type) {
        return registry.get(type.toUpperCase());
    }
}