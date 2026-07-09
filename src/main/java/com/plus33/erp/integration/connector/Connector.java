/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.connector
 * File              : Connector.java
 * Purpose           : Service interface contract defining the API for Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConnectorController
 * Related Service   : ConnectorService, ConnectorServiceImpl
 * Related Repository: ConnectorRepository
 * Related Entity    : Connector
 * Related DTO       : N/A
 * Related Mapper    : ConnectorMapper
 * Related DB Table  : connectors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.connector;

public interface Connector {
    String getConnectorType();
    ConnectorResult execute(String host, int port, String credentialRef, String payload);
}