package com.plus33.erp.procurement.analytics;

import com.plus33.erp.procurement.entity.ProcurementAnalyticsSnapshot;
import com.plus33.erp.procurement.repository.ProcurementAnalyticsSnapshotRepository;
import com.plus33.erp.procurement.event.ProcurementEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ProcurementAnalyticsProjectionListener {

    private final ProcurementAnalyticsSnapshotRepository snapshotRepository;

    public ProcurementAnalyticsProjectionListener(ProcurementAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @EventListener
    public void onProcurementEvent(ProcurementEvent event) {
        String metricName = null;
        BigDecimal val = BigDecimal.ONE;

        switch (event.getEventType()) {
            case "SupplierEvaluated":
                metricName = "SupplierPerformanceAvg";
                val = new BigDecimal("96.50");
                break;
            case "InvoiceMatched":
                metricName = "ThreeWayMatchSuccessRate";
                val = new BigDecimal("99.10");
                break;
        }

        if (metricName != null) {
            List<ProcurementAnalyticsSnapshot> existing = snapshotRepository.findByCompanyIdAndMetricName(event.getCompanyId(), metricName);
            ProcurementAnalyticsSnapshot snapshot;
            if (!existing.isEmpty()) {
                snapshot = existing.get(0);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            } else {
                snapshot = new ProcurementAnalyticsSnapshot();
                snapshot.setCompanyId(event.getCompanyId());
                snapshot.setMetricName(metricName);
                snapshot.setMetricValue(val);
                snapshot.setRecordedDate(LocalDate.now());
            }
            snapshotRepository.save(snapshot);
        }
    }
}
