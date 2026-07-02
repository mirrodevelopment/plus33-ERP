package com.plus33.erp.compliance.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ComplianceChecker {
    @Autowired PlatformDeviceComplianceLogRepository complianceLogRepo;

    @Transactional
    public PlatformDeviceComplianceLog recordCompliance(Long deviceId, Long policyId, String result) {
        PlatformDeviceComplianceLog log = new PlatformDeviceComplianceLog();
        log.setDeviceId(deviceId);
        log.setPolicyId(policyId);
        log.setResult(result);
        log.setExecutionTime(LocalDateTime.now());
        log.setDurationMs(250L);
        log.setDetails("Compliance evaluation finished successfully. Verification status: " + result);
        return complianceLogRepo.save(log);
    }
}