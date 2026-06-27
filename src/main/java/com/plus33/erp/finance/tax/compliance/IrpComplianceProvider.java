package com.plus33.erp.finance.tax.compliance;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class IrpComplianceProvider implements ComplianceProvider {

    @Override
    public String getProviderType() {
        return "IRP";
    }

    @Override
    public ComplianceSubmissionResult submitDocument(String docType, Long docId, String ublXml) {
        if (ublXml == null || ublXml.trim().isEmpty()) {
            return ComplianceSubmissionResult.builder()
                    .success(false)
                    .status("REJECTED")
                    .errorDetails("UBL XML payload is empty.")
                    .build();
        }
        return ComplianceSubmissionResult.builder()
                .success(true)
                .status("ACCEPTED")
                .governmentUuid(UUID.randomUUID().toString())
                .build();
    }
}
