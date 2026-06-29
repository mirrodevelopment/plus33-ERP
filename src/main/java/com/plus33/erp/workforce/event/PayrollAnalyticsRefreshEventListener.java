package com.plus33.erp.workforce.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PayrollAnalyticsRefreshEventListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePayrollPosted(PayrollPostedEvent event) {
        refreshView("mv_payroll_dashboard");
        refreshView("mv_payroll_department");
    }

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
