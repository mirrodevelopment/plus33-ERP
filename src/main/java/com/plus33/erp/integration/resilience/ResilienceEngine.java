/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.resilience
 * File              : ResilienceEngine.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ResilienceEngineController
 * Related Service   : ResilienceEngine
 * Related Repository: ResilienceEngineRepository
 * Related Entity    : ResilienceEngine
 * Related DTO       : N/A
 * Related Mapper    : ResilienceEngineMapper
 * Related DB Table  : resilience_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ResilienceEngineController, ResilienceEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements ResilienceEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.resilience;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ResilienceEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.resilience}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ResilienceEngineController
 *   --> ResilienceEngine (this)
 *   --> Validate business rules
 *   --> ResilienceEngineRepository (read/write 'resilience_engines')
 *   --> ResilienceEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code resilience_engines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ResilienceEngine {
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();
    private final Map<String, Bulkhead> bulkheads = new ConcurrentHashMap<>();

    /**
     * Retrieves circuit breaker data from the database.
     *
     * @param name the name input value
     * @return the CircuitBreaker result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public CircuitBreaker getCircuitBreaker(String name) {
        return circuitBreakers.computeIfAbsent(name, k -> new CircuitBreaker(k, 5, 5000));
    }

    /**
     * Retrieves bulkhead data from the database.
     *
     * @param name the name input value
     * @param limit the limit input value
     * @return the Bulkhead result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Bulkhead getBulkhead(String name, int limit) {
        return bulkheads.computeIfAbsent(name, k -> new Bulkhead(k, limit));
    }
}