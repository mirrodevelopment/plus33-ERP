package com.plus33.erp.finance.budget.event;

import org.springframework.context.ApplicationEvent;

public class BudgetChangedEvent extends ApplicationEvent {
    private final Long companyId;

    public BudgetChangedEvent(Object source, Long companyId) {
        super(source);
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}
