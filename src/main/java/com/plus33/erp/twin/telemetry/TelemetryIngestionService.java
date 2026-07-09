/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.telemetry
 * File              : TelemetryIngestionService.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TelemetryIngestionController
 * Related Service   : TelemetryIngestionService
 * Related Repository: TelemetryIngestionRepository
 * Related Entity    : TelemetryIngestion
 * Related DTO       : N/A
 * Related Mapper    : TelemetryIngestionMapper
 * Related DB Table  : telemetry_ingestions
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : TelemetryIngestionController, TelemetryIngestionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements TelemetryIngestionService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.device.TwinStateProjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code TelemetryIngestionService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.telemetry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TelemetryIngestionController
 *   --> TelemetryIngestionService (this)
 *   --> Validate business rules
 *   --> TelemetryIngestionRepository (read/write 'telemetry_ingestions')
 *   --> TelemetryIngestionMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code telemetry_ingestions}</p>
 * <p><b>Module Deps      :</b> Platform, Twin</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TelemetryIngestionService {
    @Autowired TelemetryValidator validator;
    @Autowired TelemetryNormalizer normalizer;
    @Autowired TwinStateProjector projector;
    @Autowired PlatformTwinTelemetryRepository telemetryRepo;
    /**
     * Performs the ingest operation in this module.
     *
     * @param instanceId the instanceId input value
     * @param name the name input value
     * @param val the val input value
     */
    @Transactional
    public void ingest(Long instanceId, String name, BigDecimal val) {
        if (!validator.isValid(name, val)) {
            throw new IllegalArgumentException("Invalid telemetry data");
        }
        BigDecimal normalizedVal = normalizer.normalize(name, val);

        PlatformTwinTelemetry telemetry = new PlatformTwinTelemetry();
        telemetry.setInstanceId(instanceId);
        telemetry.setMetricName(name);
        telemetry.setMetricValue(normalizedVal);
        telemetry.setRecordedAt(LocalDateTime.now());
        telemetryRepo.save(telemetry);

        projector.project(instanceId, name, normalizedVal);
    }
}