package com.plus33.erp.project.analytics;

import com.plus33.erp.project.entity.ProjectAnalyticsSnapshot;
import com.plus33.erp.project.repository.ProjectAnalyticsSnapshotRepository;
import com.plus33.erp.project.event.ProjectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ProjectAnalyticsProjectionListener {

    private final ProjectAnalyticsSnapshotRepository snapshotRepository;

    public ProjectAnalyticsProjectionListener(ProjectAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @EventListener
    public void onProjectEvent(ProjectEvent event) {
        String metricName = null;
        BigDecimal val = BigDecimal.ONE;

        switch (event.getEventType()) {
            case "ProjectCostPosted":
                metricName = "ActualCost";
                val = new BigDecimal("45000.00");
                break;
            case "RevenueRecognized":
                metricName = "RevenueForecast";
                val = new BigDecimal("120000.00");
                break;
        }

        if (metricName != null) {
            List<ProjectAnalyticsSnapshot> existing = snapshotRepository.findByCompanyIdAndMetricName(event.getCompanyId(), metricName);
            ProjectAnalyticsSnapshot snapshot;
            if (!existing.isEmpty()) {
                snapshot = existing.get(0);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            } else {
                snapshot = new ProjectAnalyticsSnapshot();
                snapshot.setCompanyId(event.getCompanyId());
                snapshot.setMetricName(metricName);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            }
            snapshotRepository.save(snapshot);
        }
    }
}
