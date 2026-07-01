package com.plus33.erp.twin.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.device.TwinStateProjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TelemetryIngestionService {
    @Autowired TelemetryValidator validator;
    @Autowired TelemetryNormalizer normalizer;
    @Autowired TwinStateProjector projector;
    @Autowired PlatformTwinTelemetryRepository telemetryRepo;

    @Transactional
    public void ingest(Long instanceId, String name, BigDecimal val) {
        if (!validator.isValid(name, val)) {
            throw new IllegalArgumentException("Invalid telemetry data");
        }
        BigDecimal normalizedVal = normalizer.normalize(name, val);

        PlatformTwinTelemetry telemetry = new PlatformTwinTelemetry();
        telemetry.setInstanceId(instanceId);
        telemetry.setMetricName(name);
        telemetry.setMetricValue(normalizedVal);
        telemetry.setRecordedAt(LocalDateTime.now());
        telemetryRepo.save(telemetry);

        projector.project(instanceId, name, normalizedVal);
    }
}