/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.analytics
 * File              : EsmAnalyticsProjectionListener.java
 * Purpose           : Component of Esm Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmAnalyticsProjectionListenerController
 * Related Service   : EsmAnalyticsProjectionListenerService, EsmAnalyticsProjectionListenerServiceImpl
 * Related Repository: EsmAnalyticsSnapshotRepository
 * Related Entity    : EsmAnalyticsProjectionListener
 * Related DTO       : N/A
 * Related Mapper    : EsmAnalyticsProjectionListenerMapper
 * Related DB Table  : esm_analytics_projection_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Esm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Esm Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.esm.analytics;

import com.plus33.erp.esm.entity.EsmAnalyticsSnapshot;
import com.plus33.erp.esm.repository.EsmAnalyticsSnapshotRepository;
import com.plus33.erp.esm.event.EsmEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmAnalyticsProjectionListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.analytics}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Esm Module.</p>
 *
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class EsmAnalyticsProjectionListener {

    private final EsmAnalyticsSnapshotRepository snapshotRepository;

    public EsmAnalyticsProjectionListener(EsmAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    /**
     * Handles the esm event event or exception in the business workflow.
     *
     * @param event the event input value
     */
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