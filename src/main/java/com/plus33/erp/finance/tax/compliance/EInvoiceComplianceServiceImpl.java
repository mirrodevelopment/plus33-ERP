/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : EInvoiceComplianceServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EInvoiceComplianceController
 * Related Service   : EInvoiceComplianceServiceImpl
 * Related Repository: EInvoiceComplianceLogRepository, CompanyRepository
 * Related Entity    : EInvoiceCompliance
 * Related DTO       : N/A
 * Related Mapper    : EInvoiceComplianceMapper
 * Related DB Table  : e_invoice_compliances
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : EInvoiceComplianceController, EInvoiceComplianceServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements EInvoiceComplianceService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;
import com.plus33.erp.finance.tax.repository.EInvoiceComplianceLogRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code EInvoiceComplianceServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.compliance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EInvoiceComplianceController
 *   --> EInvoiceComplianceServiceImpl (this)
 *   --> Validate business rules
 *   --> EInvoiceComplianceRepository (read/write 'e_invoice_compliances')
 *   --> EInvoiceComplianceMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code e_invoice_compliances}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EInvoiceComplianceServiceImpl implements EInvoiceComplianceService {

    private final ComplianceProviderRegistry providerRegistry;
    private final DigitalSignatureProvider signatureProvider;
    private final EInvoiceComplianceLogRepository complianceLogRepository;
    private final CompanyRepository companyRepository;

    /**
     * Submits the e invoice for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param docType the docType input value
     * @param docId the docId input value
     * @param providerType the providerType input value
     * @param ublXml the ublXml input value
     * @return the EInvoiceComplianceLog result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Submits the e invoice for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param docType the docType input value
     * @param docId the docId input value
     * @param providerType the providerType input value
     * @param ublXml the ublXml input value
     * @return the EInvoiceComplianceLog result
     * @throws BusinessException if a business rule is violated
     */
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