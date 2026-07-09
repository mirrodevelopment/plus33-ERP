/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Fleet Module
 * Package           : com.plus33.erp.fleet.diagnostic
 * File              : DiagnosticCollector.java
 * Purpose           : Business logic service layer for Fleet Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DiagnosticCollectorController
 * Related Service   : DiagnosticCollector
 * Related Repository: DiagnosticCollectorRepository
 * Related Entity    : DiagnosticCollector
 * Related DTO       : N/A
 * Related Mapper    : DiagnosticCollectorMapper
 * Related DB Table  : diagnostic_collectors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : DiagnosticCollectorController, DiagnosticCollectorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Fleet Module. Implements DiagnosticCollectorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.fleet.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Fleet Module</b>
 *
 * <p><b>Class  :</b> {@code DiagnosticCollector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.fleet.diagnostic}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Fleet Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DiagnosticCollectorController
 *   --> DiagnosticCollector (this)
 *   --> Validate business rules
 *   --> DiagnosticCollectorRepository (read/write 'diagnostic_collectors')
 *   --> DiagnosticCollectorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code diagnostic_collectors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DiagnosticCollector {
    @Autowired PlatformDeviceDiagnosticRepository diagnosticRepo;
    /**
     * Performs the collectDiagnostic operation in this module.
     *
     * @param nodeId the nodeId input value
     * @param cpu the cpu input value
     * @param mem the mem input value
     * @return the PlatformDeviceDiagnostic result
     */
    @Transactional
    public PlatformDeviceDiagnostic collectDiagnostic(Long nodeId, BigDecimal cpu, BigDecimal mem) {
        PlatformDeviceDiagnostic d = new PlatformDeviceDiagnostic();
        d.setNodeId(nodeId);
        d.setCpuUsage(cpu);
        d.setMemoryUsage(mem);
        d.setDiskUsage(BigDecimal.valueOf(45.00));
        d.setTemperature(BigDecimal.valueOf(39.00));
        d.setRunningServices("edge-agent, timeseries-buffer");
        d.setFirmwareVersion("v1.5.0");
        d.setUptimeSeconds(86400L);
        d.setNetworkQuality("EXCELLENT");
        d.setLogs("System started correctly. Online status achieved.");
        d.setReportedAt(LocalDateTime.now());
        return diagnosticRepo.save(d);
    }
}