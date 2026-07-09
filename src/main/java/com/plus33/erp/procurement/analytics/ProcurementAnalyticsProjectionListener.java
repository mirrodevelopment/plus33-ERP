/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.analytics
 * File              : ProcurementAnalyticsProjectionListener.java
 * Purpose           : Component of Procurement Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementAnalyticsProjectionListenerController
 * Related Service   : ProcurementAnalyticsProjectionListenerService, ProcurementAnalyticsProjectionListenerServiceImpl
 * Related Repository: ProcurementAnalyticsSnapshotRepository
 * Related Entity    : ProcurementAnalyticsProjectionListener
 * Related DTO       : N/A
 * Related Mapper    : ProcurementAnalyticsProjectionListenerMapper
 * Related DB Table  : procurement_analytics_projection_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Procurement Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.procurement.analytics;

import com.plus33.erp.procurement.entity.ProcurementAnalyticsSnapshot;
import com.plus33.erp.procurement.repository.ProcurementAnalyticsSnapshotRepository;
import com.plus33.erp.procurement.event.ProcurementEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementAnalyticsProjectionListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.analytics}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ProcurementAnalyticsProjectionListener {

    private final ProcurementAnalyticsSnapshotRepository snapshotRepository;

    public ProcurementAnalyticsProjectionListener(ProcurementAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    /**
     * Handles the procurement event event or exception in the business workflow.
     *
     * @param event the event input value
     */
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