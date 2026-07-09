/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.notification
 * File              : AlertEscalationService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AlertEscalationController
 * Related Service   : AlertEscalationService
 * Related Repository: AlertEscalationRepository
 * Related Entity    : AlertEscalation
 * Related DTO       : N/A
 * Related Mapper    : AlertEscalationMapper
 * Related DB Table  : alert_escalations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AlertEscalationController, AlertEscalationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements AlertEscalationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.notification;

import com.plus33.erp.platform.entity.PlatformAnomalyAlert;
import com.plus33.erp.platform.repository.PlatformAnomalyAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code AlertEscalationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.notification}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AlertEscalationController
 *   --> AlertEscalationService (this)
 *   --> Validate business rules
 *   --> AlertEscalationRepository (read/write 'alert_escalations')
 *   --> AlertEscalationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code alert_escalations}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AlertEscalationService {
    @Autowired PlatformAnomalyAlertRepository alertRepo;
    /**
     * Performs the triggerAlert operation in this module.
     *
     * @param name the name input value
     * @param severity the severity input value
     * @param message the message input value
     */
    @Transactional
    public void triggerAlert(String name, String severity, String message) {
        PlatformAnomalyAlert alert = new PlatformAnomalyAlert();
        alert.setAlertName(name);
        alert.setSeverity(severity);
        alert.setStatus("ACTIVE");
        alert.setTriggerMessage(message);
        alert.setTimestamp(LocalDateTime.now());
        alertRepo.save(alert);
        
        // Simulating Escalation Level notifications L1 -> L2 -> L3
        System.out.println("[Alert Escalation Service] L1 notification triggered for alert: " + name);
    }
}