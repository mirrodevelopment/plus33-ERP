package com.plus33.erp.predictive.reliability;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReliabilityEngineeringService {
    @Autowired PlatformReliabilityFailureLogRepository reliabilityRepo;
    @Autowired PlatformPredictiveAuditLogRepository auditRepo;

    @Transactional
    public PlatformReliabilityFailureLog recordReliabilityLogs(Long assetId, BigDecimal MTBF, BigDecimal MTTR, BigDecimal availability) {
        PlatformReliabilityFailureLog log = new PlatformReliabilityFailureLog();
        log.setAssetId(assetId);
        log.setMtbfHours(MTBF);
        log.setMttrHours(MTTR);
        log.setAvailabilityRate(availability);
        log.setFailureRate(BigDecimal.valueOf(0.00015));
        log.setReliabilityScore(BigDecimal.valueOf(98.50));
        log.setRepairDurationMinutes(120);
        log.setDowntimeDurationMinutes(240);
        log.setRootCauseCategory("ELECTRICAL_OVERHEATING");
        log.setFailureMode("COIL_BURNOUT");
        log.setCorrectiveAction("Coil replacement and cooling shroud installation");
        log.setReportedAt(LocalDateTime.now());
        log = reliabilityRepo.save(log);

        PlatformPredictiveAuditLog audit = new PlatformPredictiveAuditLog();
        audit.setPredictionId(log.getId());
        audit.setOperator("reliability-lead");
        audit.setActionType("STRATEGY_CHANGE");
        audit.setNewThresholdConfig("{ \"strategy\": \"Predictive\" }");
        audit.setTraceId("TRACE-ID-RELIABILITY-INIT");
        auditRepo.save(audit);

        return log;
    }
}