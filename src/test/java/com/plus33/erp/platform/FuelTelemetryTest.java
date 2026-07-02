package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.fuel.telemetry.FuelTelemetryService;

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
public class FuelTelemetryTest {

    @Autowired FuelTelemetryService telemetryService;

    @Autowired PlatformFuelTelemetryLogRepository telemetryRepo;

    @Test
    void testFuelTelemetryScenarios() {
        // Fuel telemetry logs ingestion over 40 iterations
        for (int i = 1; i <= 40; i++) {
            telemetryService.recordTelemetry((long) i, 1L);
        }

        List<PlatformFuelTelemetryLog> logs = telemetryRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals(180, logs.get(0).getIdleTimeSeconds());
    }
}
