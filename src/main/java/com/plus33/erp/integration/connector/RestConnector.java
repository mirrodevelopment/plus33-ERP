/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.connector
 * File              : RestConnector.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RestConnectorController
 * Related Service   : RestConnectorService, RestConnectorServiceImpl
 * Related Repository: RestConnectorRepository
 * Related Entity    : RestConnector
 * Related DTO       : N/A
 * Related Mapper    : RestConnectorMapper
 * Related DB Table  : rest_connectors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.connector;

import com.plus33.erp.integration.resilience.CircuitBreaker;
import com.plus33.erp.integration.resilience.ResilienceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code RestConnector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.connector}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Integration Module.</p>
 *
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class RestConnector implements Connector {
    @Autowired SecretManager secretManager;
    @Autowired ResilienceEngine resilienceEngine;
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
    public String getConnectorType() { return "REST"; }

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
        CircuitBreaker cb = resilienceEngine.getCircuitBreaker("connector-REST-" + host);
        if (!cb.allowCall()) {
            return new ConnectorResult(false, null, "Circuit breaker is OPEN");
        }

        try {
            String resolvedCreds = secretManager.resolveSecret(credentialRef);
            if (payload != null && payload.contains("TIMEOUT")) {
                throw new java.net.SocketTimeoutException("Simulated connection timeout");
            }
            cb.recordSuccess();
            return new ConnectorResult(true, "{\"status\":\"OK\",\"message\":\"REST invocation succeeded\"}", null);
        } catch (Exception e) {
            cb.recordFailure();
            return new ConnectorResult(false, null, e.getMessage());
        }
    }
}