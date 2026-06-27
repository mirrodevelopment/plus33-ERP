package com.plus33.erp.finance.budget.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BudgetEventListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleBudgetChanged(BudgetChangedEvent event) {
        log.info("Handling BudgetChangedEvent for company ID: {}", event.getCompanyId());
        try {
            Object exists = entityManager.createNativeQuery(
                "SELECT EXISTS (SELECT FROM pg_matviews WHERE matviewname = 'mv_budget_variance_analysis')"
            ).getSingleResult();
            
            if (Boolean.TRUE.equals(exists)) {
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW mv_budget_variance_analysis").executeUpdate();
                log.info("Materialized view mv_budget_variance_analysis refreshed successfully");
            } else {
                log.warn("Materialized view mv_budget_variance_analysis does not exist. Skipping refresh.");
            }
        } catch (Exception e) {
            log.warn("Could not refresh materialized view mv_budget_variance_analysis: {}", e.getMessage());
        }
    }
}
