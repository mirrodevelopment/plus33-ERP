/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.analytics
 * File              : ProjectAnalyticsProjectionListener.java
 * Purpose           : Component of Project Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectAnalyticsProjectionListenerController
 * Related Service   : ProjectAnalyticsProjectionListenerService, ProjectAnalyticsProjectionListenerServiceImpl
 * Related Repository: ProjectAnalyticsSnapshotRepository
 * Related Entity    : ProjectAnalyticsProjectionListener
 * Related DTO       : N/A
 * Related Mapper    : ProjectAnalyticsProjectionListenerMapper
 * Related DB Table  : project_analytics_projection_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Project Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Project Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.project.analytics;

import com.plus33.erp.project.entity.ProjectAnalyticsSnapshot;
import com.plus33.erp.project.repository.ProjectAnalyticsSnapshotRepository;
import com.plus33.erp.project.event.ProjectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectAnalyticsProjectionListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.analytics}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Project Module.</p>
 *
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ProjectAnalyticsProjectionListener {

    private final ProjectAnalyticsSnapshotRepository snapshotRepository;

    public ProjectAnalyticsProjectionListener(ProjectAnalyticsSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    /**
     * Handles the project event event or exception in the business workflow.
     *
     * @param event the event input value
     */
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