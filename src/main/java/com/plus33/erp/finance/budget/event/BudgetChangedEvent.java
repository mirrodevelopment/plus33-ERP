/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.event
 * File              : BudgetChangedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetChangedEventController
 * Related Service   : BudgetChangedEventService, BudgetChangedEventServiceImpl
 * Related Repository: BudgetChangedEventRepository
 * Related Entity    : BudgetChangedEvent
 * Related DTO       : N/A
 * Related Mapper    : BudgetChangedEventMapper
 * Related DB Table  : budget_changed_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Finance Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.event;

import org.springframework.context.ApplicationEvent;

public class BudgetChangedEvent extends ApplicationEvent {
    private final Long companyId;

    public BudgetChangedEvent(Object source, Long companyId) {
        super(source);
        this.companyId = companyId;
    }

    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() {
        return companyId;
    }
}
