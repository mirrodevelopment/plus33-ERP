package com.plus33.erp.twin.device;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.anomaly.AnomalyDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TwinStateProjector {
    @Autowired PlatformTwinStateRepository stateRepo;
    @Autowired AnomalyDetector anomalyDetector;

    @Transactional
    public void project(Long instanceId, String metric, BigDecimal val) {
        PlatformTwinState state = stateRepo.findAll().stream()
                .filter(s -> s.getInstanceId().equals(instanceId))
                .findFirst()
                .orElseGet(() -> {
                    PlatformTwinState s = new PlatformTwinState();
                    s.setInstanceId(instanceId);
                    return s;
                });

        state.setCurrentStateJson("{\"" + metric + "\":" + val + "}");
        state.setUpdatedAt(LocalDateTime.now());
        stateRepo.save(state);

        anomalyDetector.checkAnomaly(instanceId, metric, val);
    }
}