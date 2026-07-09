/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.event
 * File              : BudgetEventListener.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetEventListenerController
 * Related Service   : BudgetEventListenerService, BudgetEventListenerServiceImpl
 * Related Repository: BudgetEventListenerRepository
 * Related Entity    : BudgetEventListener
 * Related DTO       : N/A
 * Related Mapper    : BudgetEventListenerMapper
 * Related DB Table  : budget_event_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class BudgetEventListener {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Handles the budget changed event or exception in the business workflow.
     *
     * @param event the event input value
     */
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