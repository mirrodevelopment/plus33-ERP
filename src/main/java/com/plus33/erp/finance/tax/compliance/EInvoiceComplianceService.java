package com.plus33.erp.finance.tax.compliance;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;

public interface EInvoiceComplianceService {
    EInvoiceComplianceLog submitEInvoice(Long companyId, String docType, Long docId, String providerType, String ublXml);
}
