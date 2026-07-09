/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.connector
 * File              : ConnectorResult.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConnectorResultController
 * Related Service   : ConnectorResultService, ConnectorResultServiceImpl
 * Related Repository: ConnectorResultRepository
 * Related Entity    : ConnectorResult
 * Related DTO       : N/A
 * Related Mapper    : ConnectorResultMapper
 * Related DB Table  : connector_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.connector;

public class ConnectorResult {
    private boolean success;
    private String responsePayload;
    private String errorMessage;

    public ConnectorResult(boolean success, String responsePayload, String errorMessage) {
        this.success = success;
        this.responsePayload = responsePayload;
        this.errorMessage = errorMessage;
    }

    /**
     * Performs the isSuccess operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isSuccess() { return success; }
    /**
     * Retrieves response payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResponsePayload() { return responsePayload; }
    /**
     * Retrieves error message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getErrorMessage() { return errorMessage; }
}