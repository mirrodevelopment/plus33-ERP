package com.plus33.erp.finance.tax.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * Published after a successful tax calculation completes.
 */
@Getter
public class TaxCalculatedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final BigDecimal totalTaxAmount;

    public TaxCalculatedEvent(Object source, Long companyId, String documentType, Long documentId, BigDecimal totalTaxAmount) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.totalTaxAmount = totalTaxAmount;
    }
}
