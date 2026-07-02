package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.fuel.diagnostic.EcoDrivingDiagnosticService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EcoDrivingTest {

    @Autowired EcoDrivingDiagnosticService diagnosticService;

    @Autowired PlatformEcoDrivingLogRepository diagnosticRepo;

    @Test
    void testEcoDrivingScenarios() {
        // Eco-driving logs diagnostics over 40 iterations
        for (int i = 1; i <= 40; i++) {
            diagnosticService.logEcoDrivingMetrics(1L, (long) i);
        }

        List<PlatformEcoDrivingLog> logs = diagnosticRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals(2, logs.get(0).getHarshAccelerationCount());
        assertEquals(1, logs.get(0).getHarshBrakingCount());
        assertEquals("OK", logs.get(0).getCoachingStatus());
    }
}
