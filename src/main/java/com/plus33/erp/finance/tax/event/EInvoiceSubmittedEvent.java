package com.plus33.erp.finance.tax.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Published after an e-invoice is signed and submitted to a government portal.
 */
@Getter
public class EInvoiceSubmittedEvent extends ApplicationEvent {
    private final Long companyId;
    private final String documentType;
    private final Long documentId;
    private final String providerType;
    private final String governmentUuid;
    private final boolean success;

    public EInvoiceSubmittedEvent(Object source, Long companyId, String documentType, Long documentId,
                                  String providerType, String governmentUuid, boolean success) {
        super(source);
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentId = documentId;
        this.providerType = providerType;
        this.governmentUuid = governmentUuid;
        this.success = success;
    }
}
