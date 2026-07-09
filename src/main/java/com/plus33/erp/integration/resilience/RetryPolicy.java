/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.resilience
 * File              : RetryPolicy.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RetryPolicyController
 * Related Service   : RetryPolicyService, RetryPolicyServiceImpl
 * Related Repository: RetryPolicyRepository
 * Related Entity    : RetryPolicy
 * Related DTO       : N/A
 * Related Mapper    : RetryPolicyMapper
 * Related DB Table  : retry_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.resilience;

public class RetryPolicy {
    private String name;
    private String type = "EXPONENTIAL"; // e.g. FIXED, LINEAR, EXPONENTIAL
    private int maxAttempts = 3;
    private long backoffMs = 1000;
    private double multiplier = 2.0;

    public RetryPolicy() {}

    public RetryPolicy(String name, String type, int maxAttempts, long backoffMs, double multiplier) {
        this.name = name;
        this.type = type;
        this.maxAttempts = maxAttempts;
        this.backoffMs = backoffMs;
        this.multiplier = multiplier;
    }

    /**
     * Calculates delay totals including subtotal, tax, discounts, and net amount.
     *
     * @param attempt the attempt input value
     * @return the numeric result value
     */
    public long calculateDelay(int attempt) {
        if ("FIXED".equalsIgnoreCase(type)) {
            return backoffMs;
        } else if ("LINEAR".equalsIgnoreCase(type)) {
            return backoffMs * attempt;
        } else {
            return (long) (backoffMs * Math.pow(multiplier, attempt - 1));
        }
    }

    // Getters and setters
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getType() { return type; }
    /**
     * Performs the setType operation in this module.
     *
     * @param type the type input value
     */
    public void setType(String type) { this.type = type; }
    /**
     * Retrieves max attempts data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getMaxAttempts() { return maxAttempts; }
    /**
     * Performs the setMaxAttempts operation in this module.
     *
     * @param maxAttempts the maxAttempts input value
     */
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    /**
     * Retrieves backoff ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getBackoffMs() { return backoffMs; }
    /**
     * Performs the setBackoffMs operation in this module.
     *
     * @param backoffMs the backoffMs input value
     */
    public void setBackoffMs(long backoffMs) { this.backoffMs = backoffMs; }
    /**
     * Retrieves multiplier data from the database.
     *
     * @return the double result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public double getMultiplier() { return multiplier; }
    /**
     * Performs the setMultiplier operation in this module.
     *
     * @param multiplier the multiplier input value
     */
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }
}