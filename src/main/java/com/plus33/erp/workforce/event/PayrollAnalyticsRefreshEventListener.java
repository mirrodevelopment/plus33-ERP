/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.event
 * File              : PayrollAnalyticsRefreshEventListener.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollAnalyticsRefreshEventListenerController
 * Related Service   : PayrollAnalyticsRefreshEventListenerService, PayrollAnalyticsRefreshEventListenerServiceImpl
 * Related Repository: PayrollAnalyticsRefreshEventListenerRepository
 * Related Entity    : PayrollAnalyticsRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : PayrollAnalyticsRefreshEventListenerMapper
 * Related DB Table  : payroll_analytics_refresh_event_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollAnalyticsRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class PayrollAnalyticsRefreshEventListener {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Handles the payroll posted event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePayrollPosted(PayrollPostedEvent event) {
        refreshView("mv_payroll_dashboard");
        refreshView("mv_payroll_department");
    }

    /**
     * Handles the payroll calculated event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePayrollCalculated(PayrollCalculatedEvent event) {
        refreshView("mv_payroll_dashboard");
    }

    private void refreshView(String viewName) {
        try {
            Object exists = entityManager.createNativeQuery(
                "SELECT EXISTS (SELECT FROM pg_matviews WHERE matviewname = '" + viewName + "')"
            ).getSingleResult();
            if (Boolean.TRUE.equals(exists)) {
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW " + viewName).executeUpdate();
            }
        } catch (Exception ignored) {}
    }
}