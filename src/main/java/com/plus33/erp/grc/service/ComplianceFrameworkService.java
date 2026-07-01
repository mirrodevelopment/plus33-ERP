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

    public ComplianceFramework createFramework(Long companyId, String code, String name, String description) {
        ComplianceFramework fw = new ComplianceFramework();
        fw.setCompanyId(companyId);
        fw.setCode(code);
        fw.setName(name);
        fw.setDescription(description);
        return frameworkRepo.save(fw);
    }

    public ControlLibrary createControl(Long companyId, String controlCode, String name, String description) {
        ControlLibrary ctrl = new ControlLibrary();
        ctrl.setCompanyId(companyId);
        ctrl.setControlCode(controlCode);
        ctrl.setName(name);
        ctrl.setDescription(description);
        ctrl.setStatus("DRAFT");
        return controlLibraryRepo.save(ctrl);
    }

    public void activateControl(Long controlId) {
        ControlLibrary ctrl = controlLibraryRepo.findById(controlId).orElseThrow();
        ctrl.setStatus("ACTIVE");
        controlLibraryRepo.save(ctrl);
    }

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

    public List<ControlMapping> getFrameworkMappings(Long frameworkId) {
        return controlMappingRepo.findByFrameworkId(frameworkId);
    }
}
