/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : InvestmentMaturedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InvestmentMaturedEventController
 * Related Service   : InvestmentMaturedEventService, InvestmentMaturedEventServiceImpl
 * Related Repository: InvestmentMaturedEventRepository
 * Related Entity    : InvestmentMaturedEvent
 * Related DTO       : N/A
 * Related Mapper    : InvestmentMaturedEventMapper
 * Related DB Table  : investment_matured_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Finance Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class InvestmentMaturedEvent extends ApplicationEvent {
    private final Long investmentId;

    public InvestmentMaturedEvent(Object source, Long investmentId) {
        super(source);
        this.investmentId = investmentId;
    }

    /**
     * Retrieves investment id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInvestmentId() {
        return investmentId;
    }
}
