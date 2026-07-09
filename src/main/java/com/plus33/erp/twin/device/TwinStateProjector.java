/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.device
 * File              : TwinStateProjector.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TwinStateProjectorController
 * Related Service   : TwinStateProjector
 * Related Repository: TwinStateProjectorRepository
 * Related Entity    : TwinStateProjector
 * Related DTO       : N/A
 * Related Mapper    : TwinStateProjectorMapper
 * Related DB Table  : twin_state_projectors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : TwinStateProjectorController, TwinStateProjectorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements TwinStateProjectorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.device;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.anomaly.AnomalyDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code TwinStateProjector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.device}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TwinStateProjectorController
 *   --> TwinStateProjector (this)
 *   --> Validate business rules
 *   --> TwinStateProjectorRepository (read/write 'twin_state_projectors')
 *   --> TwinStateProjectorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code twin_state_projectors}</p>
 * <p><b>Module Deps      :</b> Platform, Twin</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TwinStateProjector {
    @Autowired PlatformTwinStateRepository stateRepo;
    @Autowired AnomalyDetector anomalyDetector;
    /**
     * Performs the project operation in this module.
     *
     * @param instanceId the instanceId input value
     * @param metric the metric input value
     * @param val the val input value
     */
    @Transactional
    public void project(Long instanceId, String metric, BigDecimal val) {
        PlatformTwinState state = stateRepo.findAll().stream()
                .filter(s -> s.getInstanceId().equals(instanceId))
                .findFirst()
                .orElseGet(() -> {
                    PlatformTwinState s = new PlatformTwinState();
                    s.setInstanceId(instanceId);
                    return s;
                });

        state.setCurrentStateJson("{\"" + metric + "\":" + val + "}");
        state.setUpdatedAt(LocalDateTime.now());
        stateRepo.save(state);

        anomalyDetector.checkAnomaly(instanceId, metric, val);
    }
}