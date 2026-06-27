package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class PaymentBatchApprovedEvent extends ApplicationEvent {
    private final Long batchId;

    public PaymentBatchApprovedEvent(Object source, Long batchId) {
        super(source);
        this.batchId = batchId;
    }

    public Long getBatchId() {
        return batchId;
    }
}
