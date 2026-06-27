package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class ReconciliationCompletedEvent extends ApplicationEvent {
    private final Long statementId;

    public ReconciliationCompletedEvent(Object source, Long statementId) {
        super(source);
        this.statementId = statementId;
    }

    public Long getStatementId() {
        return statementId;
    }
}
