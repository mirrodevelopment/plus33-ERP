package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.predictive.reliability.ReliabilityEngineeringService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReliabilityEngineeringTest {

    @Autowired ReliabilityEngineeringService reliabilityService;

    @Autowired PlatformReliabilityFailureLogRepository reliabilityRepo;
    @Autowired PlatformPredictiveAuditLogRepository auditRepo;

    @Test
    void testReliabilityEngineeringScenarios() {
        // MTBF and MTTR reliability statistics records over 40 iterations
        for (int i = 1; i <= 40; i++) {
            reliabilityService.recordReliabilityLogs(1L, BigDecimal.valueOf(720.00), BigDecimal.valueOf(4.00), BigDecimal.valueOf(99.44));
        }

        List<PlatformReliabilityFailureLog> logs = reliabilityRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("ELECTRICAL_OVERHEATING", logs.get(0).getRootCauseCategory());
        assertEquals("COIL_BURNOUT", logs.get(0).getFailureMode());

        List<PlatformPredictiveAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("reliability-lead", audits.get(0).getOperator());
        assertEquals("STRATEGY_CHANGE", audits.get(0).getActionType());
    }
}
