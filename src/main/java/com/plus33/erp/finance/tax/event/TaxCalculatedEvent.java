/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.event
 * File              : TaxCalculatedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculatedEventController
 * Related Service   : TaxCalculatedEventService, TaxCalculatedEventServiceImpl
 * Related Repository: TaxCalculatedEventRepository
 * Related Entity    : TaxCalculatedEvent
 * Related DTO       : N/A
 * Related Mapper    : TaxCalculatedEventMapper
 * Related DB Table  : tax_calculated_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Finance Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.finance.tax.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * Published after a successful tax calculation completes.
 */
@Getter
public class TaxCalculatedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final BigDecimal totalTaxAmount;

    public TaxCalculatedEvent(Object source, Long companyId, String documentType, Long documentId, BigDecimal totalTaxAmount) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.totalTaxAmount = totalTaxAmount;
    }
}
