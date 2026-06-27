package com.plus33.erp.finance.tax.compliance;

public interface ComplianceProvider {
    String getProviderType(); // ZATCA, IRP, PEPPOL
    ComplianceSubmissionResult submitDocument(String docType, Long docId, String ublXml);
}
