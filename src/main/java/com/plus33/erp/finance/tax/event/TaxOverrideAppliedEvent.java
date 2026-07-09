/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.event
 * File              : TaxOverrideAppliedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxOverrideAppliedEventController
 * Related Service   : TaxOverrideAppliedEventService, TaxOverrideAppliedEventServiceImpl
 * Related Repository: TaxOverrideAppliedEventRepository
 * Related Entity    : TaxOverrideAppliedEvent
 * Related DTO       : N/A
 * Related Mapper    : TaxOverrideAppliedEventMapper
 * Related DB Table  : tax_override_applied_events
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

/**
 * Published when an approved manual tax override is applied during calculation.
 */
@Getter
public class TaxOverrideAppliedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final String reason;

    public TaxOverrideAppliedEvent(Object source, Long companyId, String documentType, Long documentId, String reason) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.reason = reason;
    }
}
