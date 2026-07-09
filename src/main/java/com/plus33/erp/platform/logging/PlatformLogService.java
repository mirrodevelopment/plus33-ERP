/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.logging
 * File              : PlatformLogService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformLogController
 * Related Service   : PlatformLogService
 * Related Repository: PlatformLogRepository
 * Related Entity    : PlatformLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformLogMapper
 * Related DB Table  : platform_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformLogController, PlatformLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PlatformLogService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.logging;

import com.plus33.erp.platform.entity.PlatformLogEntry;
import com.plus33.erp.platform.repository.PlatformLogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformLogService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.logging}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PlatformLogController
 *   --> PlatformLogService (this)
 *   --> Validate business rules
 *   --> PlatformLogRepository (read/write 'platform_logs')
 *   --> PlatformLogMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code platform_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PlatformLogService {
    @Autowired PlatformLogEntryRepository logRepo;
    /**
     * Performs the recordLog operation in this module.
     *
     * @param traceId the traceId input value
     * @param spanId the spanId input value
     * @param service the service input value
     * @param level the level input value
     * @param logger the logger input value
     * @param message the message input value
     */
    @Transactional
    public void recordLog(String traceId, String spanId, String service, String level, String logger, String message) {
        PlatformLogEntry entry = new PlatformLogEntry();
        entry.setTraceId(traceId);
        entry.setSpanId(spanId);
        entry.setServiceName(service);
        entry.setNodeId("node-host-1");
        entry.setLogLevel(level);
        entry.setLogger(logger);
        entry.setMessage(message);
        entry.setTimestamp(LocalDateTime.now());
        logRepo.save(entry);
    }
}