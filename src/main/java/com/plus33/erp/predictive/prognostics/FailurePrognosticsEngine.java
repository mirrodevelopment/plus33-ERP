package com.plus33.erp.predictive.prognostics;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FailurePrognosticsEngine {
    @Autowired PlatformFailurePrognosticsLogRepository prognosticsRepo;
    @Autowired PlatformMaintenanceTriggerLogRepository triggerRepo;

    @Transactional
    public PlatformFailurePrognosticsLog predictFailure(Long assetId, BigDecimal RUL, BigDecimal prob, BigDecimal conf) {
        PlatformFailurePrognosticsLog log = new PlatformFailurePrognosticsLog();
        log.setAssetId(assetId);
        log.setPredictionTime(LocalDateTime.now());
        log.setPredictedFailureTime(LocalDateTime.now().plusHours(RUL.longValue()));
        log.setRemainingUsefulLifeHours(RUL);
        log.setFailureProbability(prob);
        log.setConfidenceScore(conf);
        log.setPredictionModelVersion("v2.1.0-RF");
        log.setTriggerReason("Bearing temperature spike anomaly detected.");
        log.setRecommendedAction("Schedule grease lubrication and bearing inspection window");
        log = prognosticsRepo.save(log);

        PlatformMaintenanceTriggerLog trig = new PlatformMaintenanceTriggerLog();
        trig.setTriggerSource("PREDICTIVE_ENGINE");
        trig.setPredictedFailureId(log.getId());
        trig.setWorkOrderReference("WO-REPAIR-" + log.getId());
        trig.setMaintenanceStatus("SCHEDULED");
        trig.setScheduledTime(LocalDateTime.now().plusHours(12));
        trig.setAutomaticExecution(true);
        triggerRepo.save(trig);

        return log;
    }
}