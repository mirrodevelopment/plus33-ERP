package com.plus33.erp.intelligence.query;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DecisionLineageTracker {
    @Autowired PlatformXaiLineageRepository lineageRepo;

    @Transactional
    public PlatformXaiLineage recordLineage(String key, String factors, String ver) {
        PlatformXaiLineage lin = new PlatformXaiLineage();
        lin.setDecisionKey(key);
        lin.setContributingFactors(factors);
        lin.setModelVersion(ver);
        lin.setCreatedAt(LocalDateTime.now());
        return lineageRepo.save(lin);
    }
}