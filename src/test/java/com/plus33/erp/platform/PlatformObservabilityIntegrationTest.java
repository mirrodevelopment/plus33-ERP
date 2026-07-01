package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.platform.observability.DistributedTracerService;
import com.plus33.erp.platform.logging.PlatformLogService;
import com.plus33.erp.platform.slo.SloMeasurementService;
import com.plus33.erp.platform.notification.AlertEscalationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PlatformObservabilityIntegrationTest {

    @Autowired DistributedTracerService tracerService;
    @Autowired PlatformLogService logService;
    @Autowired SloMeasurementService sloService;
    @Autowired AlertEscalationService alertService;

    @Autowired PlatformTraceSpanRepository spanRepo;
    @Autowired PlatformLogEntryRepository logRepo;
    @Autowired PlatformSloMeasurementRepository measurementRepo;
    @Autowired PlatformAnomalyAlertRepository alertRepo;

    @Test
    void testObservabilityScenarios() {
        // Record 40 spans
        for (int i = 1; i <= 40; i++) {
            tracerService.startSpan("t-" + i, "s-" + i, null, "GET /api/data", 10 + i);
        }
        List<PlatformTraceSpan> spans = spanRepo.findAll();
        assertTrue(spans.size() >= 40);

        // Record 40 log entries
        for (int i = 1; i <= 40; i++) {
            logService.recordLog("t-" + i, "s-" + i, "auth-service", "INFO", "com.plus33.Auth", "Log message " + i);
        }
        List<PlatformLogEntry> logs = logRepo.findAll();
        assertTrue(logs.size() >= 40);

        // Record 40 SLO measurements
        sloService.createSlo("API_AVAILABILITY", 99.9, "api-gateway");
        for (int i = 1; i <= 40; i++) {
            sloService.recordMeasurement("API_AVAILABILITY", 99.8, 0.1);
        }
        List<PlatformSloMeasurement> measurements = measurementRepo.findAll();
        assertTrue(measurements.size() >= 40);

        // Record 40 anomaly alerts escalation cycles
        for (int i = 1; i <= 40; i++) {
            alertService.triggerAlert("CPU_SPIKE_" + i, "CRITICAL", "Host CPU exceeded 95%");
        }
        List<PlatformAnomalyAlert> alerts = alertRepo.findAll();
        assertTrue(alerts.size() >= 40);
    }
}
