/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.anomaly
 * File              : AnomalyDetector.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AnomalyDetectorController
 * Related Service   : AnomalyDetector
 * Related Repository: AnomalyDetectorRepository
 * Related Entity    : AnomalyDetector
 * Related DTO       : N/A
 * Related Mapper    : AnomalyDetectorMapper
 * Related DB Table  : anomaly_detectors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : AnomalyDetectorController, AnomalyDetectorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements AnomalyDetectorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.anomaly;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code AnomalyDetector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.anomaly}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AnomalyDetectorController
 *   --> AnomalyDetector (this)
 *   --> Validate business rules
 *   --> AnomalyDetectorRepository (read/write 'anomaly_detectors')
 *   --> AnomalyDetectorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code anomaly_detectors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AnomalyDetector {
    @Autowired PlatformTwinAnomalyThresholdRepository thresholdRepo;
    @Autowired PlatformTwinAlertRepository alertRepo;
    /**
     * Validates business rules and constraints for anomaly.
     *
     * @param instanceId the instanceId input value
     * @param metric the metric input value
     * @param val the val input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void checkAnomaly(Long instanceId, String metric, BigDecimal val) {
        thresholdRepo.findAll().stream()
                .filter(t -> t.getInstanceId().equals(instanceId) && t.getMetricName().equals(metric))
                .findFirst()
                .ifPresent(t -> {
                    if (val.compareTo(t.getMinValue()) < 0 || val.compareTo(t.getMaxValue()) > 0) {
                        PlatformTwinAlert alert = new PlatformTwinAlert();
                        alert.setInstanceId(instanceId);
                        alert.setAlertType("OUT_OF_BOUNDS");
                        alert.setSeverity("CRITICAL");
                        alert.setMessage("Metric " + metric + " exceeded threshold limit: " + val);
                        alert.setTriggeredAt(LocalDateTime.now());
                        alertRepo.save(alert);
                    }
                });
    }
}