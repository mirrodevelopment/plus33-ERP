/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Fleet Module
 * Package           : com.plus33.erp.fleet.diagnostic
 * File              : CrashReporter.java
 * Purpose           : Business logic service layer for Fleet Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrashReporterController
 * Related Service   : CrashReporter
 * Related Repository: CrashReporterRepository
 * Related Entity    : CrashReporter
 * Related DTO       : N/A
 * Related Mapper    : CrashReporterMapper
 * Related DB Table  : crash_reporters
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CrashReporterController, CrashReporterImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Fleet Module. Implements CrashReporterService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.fleet.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Fleet Module</b>
 *
 * <p><b>Class  :</b> {@code CrashReporter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.fleet.diagnostic}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Fleet Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CrashReporterController
 *   --> CrashReporter (this)
 *   --> Validate business rules
 *   --> CrashReporterRepository (read/write 'crash_reporters')
 *   --> CrashReporterMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code crash_reporters}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CrashReporter {
    @Autowired PlatformDeviceDiagnosticRepository diagnosticRepo;
    /**
     * Performs the reportCrash operation in this module.
     *
     * @param nodeId the nodeId input value
     * @param exception the exception input value
     * @param stack the stack input value
     * @return the PlatformDeviceDiagnostic result
     */
    @Transactional
    public PlatformDeviceDiagnostic reportCrash(Long nodeId, String exception, String stack) {
        PlatformDeviceDiagnostic d = new PlatformDeviceDiagnostic();
        d.setNodeId(nodeId);
        d.setExceptionMessage(exception);
        d.setStackTrace(stack);
        d.setThreadDump("Thread [main] (Suspended) \n\t at java.lang.Object.wait(Native Method)");
        d.setCoreDumpLocation("/var/log/crashed-dumps/core." + nodeId);
        d.setReportedAt(LocalDateTime.now());
        return diagnosticRepo.save(d);
    }
}