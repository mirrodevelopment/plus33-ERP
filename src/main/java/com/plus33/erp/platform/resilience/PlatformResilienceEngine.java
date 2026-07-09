/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.resilience
 * File              : PlatformResilienceEngine.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformResilienceEngineController
 * Related Service   : PlatformResilienceEngine
 * Related Repository: PlatformResilienceEngineRepository
 * Related Entity    : PlatformResilienceEngine
 * Related DTO       : N/A
 * Related Mapper    : PlatformResilienceEngineMapper
 * Related DB Table  : platform_resilience_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformResilienceEngineController, PlatformResilienceEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PlatformResilienceEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.resilience;

import com.plus33.erp.platform.entity.PlatformCircuitBreakerStats;
import com.plus33.erp.platform.repository.PlatformCircuitBreakerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformResilienceEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.resilience}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PlatformResilienceEngineController
 *   --> PlatformResilienceEngine (this)
 *   --> Validate business rules
 *   --> PlatformResilienceEngineRepository (read/write 'platform_resilience_engines')
 *   --> PlatformResilienceEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code platform_resilience_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PlatformResilienceEngine {
    @Autowired PlatformCircuitBreakerStatsRepository statsRepo;
    /**
     * Performs the recordTrip operation in this module.
     *
     * @param breakerName the breakerName input value
     * @param status status filter for narrowing query results
     * @param successRatio the successRatio input value
     */
    @Transactional
    public void recordTrip(String breakerName, String status, double successRatio) {
        PlatformCircuitBreakerStats stats = statsRepo.findAll().stream()
                .filter(s -> s.getBreakerName().equals(breakerName))
                .findFirst()
                .orElseGet(() -> {
                    PlatformCircuitBreakerStats newStats = new PlatformCircuitBreakerStats();
                    newStats.setBreakerName(breakerName);
                    return newStats;
                });

        stats.setStatus(status);
        stats.setSuccessRatio(BigDecimal.valueOf(successRatio));
        if ("OPEN".equals(status)) {
            stats.setFailuresCount(stats.getFailuresCount() + 1);
            stats.setLastTripTime(LocalDateTime.now());
        }
        statsRepo.save(stats);
    }
}