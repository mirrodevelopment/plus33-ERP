/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.contract
 * File              : SchemaEvolutionTracker.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SchemaEvolutionTrackerController
 * Related Service   : SchemaEvolutionTracker
 * Related Repository: SchemaEvolutionTrackerRepository
 * Related Entity    : SchemaEvolutionTracker
 * Related DTO       : N/A
 * Related Mapper    : SchemaEvolutionTrackerMapper
 * Related DB Table  : schema_evolution_trackers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SchemaEvolutionTrackerController, SchemaEvolutionTrackerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements SchemaEvolutionTrackerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.contract;

import com.plus33.erp.bi.entity.BiSchemaEvolutionHistory;
import com.plus33.erp.bi.repository.BiSchemaEvolutionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code SchemaEvolutionTracker}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.contract}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SchemaEvolutionTrackerController
 *   --> SchemaEvolutionTracker (this)
 *   --> Validate business rules
 *   --> SchemaEvolutionTrackerRepository (read/write 'schema_evolution_trackers')
 *   --> SchemaEvolutionTrackerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code schema_evolution_trackers}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SchemaEvolutionTracker {

    @Autowired BiSchemaEvolutionHistoryRepository historyRepo;
    /**
     * Performs the logDrift operation in this module.
     *
     * @param tableName the tableName input value
     * @param actionType the actionType input value
     * @param detail the detail input value
     * @param status status filter for narrowing query results
     * @return the BiSchemaEvolutionHistory result
     */
    @Transactional
    public BiSchemaEvolutionHistory logDrift(String tableName, String actionType, String detail, String status) {
        BiSchemaEvolutionHistory h = new BiSchemaEvolutionHistory();
        h.setTableName(tableName);
        h.setActionType(actionType);
        h.setActionDetail(detail);
        h.setValidationStatus(status);
        h.setDetectedAt(LocalDateTime.now());
        return historyRepo.save(h);
    }
}