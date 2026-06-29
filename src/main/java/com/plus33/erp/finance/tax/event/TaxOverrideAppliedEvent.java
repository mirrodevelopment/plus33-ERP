package com.plus33.erp.finance.tax.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Published when an approved manual tax override is applied during calculation.
 */
@Getter
public class TaxOverrideAppliedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final String reason;

    public TaxOverrideAppliedEvent(Object source, Long companyId, String documentType, Long documentId, String reason) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.reason = reason;
    }
}
