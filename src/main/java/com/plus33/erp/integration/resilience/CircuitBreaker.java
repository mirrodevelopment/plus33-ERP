/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.resilience
 * File              : CircuitBreaker.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CircuitBreakerController
 * Related Service   : CircuitBreakerService, CircuitBreakerServiceImpl
 * Related Repository: CircuitBreakerRepository
 * Related Entity    : CircuitBreaker
 * Related DTO       : N/A
 * Related Mapper    : CircuitBreakerMapper
 * Related DB Table  : circuit_breakers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.resilience;

import java.time.LocalDateTime;

public class CircuitBreaker {
    private String name;
    private String state = "CLOSED"; // CLOSED, OPEN, HALF_OPEN
    private int failureThreshold = 5;
    private long cooldownMs = 5000;
    private int failureCount = 0;
    private LocalDateTime openTime;

    public CircuitBreaker(String name, int failureThreshold, long cooldownMs) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.cooldownMs = cooldownMs;
    }

    /**
     * Performs the allowCall operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public synchronized boolean allowCall() {
        if ("OPEN".equals(state)) {
            if (openTime != null && LocalDateTime.now().isAfter(openTime.plusNanos(cooldownMs * 1_000_000L))) {
                state = "HALF_OPEN";
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Performs the recordSuccess operation in this module.
     *
     * @return the synchronized void result
     */
    public synchronized void recordSuccess() {
        failureCount = 0;
        state = "CLOSED";
        openTime = null;
    }

    /**
     * Performs the recordFailure operation in this module.
     *
     * @return the synchronized void result
     */
    public synchronized void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = "OPEN";
            openTime = LocalDateTime.now();
        }
    }

    // Getters
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Retrieves state data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getState() { return state; }
    /**
     * Retrieves failure count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getFailureCount() { return failureCount; }
}