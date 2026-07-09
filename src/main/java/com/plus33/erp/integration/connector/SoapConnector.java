/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.connector
 * File              : SoapConnector.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SoapConnectorController
 * Related Service   : SoapConnectorService, SoapConnectorServiceImpl
 * Related Repository: SoapConnectorRepository
 * Related Entity    : SoapConnector
 * Related DTO       : N/A
 * Related Mapper    : SoapConnectorMapper
 * Related DB Table  : soap_connectors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;

@Component
public class SoapConnector implements Connector {
    /**
     * Retrieves connector type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves connector type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getConnectorType() { return "SOAP"; }

    /**
     * Performs the execute operation in this module.
     *
     * @param host the host input value
     * @param port the port input value
     * @param credentialRef the credentialRef input value
     * @param payload the payload input value
     * @return the ConnectorResult result
     */
    /**
     * Performs the execute operation in this module.
     *
     * @param host the host input value
     * @param port the port input value
     * @param credentialRef the credentialRef input value
     * @param payload the payload input value
     * @return the ConnectorResult result
     */
    @Override
    public ConnectorResult execute(String host, int port, String credentialRef, String payload) {
        return new ConnectorResult(true, "<response><status>SUCCESS</status></response>", null);
    }
}