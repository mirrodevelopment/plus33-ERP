package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.mining.*;
import com.plus33.erp.twin.conformance.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProcessMiningTest {

    @Autowired CaseAssembler caseAssembler;
    @Autowired VariantDiscoveryService variantDiscovery;
    @Autowired PerformanceAnalyzer performanceAnalyzer;
    @Autowired ConformanceEngine conformanceEngine;

    @Autowired PlatformProcessCaseRepository caseRepo;
    @Autowired PlatformProcessEventLogRepository eventLogRepo;
    @Autowired PlatformConformanceRuleRepository ruleRepo;
    @Autowired PlatformConformanceDeviationRepository deviationRepo;

    @Test
    void testProcessMiningScenarios() {
        // Register 40 cases
        for (int i = 1; i <= 40; i++) {
            caseAssembler.assembleCase("case-token-" + i, "ProcurementProcess");
        }
        List<PlatformProcessCase> cases = caseRepo.findAll();
        assertTrue(cases.size() >= 40);

        // Populate logs for variant discovery & performance
        PlatformProcessCase testCase = cases.get(0);
        for (int i = 1; i <= 40; i++) {
            PlatformProcessEventLog log = new PlatformProcessEventLog();
            log.setCaseId(testCase.getId());
            log.setActivityName("ACTIVITY_" + i);
            log.setTransitionState("COMPLETED");
            log.setDurationMs(100L * i);
            log.setRecordedAt(LocalDateTime.now());
            eventLogRepo.save(log);
        }

        List<PlatformProcessEventLog> logs = variantDiscovery.discoverVariant(testCase.getId());
        assertEquals(40, logs.size());

        long avgDuration = performanceAnalyzer.analyzeAverageDuration("ACTIVITY_1");
        assertEquals(100L, avgDuration);

        // Setup 40 conformance rules & deviations checking
        for (int i = 1; i <= 40; i++) {
            PlatformConformanceRule rule = new PlatformConformanceRule();
            rule.setProcessName("ProcurementProcess");
            rule.setExpectedActivity("ACTIVITY_EXPECTED_" + i);
            rule.setSequenceOrder(i);
            ruleRepo.save(rule);
        }

        List<String> actualActivities = List.of("ACTIVITY_DELAY_1", "ACTIVITY_REJECT_2");
        conformanceEngine.checkConformance(testCase.getId(), "ProcurementProcess", actualActivities);

        List<PlatformConformanceDeviation> deviations = deviationRepo.findAll();
        assertTrue(deviations.size() > 0);
        assertTrue(deviations.get(0).getSlaBreachRisk());
    }
}
