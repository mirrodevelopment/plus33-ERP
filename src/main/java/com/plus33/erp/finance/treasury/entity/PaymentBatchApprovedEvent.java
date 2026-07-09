/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : PaymentBatchApprovedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentBatchApprovedEventController
 * Related Service   : PaymentBatchApprovedEventService, PaymentBatchApprovedEventServiceImpl
 * Related Repository: PaymentBatchApprovedEventRepository
 * Related Entity    : PaymentBatchApprovedEvent
 * Related DTO       : N/A
 * Related Mapper    : PaymentBatchApprovedEventMapper
 * Related DB Table  : payment_batch_approved_events
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

public class PaymentBatchApprovedEvent extends ApplicationEvent {
    private final Long batchId;

    public PaymentBatchApprovedEvent(Object source, Long batchId) {
        super(source);
        this.batchId = batchId;
    }

    /**
     * Retrieves batch id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getBatchId() {
        return batchId;
    }
}
