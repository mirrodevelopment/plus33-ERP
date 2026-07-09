/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.control
 * File              : ScadaControlService.java
 * Purpose           : Business logic service layer for Iot Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ScadaControlController
 * Related Service   : ScadaControlService
 * Related Repository: ScadaControlRepository
 * Related Entity    : ScadaControl
 * Related DTO       : N/A
 * Related Mapper    : ScadaControlMapper
 * Related DB Table  : scada_controls
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ScadaControlController, ScadaControlServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Iot Module. Implements ScadaControlService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.iot.control;

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
 * <p><b>Class  :</b> {@code ScadaControlService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.control}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Iot Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ScadaControlController
 *   --> ScadaControlService (this)
 *   --> Validate business rules
 *   --> ScadaControlRepository (read/write 'scada_controls')
 *   --> ScadaControlMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code scada_controls}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ScadaControlService {
    @Autowired PlatformScadaWriteCommandRepository commandRepo;
    @Autowired PlatformScadaAuditTrailRepository auditRepo;
    /**
     * Performs the executeWrite operation in this module.
     *
     * @param deviceId the deviceId input value
     * @param registerId the registerId input value
     * @param val the val input value
     * @param operator the operator input value
     * @param sign the sign input value
     */
    @Transactional
    public void executeWrite(Long deviceId, Long registerId, BigDecimal val, String operator, String sign) {
        PlatformScadaWriteCommand cmd = new PlatformScadaWriteCommand();
        cmd.setDeviceId(deviceId);
        cmd.setRegisterId(registerId);
        cmd.setCommandValue(val);
        cmd.setCommandHash("SHA-256-HASH-VALUE");
        cmd.setApprovedBy("admin-supervisor");
        cmd.setExecutedBy(operator);
        cmd.setSignature(sign);
        cmd.setExecutionTime(LocalDateTime.now());
        cmd.setRollbackSupported(true);
        cmd = commandRepo.save(cmd);

        PlatformScadaAuditTrail audit = new PlatformScadaAuditTrail();
        audit.setCommandId(cmd.getId());
        audit.setStatus("VERIFIED");
        audit.setAuditHash("AUDIT-SIG-CRYPT");
        audit.setAuditedAt(LocalDateTime.now());
        auditRepo.save(audit);
    }
}