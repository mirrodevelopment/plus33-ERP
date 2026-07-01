package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaseAssembler {
    @Autowired PlatformProcessCaseRepository caseRepo;

    @Transactional
    public PlatformProcessCase assembleCase(String token, String processName) {
        return caseRepo.findAll().stream()
                .filter(c -> c.getCaseToken().equals(token))
                .findFirst()
                .orElseGet(() -> {
                    PlatformProcessCase c = new PlatformProcessCase();
                    c.setCaseToken(token);
                    c.setProcessName(processName);
                    c.setActive(true);
                    return caseRepo.save(c);
                });
    }
}