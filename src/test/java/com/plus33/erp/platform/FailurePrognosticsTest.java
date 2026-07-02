package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.predictive.prognostics.FailurePrognosticsEngine;

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
public class FailurePrognosticsTest {

    @Autowired FailurePrognosticsEngine prognosticsEngine;

    @Autowired PlatformFailurePrognosticsLogRepository prognosticsRepo;
    @Autowired PlatformMaintenanceTriggerLogRepository triggerRepo;

    @Test
    void testFailurePrognosticsScenarios() {
        // RUL failure prognostics checks over 40 iterations
        for (int i = 1; i <= 40; i++) {
            prognosticsEngine.predictFailure(1L, BigDecimal.valueOf(168.00), BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.92));
        }

        List<PlatformFailurePrognosticsLog> prognostics = prognosticsRepo.findAll();
        assertTrue(prognostics.size() >= 40);
        assertEquals("v2.1.0-RF", prognostics.get(0).getPredictionModelVersion());
        assertEquals("Bearing temperature spike anomaly detected.", prognostics.get(0).getTriggerReason());

        List<PlatformMaintenanceTriggerLog> triggers = triggerRepo.findAll();
        assertTrue(triggers.size() >= 40);
        assertEquals("PREDICTIVE_ENGINE", triggers.get(0).getTriggerSource());
        assertEquals("SCHEDULED", triggers.get(0).getMaintenanceStatus());
    }
}
