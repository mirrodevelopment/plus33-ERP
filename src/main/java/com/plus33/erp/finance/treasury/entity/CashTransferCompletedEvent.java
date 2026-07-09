/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashTransferCompletedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashTransferCompletedEventController
 * Related Service   : CashTransferCompletedEventService, CashTransferCompletedEventServiceImpl
 * Related Repository: CashTransferCompletedEventRepository
 * Related Entity    : CashTransferCompletedEvent
 * Related DTO       : N/A
 * Related Mapper    : CashTransferCompletedEventMapper
 * Related DB Table  : cash_transfer_completed_events
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

public class CashTransferCompletedEvent extends ApplicationEvent {
    private final Long transferId;

    public CashTransferCompletedEvent(Object source, Long transferId) {
        super(source);
        this.transferId = transferId;
    }

    /**
     * Retrieves transfer id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTransferId() {
        return transferId;
    }
}
