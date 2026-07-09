/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : StatementImportedEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StatementImportedEventController
 * Related Service   : StatementImportedEventService, StatementImportedEventServiceImpl
 * Related Repository: StatementImportedEventRepository
 * Related Entity    : StatementImportedEvent
 * Related DTO       : N/A
 * Related Mapper    : StatementImportedEventMapper
 * Related DB Table  : statement_imported_events
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

public class StatementImportedEvent extends ApplicationEvent {
    private final Long statementId;

    public StatementImportedEvent(Object source, Long statementId) {
        super(source);
        this.statementId = statementId;
    }

    /**
     * Retrieves statement id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getStatementId() {
        return statementId;
    }
}
