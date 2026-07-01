package com.plus33.erp.platform.scaling;

import com.plus33.erp.platform.entity.PlatformScalingActivity;
import com.plus33.erp.platform.entity.PlatformScalingDecision;
import com.plus33.erp.platform.entity.PlatformScalingPolicy;
import com.plus33.erp.platform.repository.PlatformScalingActivityRepository;
import com.plus33.erp.platform.repository.PlatformScalingDecisionRepository;
import com.plus33.erp.platform.repository.PlatformScalingPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ScaleOrchestrator {
    @Autowired PlatformScalingPolicyRepository policyRepo;
    @Autowired PlatformScalingActivityRepository activityRepo;
    @Autowired PlatformScalingDecisionRepository decisionRepo;

    @Transactional
    public void evaluate(String metricName, double currentValue, int currentReplicas) {
        PlatformScalingPolicy policy = policyRepo.findAll().stream()
                .filter(p -> p.getMetricName().equals(metricName))
                .findFirst().orElse(null);

        if (policy == null) return;

        if (BigDecimal.valueOf(currentValue).compareTo(policy.getThresholdValue()) > 0) {
            int desired = Math.min(currentReplicas + 2, policy.getMaxReplicas());
            if (desired > currentReplicas) {
                PlatformScalingDecision dec = new PlatformScalingDecision();
                dec.setMetricName(metricName);
                dec.setCurrentValue(BigDecimal.valueOf(currentValue));
                dec.setThresholdValue(policy.getThresholdValue());
                dec.setCurrentReplicas(currentReplicas);
                dec.setDesiredReplicas(desired);
                dec.setReason("Metric " + metricName + " exceeded threshold of " + policy.getThresholdValue());
                dec.setTimestamp(LocalDateTime.now());
                decisionRepo.save(dec);

                PlatformScalingActivity act = new PlatformScalingActivity();
                act.setActivityType("SCALE_UP");
                act.setCurrentReplicas(currentReplicas);
                act.setDesiredReplicas(desired);
                act.setStatus("COMPLETED");
                act.setStartedAt(LocalDateTime.now());
                act.setCompletedAt(LocalDateTime.now());
                activityRepo.save(act);
            }
        }
    }
}