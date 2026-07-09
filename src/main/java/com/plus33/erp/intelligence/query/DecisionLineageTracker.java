/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.query
 * File              : DecisionLineageTracker.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DecisionLineageTrackerController
 * Related Service   : DecisionLineageTracker
 * Related Repository: DecisionLineageTrackerRepository
 * Related Entity    : DecisionLineageTracker
 * Related DTO       : N/A
 * Related Mapper    : DecisionLineageTrackerMapper
 * Related DB Table  : decision_lineage_trackers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : DecisionLineageTrackerController, DecisionLineageTrackerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements DecisionLineageTrackerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.query;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code DecisionLineageTracker}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.query}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DecisionLineageTrackerController
 *   --> DecisionLineageTracker (this)
 *   --> Validate business rules
 *   --> DecisionLineageTrackerRepository (read/write 'decision_lineage_trackers')
 *   --> DecisionLineageTrackerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code decision_lineage_trackers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DecisionLineageTracker {
    @Autowired PlatformXaiLineageRepository lineageRepo;
    /**
     * Performs the recordLineage operation in this module.
     *
     * @param key the key input value
     * @param factors the factors input value
     * @param ver the ver input value
     * @return the PlatformXaiLineage result
     */
    @Transactional
    public PlatformXaiLineage recordLineage(String key, String factors, String ver) {
        PlatformXaiLineage lin = new PlatformXaiLineage();
        lin.setDecisionKey(key);
        lin.setContributingFactors(factors);
        lin.setModelVersion(ver);
        lin.setCreatedAt(LocalDateTime.now());
        return lineageRepo.save(lin);
    }
}