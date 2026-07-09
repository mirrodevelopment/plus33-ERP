/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.resilience
 * File              : Bulkhead.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BulkheadController
 * Related Service   : BulkheadService, BulkheadServiceImpl
 * Related Repository: BulkheadRepository
 * Related Entity    : Bulkhead
 * Related DTO       : N/A
 * Related Mapper    : BulkheadMapper
 * Related DB Table  : bulkheads
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.resilience;

import java.util.concurrent.Semaphore;

public class Bulkhead {
    private final String name;
    private final Semaphore semaphore;

    public Bulkhead(String name, int maxConcurrentCalls) {
        this.name = name;
        this.semaphore = new Semaphore(maxConcurrentCalls);
    }

    /**
     * Performs the tryAcquire operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    /**
     * Releases previously reserved integration resources back to the available pool.
     *
     */
    public void release() {
        semaphore.release();
    }

    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
}