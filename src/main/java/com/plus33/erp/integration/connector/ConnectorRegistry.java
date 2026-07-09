/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.connector
 * File              : ConnectorRegistry.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConnectorRegistryController
 * Related Service   : ConnectorRegistryService, ConnectorRegistryServiceImpl
 * Related Repository: ConnectorRegistryRepository
 * Related Entity    : ConnectorRegistry
 * Related DTO       : N/A
 * Related Mapper    : ConnectorRegistryMapper
 * Related DB Table  : connector_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ConnectorRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.connector}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Integration Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves connector data from the database.
     *
     * @param type the type input value
     * @return the Connector result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Connector getConnector(String type) {
        return registry.get(type.toUpperCase());
    }
}