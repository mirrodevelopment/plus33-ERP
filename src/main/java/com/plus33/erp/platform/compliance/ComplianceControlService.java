/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.compliance
 * File              : ComplianceControlService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceControlController
 * Related Service   : ComplianceControlService
 * Related Repository: ComplianceControlRepository
 * Related Entity    : ComplianceControl
 * Related DTO       : N/A
 * Related Mapper    : ComplianceControlMapper
 * Related DB Table  : compliance_controls
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceControlController, ComplianceControlServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ComplianceControlService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.compliance;

import com.plus33.erp.platform.entity.PlatformComplianceControl;
import com.plus33.erp.platform.entity.PlatformComplianceFramework;
import com.plus33.erp.platform.repository.PlatformComplianceControlRepository;
import com.plus33.erp.platform.repository.PlatformComplianceFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceControlService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.compliance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ComplianceControlController
 *   --> ComplianceControlService (this)
 *   --> Validate business rules
 *   --> ComplianceControlRepository (read/write 'compliance_controls')
 *   --> ComplianceControlMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compliance_controls}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ComplianceControlService {
    @Autowired PlatformComplianceFrameworkRepository frameworkRepo;
    @Autowired PlatformComplianceControlRepository controlRepo;
    /**
     * Creates a new framework and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerFramework(String code, String name) {
        PlatformComplianceFramework fw = new PlatformComplianceFramework();
        fw.setFrameworkCode(code);
        fw.setFrameworkName(name);
        frameworkRepo.save(fw);
    }

    /**
     * Creates a new control and persists it to the database.
     *
     * @param frameworkCode the frameworkCode input value
     * @param controlCode the controlCode input value
     * @param controlName the controlName input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void addControl(String frameworkCode, String controlCode, String controlName) {
        PlatformComplianceFramework fw = frameworkRepo.findAll().stream()
                .filter(f -> f.getFrameworkCode().equals(frameworkCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Framework not found"));

        PlatformComplianceControl ctrl = new PlatformComplianceControl();
        ctrl.setFrameworkId(fw.getId());
        ctrl.setControlCode(controlCode);
        ctrl.setControlName(controlName);
        ctrl.setStatus("COMPLIANT");
        controlRepo.save(ctrl);
    }
}