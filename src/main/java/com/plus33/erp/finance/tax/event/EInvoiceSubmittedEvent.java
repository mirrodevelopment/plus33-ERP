/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.event
 * File              : EInvoiceSubmittedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EInvoiceSubmittedEventController
 * Related Service   : EInvoiceSubmittedEventService, EInvoiceSubmittedEventServiceImpl
 * Related Repository: EInvoiceSubmittedEventRepository
 * Related Entity    : EInvoiceSubmittedEvent
 * Related DTO       : N/A
 * Related Mapper    : EInvoiceSubmittedEventMapper
 * Related DB Table  : e_invoice_submitted_events
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
 * Published after an e-invoice is signed and submitted to a government portal.
 */
@Getter
public class EInvoiceSubmittedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final String providerType;
    private final String governmentUuid;
    private final boolean success;

    public EInvoiceSubmittedEvent(Object source, Long companyId, String documentType, Long documentId,
                                  String providerType, String governmentUuid, boolean success) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.providerType = providerType;
        this.governmentUuid = governmentUuid;
        this.success = success;
    }
}
