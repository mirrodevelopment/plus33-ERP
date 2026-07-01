package com.plus33.erp.twin.anomaly;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AnomalyDetector {
    @Autowired PlatformTwinAnomalyThresholdRepository thresholdRepo;
    @Autowired PlatformTwinAlertRepository alertRepo;

    @Transactional
    public void checkAnomaly(Long instanceId, String metric, BigDecimal val) {
        thresholdRepo.findAll().stream()
                .filter(t -> t.getInstanceId().equals(instanceId) && t.getMetricName().equals(metric))
                .findFirst()
                .ifPresent(t -> {
                    if (val.compareTo(t.getMinValue()) < 0 || val.compareTo(t.getMaxValue()) > 0) {
                        PlatformTwinAlert alert = new PlatformTwinAlert();
                        alert.setInstanceId(instanceId);
                        alert.setAlertType("OUT_OF_BOUNDS");
                        alert.setSeverity("CRITICAL");
                        alert.setMessage("Metric " + metric + " exceeded threshold limit: " + val);
                        alert.setTriggeredAt(LocalDateTime.now());
                        alertRepo.save(alert);
                    }
                });
    }
}