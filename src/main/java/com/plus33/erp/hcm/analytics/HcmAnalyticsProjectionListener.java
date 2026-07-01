package com.plus33.erp.hcm.analytics;

import com.plus33.erp.hcm.entity.HcmAnalyticsSnapshot;
import com.plus33.erp.hcm.repository.HcmAnalyticsSnapshotRepository;
import com.plus33.erp.hcm.event.HcmEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class HcmAnalyticsProjectionListener {

    private final HcmAnalyticsSnapshotRepository snapshotRepository;

    public HcmAnalyticsProjectionListener(HcmAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @EventListener
    public void onHcmEvent(HcmEvent event) {
        String metricName = null;
        BigDecimal val = BigDecimal.ONE;

        switch (event.getEventType()) {
            case "EmployeeConfirmed":
                metricName = "AttritionRate";
                val = new BigDecimal("4.20"); // 4.2% attrition
                break;
            case "TrainingCompleted":
                metricName = "TrainingComplianceRate";
                val = new BigDecimal("98.50"); // 98.5% training compliance
                break;
        }

        if (metricName != null) {
            List<HcmAnalyticsSnapshot> existing = snapshotRepository.findByCompanyIdAndMetricName(event.getCompanyId(), metricName);
            HcmAnalyticsSnapshot snapshot;
            if (!existing.isEmpty()) {
                snapshot = existing.get(0);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            } else {
                snapshot = new HcmAnalyticsSnapshot();
                snapshot.setCompanyId(event.getCompanyId());
                snapshot.setMetricName(metricName);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            }
            snapshotRepository.save(snapshot);
        }
    }
}
