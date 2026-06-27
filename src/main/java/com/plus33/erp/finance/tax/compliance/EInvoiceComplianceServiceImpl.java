package com.plus33.erp.finance.tax.compliance;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;
import com.plus33.erp.finance.tax.repository.EInvoiceComplianceLogRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EInvoiceComplianceServiceImpl implements EInvoiceComplianceService {

    private final ComplianceProviderRegistry providerRegistry;
    private final DigitalSignatureProvider signatureProvider;
    private final EInvoiceComplianceLogRepository complianceLogRepository;
    private final CompanyRepository companyRepository;

    @Override
    public EInvoiceComplianceLog submitEInvoice(Long companyId, String docType, Long docId, String providerType, String ublXml) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        // Sign the payload using the signature provider
        String signature = signatureProvider.signPayload(ublXml);

        // Resolve compliance provider and submit the document
        ComplianceProvider provider = providerRegistry.getProvider(providerType);
        ComplianceSubmissionResult submissionResult = provider.submitDocument(docType, docId, ublXml);

        EInvoiceComplianceLog log = EInvoiceComplianceLog.builder()
                .company(company)
                .documentType(docType)
                .documentId(docId)
                .providerType(providerType)
                .xmlPayload(ublXml)
                .signatureHash(signature)
                .status(submissionResult.getStatus())
                .errorDetails(submissionResult.getErrorDetails())
                .governmentUuid(submissionResult.getGovernmentUuid())
                .occurredAt(LocalDateTime.now())
                .build();

        return complianceLogRepository.save(log);
    }
}
