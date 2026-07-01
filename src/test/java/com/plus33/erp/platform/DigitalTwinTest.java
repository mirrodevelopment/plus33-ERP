package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.telemetry.TelemetryIngestionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DigitalTwinTest {

    @Autowired TelemetryIngestionService ingestionService;

    @Autowired PlatformTwinDefinitionRepository definitionRepo;
    @Autowired PlatformTwinInstanceRepository instanceRepo;
    @Autowired PlatformTwinStateRepository stateRepo;
    @Autowired PlatformTwinTelemetryRepository telemetryRepo;
    @Autowired PlatformTwinAnomalyThresholdRepository thresholdRepo;
    @Autowired PlatformTwinAlertRepository alertRepo;
    @Autowired PlatformTelemetryRetentionPolicyRepository retentionRepo;
    @Autowired PlatformTelemetryArchiveLogRepository archiveLogRepo;

    @Test
    void testDigitalTwinScenarios() {
        // Register definitions & instances
        PlatformTwinDefinition def = new PlatformTwinDefinition();
        def.setDefinitionCode("PUMP_TYPE");
        def.setDefinitionName("Hydraulic Pump");
        def = definitionRepo.save(def);

        PlatformTwinInstance inst = new PlatformTwinInstance();
        inst.setDefinitionId(def.getId());
        inst.setInstanceCode("PUMP_001");
        inst.setStatus("ACTIVE");
        inst = instanceRepo.save(inst);

        // Save anomaly thresholds
        PlatformTwinAnomalyThreshold threshold = new PlatformTwinAnomalyThreshold();
        threshold.setInstanceId(inst.getId());
        threshold.setMetricName("temperature");
        threshold.setMinValue(BigDecimal.valueOf(10.00));
        threshold.setMaxValue(BigDecimal.valueOf(90.00));
        thresholdRepo.save(threshold);

        // Run 40 telemetry updates (ingest inside range)
        for (int i = 1; i <= 40; i++) {
            ingestionService.ingest(inst.getId(), "temperature", BigDecimal.valueOf(50.00 + i/2.0));
        }

        List<PlatformTwinTelemetry> telemetries = telemetryRepo.findAll();
        assertTrue(telemetries.size() >= 40);

        // Trigger anomaly alerts with value out of bounds
        ingestionService.ingest(inst.getId(), "temperature", BigDecimal.valueOf(95.50));
        List<PlatformTwinAlert> alerts = alertRepo.findAll();
        assertEquals(1, alerts.size());
        assertEquals("CRITICAL", alerts.get(0).getSeverity());

        // Register 40 telemetry retention policies and archive logs
        for (int i = 1; i <= 40; i++) {
            PlatformTelemetryArchiveLog log = new PlatformTelemetryArchiveLog();
            log.setInstanceId(inst.getId());
            log.setRecordsArchived(100 + i);
            log.setArchiveKey("s3-archived-key-" + i);
            log.setArchivedAt(LocalDateTime.now());
            archiveLogRepo.save(log);
        }
        List<PlatformTelemetryArchiveLog> archiveLogs = archiveLogRepo.findAll();
        assertTrue(archiveLogs.size() >= 40);
    }
}
