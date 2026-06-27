package com.plus33.erp.finance.treasury.entity;

import org.springframework.context.ApplicationEvent;

public class InvestmentMaturedEvent extends ApplicationEvent {
    private final Long investmentId;

    public InvestmentMaturedEvent(Object source, Long investmentId) {
        super(source);
        this.investmentId = investmentId;
    }

    public Long getInvestmentId() {
        return investmentId;
    }
}
