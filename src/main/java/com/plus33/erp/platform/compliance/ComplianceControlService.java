package com.plus33.erp.platform.compliance;

import com.plus33.erp.platform.entity.PlatformComplianceControl;
import com.plus33.erp.platform.entity.PlatformComplianceFramework;
import com.plus33.erp.platform.repository.PlatformComplianceControlRepository;
import com.plus33.erp.platform.repository.PlatformComplianceFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplianceControlService {
    @Autowired PlatformComplianceFrameworkRepository frameworkRepo;
    @Autowired PlatformComplianceControlRepository controlRepo;

    @Transactional
    public void registerFramework(String code, String name) {
        PlatformComplianceFramework fw = new PlatformComplianceFramework();
        fw.setFrameworkCode(code);
        fw.setFrameworkName(name);
        frameworkRepo.save(fw);
    }

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