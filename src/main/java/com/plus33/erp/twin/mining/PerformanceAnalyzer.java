/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.mining
 * File              : PerformanceAnalyzer.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PerformanceAnalyzerController
 * Related Service   : PerformanceAnalyzer
 * Related Repository: PerformanceAnalyzerRepository
 * Related Entity    : PerformanceAnalyzer
 * Related DTO       : N/A
 * Related Mapper    : PerformanceAnalyzerMapper
 * Related DB Table  : performance_analyzers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : PerformanceAnalyzerController, PerformanceAnalyzerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements PerformanceAnalyzerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code PerformanceAnalyzer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.mining}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PerformanceAnalyzerController
 *   --> PerformanceAnalyzer (this)
 *   --> Validate business rules
 *   --> PerformanceAnalyzerRepository (read/write 'performance_analyzers')
 *   --> PerformanceAnalyzerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code performance_analyzers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PerformanceAnalyzer {
    /**
     * Performs the analyzeAverageDuration operation in this module.
     *
     * @param activity the activity input value
     * @return the numeric result value
     */
    @Autowired PlatformProcessEventLogRepository eventLogRepo;
    public long analyzeAverageDuration(String activity) {
        return (long) eventLogRepo.findAll().stream()
                .filter(e -> e.getActivityName().equals(activity))
                .mapToLong(PlatformProcessEventLog::getDurationMs)
                .average()
                .orElse(0.0);
    }
}