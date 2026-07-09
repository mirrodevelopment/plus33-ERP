/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.telemetry
 * File              : MachineryTelemetryService.java
 * Purpose           : Business logic service layer for Iot Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MachineryTelemetryController
 * Related Service   : MachineryTelemetryService
 * Related Repository: MachineryTelemetryRepository
 * Related Entity    : MachineryTelemetry
 * Related DTO       : N/A
 * Related Mapper    : MachineryTelemetryMapper
 * Related DB Table  : machinery_telemetrys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : MachineryTelemetryController, MachineryTelemetryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Iot Module. Implements MachineryTelemetryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.iot.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Iot Module</b>
 *
 * <p><b>Class  :</b> {@code MachineryTelemetryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.telemetry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Iot Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MachineryTelemetryController
 *   --> MachineryTelemetryService (this)
 *   --> Validate business rules
 *   --> MachineryTelemetryRepository (read/write 'machinery_telemetrys')
 *   --> MachineryTelemetryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code machinery_telemetrys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MachineryTelemetryService {
    @Autowired PlatformMachineryTelemetryRepository telemetryRepo;
    /**
     * Performs the ingest operation in this module.
     *
     * @param deviceId the deviceId input value
     * @param signalId the signalId input value
     * @param val the val input value
     * @param unit the unit input value
     * @param seq the seq input value
     * @return the PlatformMachineryTelemetry result
     */
    @Transactional
    public PlatformMachineryTelemetry ingest(Long deviceId, Long signalId, BigDecimal val, String unit, long seq) {
        PlatformMachineryTelemetry t = new PlatformMachineryTelemetry();
        t.setDeviceId(deviceId);
        t.setSignalId(signalId);
        t.setRecordedAt(LocalDateTime.now());
        t.setQuality("GOOD");
        t.setValue(val);
        t.setUnit(unit);
        t.setSequenceNum(seq);
        return telemetryRepo.save(t);
    }
}