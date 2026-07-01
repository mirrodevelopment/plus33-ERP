package com.plus33.erp.twin.conformance;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConformanceEngine {
    @Autowired PlatformConformanceRuleRepository ruleRepo;
    @Autowired PlatformConformanceDeviationRepository deviationRepo;
    @Autowired SlaPredictionService slaPredictor;

    @Transactional
    public void checkConformance(Long caseId, String processName, List<String> transitionActivities) {
        List<PlatformConformanceRule> rules = ruleRepo.findAll().stream()
                .filter(r -> r.getProcessName().equals(processName))
                .toList();

        for (int i = 0; i < Math.min(rules.size(), transitionActivities.size()); i++) {
            PlatformConformanceRule rule = rules.get(i);
            String activity = transitionActivities.get(i);

            if (!rule.getExpectedActivity().equals(activity)) {
                PlatformConformanceDeviation deviation = new PlatformConformanceDeviation();
                deviation.setCaseId(caseId);
                deviation.setRuleId(rule.getId());
                deviation.setDeviationDetails("Expected activity " + rule.getExpectedActivity() + " but got " + activity);
                deviation.setSlaBreachRisk(slaPredictor.predictSlaBreach(processName, activity));
                deviation.setDetectedAt(LocalDateTime.now());
                deviationRepo.save(deviation);
            }
        }
    }
}