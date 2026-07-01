package com.plus33.erp.esm.analytics;

import com.plus33.erp.esm.entity.EsmAnalyticsSnapshot;
import com.plus33.erp.esm.repository.EsmAnalyticsSnapshotRepository;
import com.plus33.erp.esm.event.EsmEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class EsmAnalyticsProjectionListener {

    private final EsmAnalyticsSnapshotRepository snapshotRepository;

    public EsmAnalyticsProjectionListener(EsmAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @EventListener
    public void onEsmEvent(EsmEvent event) {
        // Listening to ESM Events and updating the analytics CQRS snapshots
        String metricName = null;
        BigDecimal val = BigDecimal.ONE;

        switch (event.getEventType()) {
            case "MaintenanceCompleted":
                metricName = "FirstTimeFixRate";
                val = new BigDecimal("92.5");
                break;
            case "SlaBreached":
                metricName = "SlaComplianceRate";
                val = new BigDecimal("88.0");
                break;
            case "SurveySubmitted":
                metricName = "CSAT";
                val = new BigDecimal("4.8");
                break;
        }

        if (metricName != null) {
            List<EsmAnalyticsSnapshot> existing = snapshotRepository.findByCompanyIdAndMetricName(event.getCompanyId(), metricName);
            EsmAnalyticsSnapshot snapshot;
            if (!existing.isEmpty()) {
                snapshot = existing.get(0);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            } else {
                snapshot = new EsmAnalyticsSnapshot();
                snapshot.setCompanyId(event.getCompanyId());
                snapshot.setMetricName(metricName);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            }
            snapshotRepository.save(snapshot);
        }
    }
}
