/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.query
 * File              : NaturalLanguageQueryService.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: NaturalLanguageQueryController
 * Related Service   : NaturalLanguageQueryService
 * Related Repository: NaturalLanguageQueryRepository
 * Related Entity    : NaturalLanguageQuery
 * Related DTO       : N/A
 * Related Mapper    : NaturalLanguageQueryMapper
 * Related DB Table  : natural_language_querys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : NaturalLanguageQueryController, NaturalLanguageQueryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements NaturalLanguageQueryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
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
 * <p><b>Class  :</b> {@code NaturalLanguageQueryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.query}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * NaturalLanguageQueryController
 *   --> NaturalLanguageQueryService (this)
 *   --> Validate business rules
 *   --> NaturalLanguageQueryRepository (read/write 'natural_language_querys')
 *   --> NaturalLanguageQueryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code natural_language_querys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class NaturalLanguageQueryService {
    @Autowired PlatformOperationalQueryLogRepository logRepo;
    @Autowired QueryPlanner queryPlanner;
    /**
     * Performs the executeQuery operation in this module.
     *
     * @param queryText the queryText input value
     * @return the PlatformOperationalQueryLog result
     */
    @Transactional
    public PlatformOperationalQueryLog executeQuery(String queryText) {
        String plan = queryPlanner.planQuery(queryText);

        PlatformOperationalQueryLog log = new PlatformOperationalQueryLog();
        log.setQueryText(queryText);
        log.setParsedIntent("QUERY_INTENT_DISCOVERY");
        log.setExecutionPlanJson(plan);
        log.setQueriedAt(LocalDateTime.now());
        return logRepo.save(log);
    }
}