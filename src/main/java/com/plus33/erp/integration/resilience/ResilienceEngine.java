package com.plus33.erp.integration.resilience;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResilienceEngine {
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();
    private final Map<String, Bulkhead> bulkheads = new ConcurrentHashMap<>();

    public CircuitBreaker getCircuitBreaker(String name) {
        return circuitBreakers.computeIfAbsent(name, k -> new CircuitBreaker(k, 5, 5000));
    }

    public Bulkhead getBulkhead(String name, int limit) {
        return bulkheads.computeIfAbsent(name, k -> new Bulkhead(k, limit));
    }
}