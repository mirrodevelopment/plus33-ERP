/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : ComplianceFrameworkService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceFrameworkController
 * Related Service   : ComplianceFrameworkService
 * Related Repository: ComplianceFrameworkRepository, ControlLibraryRepository, ControlMappingRepository, ControlTestResultRepository
 * Related Entity    : ComplianceFramework
 * Related DTO       : N/A
 * Related Mapper    : ComplianceFrameworkMapper
 * Related DB Table  : compliance_frameworks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceFrameworkController, ComplianceFrameworkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements ComplianceFrameworkService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceFrameworkService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ComplianceFrameworkController
 *   --> ComplianceFrameworkService (this)
 *   --> Validate business rules
 *   --> ComplianceFrameworkRepository (read/write 'compliance_frameworks')
 *   --> ComplianceFrameworkMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compliance_frameworks}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class ComplianceFrameworkService {

    private final ComplianceFrameworkRepository frameworkRepo;
    private final ControlLibraryRepository controlLibraryRepo;
    private final ControlMappingRepository controlMappingRepo;
    private final ControlTestResultRepository testResultRepo;
    private final GrcEventBus eventBus;

    public ComplianceFrameworkService(ComplianceFrameworkRepository frameworkRepo,
                                      ControlLibraryRepository controlLibraryRepo,
                                      ControlMappingRepository controlMappingRepo,
                                      ControlTestResultRepository testResultRepo,
                                      GrcEventBus eventBus) {
        this.frameworkRepo = frameworkRepo;
        this.controlLibraryRepo = controlLibraryRepo;
        this.controlMappingRepo = controlMappingRepo;
        this.testResultRepo = testResultRepo;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new framework and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param code the code input value
     * @param name the name input value
     * @param description the description input value
     * @return the ComplianceFramework result
     * @throws BusinessException if a business rule is violated
     */
    public ComplianceFramework createFramework(Long companyId, String code, String name, String description) {
        ComplianceFramework fw = new ComplianceFramework();
        fw.setCompanyId(companyId);
        fw.setCode(code);
        fw.setName(name);
        fw.setDescription(description);
        return frameworkRepo.save(fw);
    }

    /**
     * Creates a new control and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param controlCode the controlCode input value
     * @param name the name input value
     * @param description the description input value
     * @return the ControlLibrary result
     * @throws BusinessException if a business rule is violated
     */
    public ControlLibrary createControl(Long companyId, String controlCode, String name, String description) {
        ControlLibrary ctrl = new ControlLibrary();
        ctrl.setCompanyId(companyId);
        ctrl.setControlCode(controlCode);
        ctrl.setName(name);
        ctrl.setDescription(description);
        ctrl.setStatus("DRAFT");
        return controlLibraryRepo.save(ctrl);
    }

    /**
     * Performs the activateControl operation in this module.
     *
     * @param controlId the controlId input value
     */
    public void activateControl(Long controlId) {
        ControlLibrary ctrl = controlLibraryRepo.findById(controlId).orElseThrow();
        ctrl.setStatus("ACTIVE");
        controlLibraryRepo.save(ctrl);
    }

    /**
     * Converts between Entity and DTO representations (MapStruct).
     *
     * @param controlLibraryId the controlLibraryId input value
     * @param frameworkId the frameworkId input value
     * @param controlRef the controlRef input value
     * @return the ControlMapping result
     */
    public ControlMapping mapControlToFramework(Long controlLibraryId, Long frameworkId, String controlRef) {
        if (controlMappingRepo.existsByControlLibraryIdAndFrameworkId(controlLibraryId, frameworkId)) {
            throw new IllegalStateException("Control already mapped to this framework");
        }
        ControlMapping mapping = new ControlMapping();
        mapping.setControlLibraryId(controlLibraryId);
        mapping.setFrameworkId(frameworkId);
        mapping.setControlRef(controlRef);
        return controlMappingRepo.save(mapping);
    }

    /**
     * Performs the recordTestResult operation in this module.
     *
     * @param testPlanId the testPlanId input value
     * @param result the result input value
     * @param testedById the testedById input value
     * @param notes the notes input value
     * @return the ControlTestResult result
     */
    public ControlTestResult recordTestResult(Long testPlanId, String result, Long testedById, String notes) {
        ControlTestResult r = new ControlTestResult();
        r.setTestPlanId(testPlanId);
        r.setResult(result);
        r.setTestedById(testedById);
        r.setTestedAt(LocalDateTime.now());
        r.setNotes(notes);
        testResultRepo.save(r);
        String eventType = "PASS".equalsIgnoreCase(result) ? "ControlPassed" : "ControlFailed";
        eventBus.publish(null, eventType, Map.of("testPlanId", testPlanId, "result", result));
        return r;
    }

    /**
     * Retrieves framework mappings data from the database.
     *
     * @param frameworkId the frameworkId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<ControlMapping> getFrameworkMappings(Long frameworkId) {
        return controlMappingRepo.findByFrameworkId(frameworkId);
    }
}