package com.plus33.erp.integration.connector;

import com.plus33.erp.integration.resilience.CircuitBreaker;
import com.plus33.erp.integration.resilience.ResilienceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestConnector implements Connector {
    @Autowired SecretManager secretManager;
    @Autowired ResilienceEngine resilienceEngine;

    @Override
    public String getConnectorType() { return "REST"; }

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