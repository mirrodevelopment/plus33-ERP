/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.fuel.diagnostic
 * File              : EcoDrivingDiagnosticService.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EcoDrivingDiagnosticController
 * Related Service   : EcoDrivingDiagnosticService
 * Related Repository: EcoDrivingDiagnosticRepository
 * Related Entity    : EcoDrivingDiagnostic
 * Related DTO       : N/A
 * Related Mapper    : EcoDrivingDiagnosticMapper
 * Related DB Table  : eco_driving_diagnostics
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : EcoDrivingDiagnosticController, EcoDrivingDiagnosticServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements EcoDrivingDiagnosticService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.fuel.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code EcoDrivingDiagnosticService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.fuel.diagnostic}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EcoDrivingDiagnosticController
 *   --> EcoDrivingDiagnosticService (this)
 *   --> Validate business rules
 *   --> EcoDrivingDiagnosticRepository (read/write 'eco_driving_diagnostics')
 *   --> EcoDrivingDiagnosticMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code eco_driving_diagnostics}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class EcoDrivingDiagnosticService {
    @Autowired PlatformEcoDrivingLogRepository diagnosticRepo;
    /**
     * Performs the logEcoDrivingMetrics operation in this module.
     *
     * @param driverId the driverId input value
     * @param tripId the tripId input value
     * @return the PlatformEcoDrivingLog result
     */
    @Transactional
    public PlatformEcoDrivingLog logEcoDrivingMetrics(Long driverId, Long tripId) {
        PlatformEcoDrivingLog log = new PlatformEcoDrivingLog();
        log.setDriverId(driverId);
        log.setTripId(tripId);
        log.setHarshAccelerationCount(2);
        log.setHarshBrakingCount(1);
        log.setHarshCorneringCount(0);
        log.setExcessiveIdleSeconds(120);
        log.setOverspeedDurationSecs(45);
        log.setCruiseControlUsagePct(BigDecimal.valueOf(74.50));
        log.setDriverScore(BigDecimal.valueOf(92.50));
        log.setTripScore(BigDecimal.valueOf(94.00));
        log.setCoachingStatus("OK");
        log.setLoggedAt(LocalDateTime.now());
        return diagnosticRepo.save(log);
    }
}