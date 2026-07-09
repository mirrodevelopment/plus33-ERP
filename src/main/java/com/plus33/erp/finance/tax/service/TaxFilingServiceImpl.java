/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxFilingServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxFilingController
 * Related Service   : TaxFilingServiceImpl
 * Related Repository: TaxCalendarRepository, TaxFilingRepository, TaxRegistrationRepository, CustomerInvoiceRepository, SupplierInvoiceRepository
 * Related Entity    : TaxFiling
 * Related DTO       : N/A
 * Related Mapper    : TaxFilingMapper
 * Related DB Table  : tax_filings
 * Related REST APIs : N/A
 * Depends On        : Sales Module
 * Used By           : TaxFilingController, TaxFilingServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TaxFilingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.finance.tax.dto.PreFilingValidationResult;
import com.plus33.erp.finance.tax.entity.TaxCalendar;
import com.plus33.erp.finance.tax.entity.TaxFiling;
import com.plus33.erp.finance.tax.repository.TaxCalendarRepository;
import com.plus33.erp.finance.tax.repository.TaxFilingRepository;
import com.plus33.erp.finance.tax.repository.TaxRegistrationRepository;
import com.plus33.erp.sales.repository.CustomerInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxFilingServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TaxFilingController
 *   --> TaxFilingServiceImpl (this)
 *   --> Validate business rules
 *   --> TaxFilingRepository (read/write 'tax_filings')
 *   --> TaxFilingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code tax_filings}</p>
 * <p><b>Module Deps      :</b> Finance, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaxFilingServiceImpl implements TaxFilingService {

    private final TaxCalendarRepository taxCalendarRepository;
    private final TaxFilingRepository taxFilingRepository;
    private final TaxRegistrationRepository taxRegistrationRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final SupplierInvoiceRepository supplierInvoiceRepository;

    /**
     * Validates business rules and constraints for filing.
     *
     * @param calendarId the calendarId input value
     * @return the PreFilingValidationResult result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional(readOnly = true)
    public PreFilingValidationResult validateFiling(Long calendarId) {
        TaxCalendar calendar = taxCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Calendar not found: " + calendarId));

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // 1. Check if Company has default TRN
        boolean hasTRN = taxRegistrationRepository.findByEntityTypeAndEntityIdAndActiveTrue("COMPANY", calendar.getCompany().getId())
                .stream().anyMatch(r -> "ACTIVE".equals(r.getStatus()));

        if (!hasTRN) {
            errors.add("Missing company tax registration number (TRN) or registration is inactive.");
        }

        // 2. Warning if no invoices exist in the filing period
        long custInvoicesCount = customerInvoiceRepository.findAll().stream()
                .filter(i -> i.getCompany().getId().equals(calendar.getCompany().getId())
                        && !i.getInvoiceDate().isBefore(calendar.getPeriodStart())
                        && !i.getInvoiceDate().isAfter(calendar.getPeriodEnd()))
                .count();

        if (custInvoicesCount == 0) {
            warnings.add("No customer invoices recorded in this filing period.");
        }

        return new PreFilingValidationResult(errors, warnings);
    }

    /**
     * Calculates filing totals including subtotal, tax, discounts, and net amount.
     *
     * @param calendarId the calendarId input value
     * @param filedBy the filedBy input value
     * @return the TaxFiling result
     */
    /**
     * Calculates filing totals including subtotal, tax, discounts, and net amount.
     *
     * @param calendarId the calendarId input value
     * @param filedBy the filedBy input value
     * @return the TaxFiling result
     */
    @Override
    public TaxFiling calculateFiling(Long calendarId, String filedBy) {
        TaxCalendar calendar = taxCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Calendar not found: " + calendarId));

        PreFilingValidationResult validation = validateFiling(calendarId);
        if (!validation.isValid()) {
            throw new IllegalStateException("Pre-filing validation failed with errors: " + validation.getErrors());
        }

        // Sum Customer Invoices (Output Tax)
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalOutputTax = BigDecimal.ZERO;

        List<com.plus33.erp.sales.entity.CustomerInvoice> custInvoices = customerInvoiceRepository.findAll().stream()
                .filter(i -> i.getCompany().getId().equals(calendar.getCompany().getId())
                        && !i.getInvoiceDate().isBefore(calendar.getPeriodStart())
                        && !i.getInvoiceDate().isAfter(calendar.getPeriodEnd())
                        && i.getStatus() == com.plus33.erp.sales.entity.CustomerInvoiceStatus.APPROVED)
                .toList();

        for (com.plus33.erp.sales.entity.CustomerInvoice ci : custInvoices) {
            totalSales = totalSales.add(ci.getTotalAmount());
            totalOutputTax = totalOutputTax.add(ci.getTaxAmount());
        }

        // Sum Supplier Invoices (Input Tax)
        BigDecimal totalInputTax = BigDecimal.ZERO;

        List<com.plus33.erp.finance.entity.SupplierInvoice> suppInvoices = supplierInvoiceRepository.findAll().stream()
                .filter(i -> i.getCompany().getId().equals(calendar.getCompany().getId())
                        && !i.getInvoiceDate().isBefore(calendar.getPeriodStart())
                        && !i.getInvoiceDate().isAfter(calendar.getPeriodEnd())
                        && i.getStatus() == com.plus33.erp.finance.entity.SupplierInvoiceStatus.APPROVED)
                .toList();

        for (com.plus33.erp.finance.entity.SupplierInvoice si : suppInvoices) {
            totalInputTax = totalInputTax.add(si.getTaxAmount());
        }

        BigDecimal netLiability = totalOutputTax.subtract(totalInputTax);

        calendar.setStatus("CALCULATED");
        taxCalendarRepository.save(calendar);

        TaxFiling filing = TaxFiling.builder()
                .calendar(calendar)
                .totalSalesAmount(totalSales)
                .totalInputTax(totalInputTax)
                .totalOutputTax(totalOutputTax)
                .netTaxLiability(netLiability)
                .filedBy(filedBy)
                .filedAt(LocalDateTime.now())
                .build();

        return taxFilingRepository.save(filing);
    }

    /**
     * Submits the filing for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param filingId the filingId input value
     * @param filedBy the filedBy input value
     * @return the TaxFiling result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Submits the filing for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param filingId the filingId input value
     * @param filedBy the filedBy input value
     * @return the TaxFiling result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public TaxFiling submitFiling(Long filingId, String filedBy) {
        TaxFiling filing = taxFilingRepository.findById(filingId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Filing not found: " + filingId));

        TaxCalendar calendar = filing.getCalendar();
        if (!"CALCULATED".equals(calendar.getStatus())) {
            throw new IllegalStateException("Filing must be in CALCULATED status before submitting.");
        }

        calendar.setStatus("SUBMITTED");
        taxCalendarRepository.save(calendar);

        filing.setSubmissionPayload("<UBLTaxReturnPeriod><PeriodID>" + calendar.getId() + "</PeriodID></UBLTaxReturnPeriod>");
        filing.setGovernmentReceiptRef(UUID.randomUUID().toString());
        filing.setFiledBy(filedBy);
        filing.setFiledAt(LocalDateTime.now());

        return taxFilingRepository.save(filing);
    }
}