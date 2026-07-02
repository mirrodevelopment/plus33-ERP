package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.ev.diagnostic.BatteryHealthDiagnosticService;

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
public class BatteryHealthTest {

    @Autowired BatteryHealthDiagnosticService diagnosticService;

    @Autowired PlatformEvBatteryHealthLogRepository healthRepo;

    @Test
    void testBatteryHealthScenarios() {
        // Battery health diagnostics over 40 iterations
        for (int i = 1; i <= 40; i++) {
            diagnosticService.diagnoseBattery((long) i);
        }

        List<PlatformEvBatteryHealthLog> logs = healthRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("v59.0-BMS-DIAG", logs.get(0).getDiagnosticVersion());
    }
}
