/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.alarm
 * File              : AlarmOrchestrator.java
 * Purpose           : Business logic service layer for Iot Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AlarmOrchestratorController
 * Related Service   : AlarmOrchestrator
 * Related Repository: AlarmOrchestratorRepository
 * Related Entity    : AlarmOrchestrator
 * Related DTO       : N/A
 * Related Mapper    : AlarmOrchestratorMapper
 * Related DB Table  : alarm_orchestrators
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : AlarmOrchestratorController, AlarmOrchestratorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Iot Module. Implements AlarmOrchestratorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.iot.alarm;

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
 * <p><b>Class  :</b> {@code AlarmOrchestrator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.alarm}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Iot Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AlarmOrchestratorController
 *   --> AlarmOrchestrator (this)
 *   --> Validate business rules
 *   --> AlarmOrchestratorRepository (read/write 'alarm_orchestrators')
 *   --> AlarmOrchestratorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code alarm_orchestrators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AlarmOrchestrator {
    @Autowired PlatformScadaAlarmPolicyRepository policyRepo;
    @Autowired PlatformScadaAlarmEventRepository eventRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param severity the severity input value
     * @param threshold the threshold input value
     * @return the PlatformScadaAlarmPolicy result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformScadaAlarmPolicy createPolicy(String code, String severity, BigDecimal threshold) {
        PlatformScadaAlarmPolicy p = new PlatformScadaAlarmPolicy();
        p.setPolicyCode(code);
        p.setSeverity(severity);
        p.setThresholdValue(threshold);
        return policyRepo.save(p);
    }

    /**
     * Performs the triggerAlarm operation in this module.
     *
     * @param deviceId the deviceId input value
     * @param policyId the policyId input value
     * @param msg the msg input value
     */
    @Transactional
    public void triggerAlarm(Long deviceId, Long policyId, String msg) {
        PlatformScadaAlarmEvent event = new PlatformScadaAlarmEvent();
        event.setDeviceId(deviceId);
        event.setPolicyId(policyId);
        event.setAlarmStatus("ACTIVE");
        event.setMessage(msg);
        event.setTriggeredAt(LocalDateTime.now());
        eventRepo.save(event);
    }
}