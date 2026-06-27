package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class CashTransferCompletedEvent extends ApplicationEvent {
    private final Long transferId;

    public CashTransferCompletedEvent(Object source, Long transferId) {
        super(source);
        this.transferId = transferId;
    }

    public Long getTransferId() {
        return transferId;
    }
}
