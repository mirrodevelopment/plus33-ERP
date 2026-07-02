package com.plus33.erp.routing.esg.compliance;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SustainabilityComplianceService {
    @Autowired PlatformEsgCarbonOffsetRepository offsetRepo;
    @Autowired PlatformEsgComplianceLogRepository complianceRepo;

    @Transactional
    public PlatformEsgCarbonOffset registerOffset(String certificate) {
        PlatformEsgCarbonOffset offset = new PlatformEsgCarbonOffset();
        offset.setOffsetProvider("EcoRegistryGlobal");
        offset.setCertificateNumber(certificate);
        offset.setVerificationStandard("GoldStandard");
        offset.setCreditAmountT(BigDecimal.valueOf(150.00));
        offset.setRetirementDate(LocalDateTime.now().plusMonths(6));
        offset.setProjectCountry("Costa Rica");
        offset.setProjectType("Reforestation");
        offset.setRegisteredAt(LocalDateTime.now());
        return offsetRepo.save(offset);
    }

    @Transactional
    public PlatformEsgComplianceLog verifyCompliance(String framework) {
        PlatformEsgComplianceLog compliance = new PlatformEsgComplianceLog();
        compliance.setFramework(framework);
        compliance.setComplianceScore(BigDecimal.valueOf(98.50));
        compliance.setStatus("COMPLIANT");
        compliance.setFindingSummary("Scope 1 and Scope 2 emission factor records are correct.");
        compliance.setCorrectiveAction("None required. Monitor grid-factor variations quarterly.");
        compliance.setOwnerName("sustainability-officer");
        compliance.setNextReviewDate(LocalDateTime.now().plusMonths(3));
        compliance.setAuditedAt(LocalDateTime.now());
        return complianceRepo.save(compliance);
    }
}