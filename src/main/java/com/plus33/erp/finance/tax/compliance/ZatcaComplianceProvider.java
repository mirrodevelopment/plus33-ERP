package com.plus33.erp.finance.tax.compliance;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ZatcaComplianceProvider implements ComplianceProvider {

    @Override
    public String getProviderType() {
        return "ZATCA";
    }

    @Override
    public ComplianceSubmissionResult submitDocument(String docType, Long docId, String ublXml) {
        if (ublXml == null || !ublXml.contains("trn")) {
            return ComplianceSubmissionResult.builder()
                    .success(false)
                    .status("REJECTED")
                    .errorDetails("Missing Tax Registration Number (TRN) in ZATCA UBL XML payload.")
                    .build();
        }
        return ComplianceSubmissionResult.builder()
                .success(true)
                .status("ACCEPTED")
                .governmentUuid(UUID.randomUUID().toString())
                .build();
    }
}
