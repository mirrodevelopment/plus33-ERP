/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Spatial Module
 * Package           : com.plus33.erp.spatial.analytics
 * File              : DeviancyDetector.java
 * Purpose           : Business logic service layer for Spatial Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DeviancyDetectorController
 * Related Service   : DeviancyDetector
 * Related Repository: DeviancyDetectorRepository
 * Related Entity    : DeviancyDetector
 * Related DTO       : N/A
 * Related Mapper    : DeviancyDetectorMapper
 * Related DB Table  : deviancy_detectors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : DeviancyDetectorController, DeviancyDetectorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Spatial Module. Implements DeviancyDetectorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.spatial.analytics;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Spatial Module</b>
 *
 * <p><b>Class  :</b> {@code DeviancyDetector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.spatial.analytics}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Spatial Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DeviancyDetectorController
 *   --> DeviancyDetector (this)
 *   --> Validate business rules
 *   --> DeviancyDetectorRepository (read/write 'deviancy_detectors')
 *   --> DeviancyDetectorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code deviancy_detectors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DeviancyDetector {
    @Autowired PlatformRouteDeviancyLogRepository deviancyRepo;
    /**
     * Performs the recordDeviancy operation in this module.
     *
     * @param routeId the routeId input value
     * @param expected the expected input value
     * @param actual the actual input value
     * @param dist the dist input value
     * @return the PlatformRouteDeviancyLog result
     */
    @Transactional
    public PlatformRouteDeviancyLog recordDeviancy(Long routeId, String expected, String actual, BigDecimal dist) {
        PlatformRouteDeviancyLog log = new PlatformRouteDeviancyLog();
        log.setTransitRouteId(routeId);
        log.setExpectedRouteWkt(expected);
        log.setActualRouteWkt(actual);
        log.setDeviationDistance(dist);
        log.setDeviationDurationMinutes(15);
        log.setSeverity("High");
        log.setAutomaticRecovery(true);
        log.setRerouteTriggered(false);
        log.setDetectedAt(LocalDateTime.now());
        return deviancyRepo.save(log);
    }
}