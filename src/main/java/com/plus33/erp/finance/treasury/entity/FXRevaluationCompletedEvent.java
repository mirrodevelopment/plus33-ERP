package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class FXRevaluationCompletedEvent extends ApplicationEvent {
    private final Long companyId;

    public FXRevaluationCompletedEvent(Object source, Long companyId) {
        super(source);
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}
