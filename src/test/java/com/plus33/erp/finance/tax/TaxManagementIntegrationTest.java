package com.plus33.erp.finance.tax;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.tax.compliance.*;
import com.plus33.erp.finance.tax.dto.*;
import com.plus33.erp.finance.tax.entity.*;
import com.plus33.erp.finance.tax.repository.*;
import com.plus33.erp.finance.tax.service.*;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ProductCategory;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.inventory.repository.ProductCategoryRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TaxManagementIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private TaxCategoryRepository taxCategoryRepository;
    @Autowired
    private TaxPostingProfileRepository taxPostingProfileRepository;
    @Autowired
    private TaxRateRepository taxRateRepository;
    @Autowired
    private TaxGroupRepository taxGroupRepository;
    @Autowired
    private TaxGroupLineRepository taxGroupLineRepository;
    @Autowired
    private TaxRegistrationRepository taxRegistrationRepository;
    @Autowired
    private TaxExemptionCertificateRepository taxExemptionCertificateRepository;
    @Autowired
    private TaxDeterminationRuleRepository taxDeterminationRuleRepository;

    @Autowired
    private TaxCalculationEngine taxCalculationEngine;
    @Autowired
    private TaxDeterminationRuleEngine taxDeterminationRuleEngine;
    @Autowired
    private TaxFilingService taxFilingService;
    @Autowired
    private TaxCalendarRepository taxCalendarRepository;
    @Autowired
    private TaxFilingRepository taxFilingRepository;
    @Autowired
    private EInvoiceComplianceService eInvoiceComplianceService;

    private Company company;
    private Customer customer;
    private Product product;
    private TaxCategory vatCategory;
    private TaxRate vatRate;
    private TaxGroup vatGroup;
    private Account inputTaxAccount;
    private Account outputTaxAccount;
    private Account reverseChargeAccount;
    private Account recoverableAccount;

    @BeforeEach
    public void setUp() {
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setCode("PLUS33_GLOBAL");
                    newCompany.setName("Global Company");
                    newCompany.setActive(true);
                    return companyRepository.save(newCompany);
                });

        customer = customerRepository.findByCompanyIdAndCode(company.getId(), "CUST_TAX_TEST")
                .orElseGet(() -> {
                    return customerRepository.save(Customer.builder()
                            .company(company)
                            .code("CUST_TAX_TEST")
                            .name("Tax Test Customer")
                            .customerType(com.plus33.erp.sales.entity.CustomerType.B2B)
                            .currencyCode("AED")
                            .build());
                });

        ProductCategory category = productCategoryRepository.findByCode("CAT_TAX_TEST")
                .orElseGet(() -> {
                    return productCategoryRepository.save(new ProductCategory(null, "CAT_TAX_TEST", "Test Category", null, true, LocalDateTime.now(), LocalDateTime.now()));
                });

        UnitOfMeasure uom = unitOfMeasureRepository.findByCode("PCS")
                .orElseGet(() -> {
                    return unitOfMeasureRepository.save(new UnitOfMeasure(null, "PCS", "Pieces"));
                });

        product = productRepository.findByCode("PROD_TAX_TEST")
                .orElseGet(() -> {
                    return productRepository.save(Product.builder()
                            .code("PROD_TAX_TEST")
                            .name("Tax Test Product")
                            .category(category)
                            .unit(uom)
                            .productType("GOODS")
                            .active(true)
                            .build());
                });

        // Initialize Accounts
        inputTaxAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2200")
                .orElseGet(() -> accountRepository.save(Account.builder().company(company).accountCode("2200").accountName("Tax Payable").accountType("LIABILITY").active(true).build()));

        outputTaxAccount = inputTaxAccount;

        reverseChargeAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2201")
                .orElseGet(() -> accountRepository.save(Account.builder().company(company).accountCode("2201").accountName("Reverse Charge Offset").accountType("LIABILITY").active(true).build()));

        recoverableAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1410")
                .orElseGet(() -> accountRepository.save(Account.builder().company(company).accountCode("1410").accountName("Recoverable Tax").accountType("ASSET").active(true).build()));

        // Create Tax Categories and Rates
        vatCategory = taxCategoryRepository.findByCode("VAT_15")
                .orElseGet(() -> taxCategoryRepository.save(TaxCategory.builder().code("VAT_15").name("Value Added Tax 15%").active(true).build()));

        taxPostingProfileRepository.findByCompanyIdAndCategoryId(company.getId(), vatCategory.getId())
                .orElseGet(() -> taxPostingProfileRepository.save(TaxPostingProfile.builder()
                        .company(company)
                        .category(vatCategory)
                        .inputTaxAccount(inputTaxAccount)
                        .outputTaxAccount(outputTaxAccount)
                        .recoverableAccount(recoverableAccount)
                        .reverseChargeAccount(reverseChargeAccount)
                        .active(true)
                        .build()));

        vatRate = taxRateRepository.findActiveRatesByCategoryIdAndDate(vatCategory.getId(), LocalDate.now())
                .stream().findFirst()
                .orElseGet(() -> taxRateRepository.save(TaxRate.builder()
                        .category(vatCategory)
                        .ratePercent(BigDecimal.valueOf(15.00))
                        .effectiveFrom(LocalDate.now().minusDays(10))
                        .active(true)
                        .build()));

        vatGroup = taxGroupRepository.findByCode("VAT_GROUP_15")
                .orElseGet(() -> {
                    TaxGroup tg = taxGroupRepository.save(TaxGroup.builder().code("VAT_GROUP_15").name("VAT Group 15%").active(true).build());
                    TaxGroupLine line = taxGroupLineRepository.save(TaxGroupLine.builder().group(tg).rate(vatRate).build());
                    tg.getLines().add(line);
                    return tg;
                });
    }

    @Test
    public void testTaxRegistrationResolution() {
        TaxRegistration reg = TaxRegistration.builder()
                .entityType("COMPANY")
                .entityId(company.getId())
                .taxScheme("TRN")
                .registrationNumber("123456789")
                .isDefault(true)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .status("ACTIVE")
                .active(true)
                .build();
        taxRegistrationRepository.save(reg);

        List<TaxRegistration> companyRegs = taxRegistrationRepository.findByEntityTypeAndEntityIdAndActiveTrue("COMPANY", company.getId());
        assertFalse(companyRegs.isEmpty());
        assertEquals("123456789", companyRegs.get(0).getRegistrationNumber());
        assertTrue(companyRegs.get(0).isDefault());
    }

    @Test
    public void testInclusiveVsExclusiveCalculations() {
        // Setup Tax Determination Rule for Fallback
        taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                .company(company)
                .ruleName("Fallback Sales Rule")
                .priority(100)
                .documentType("SALES_INVOICE")
                .taxGroup(vatGroup)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        // 1. Exclusive Tax Calculation
        TaxCalculationRequest exclusiveReq = TaxCalculationRequest.builder()
                .companyId(company.getId())
                .transactionDate(LocalDate.now())
                .documentType("SALES_INVOICE")
                .customerId(customer.getId())
                .customerTaxProfile("STANDARD")
                .lines(List.of(TaxCalculationLineRequest.builder()
                        .lineId(1L)
                        .amount(BigDecimal.valueOf(100.00))
                        .taxInclusive(false)
                        .productTaxCategory(product.getCategory().getCode())
                        .build()))
                .build();

        TaxCalculationResult exclusiveRes = taxCalculationEngine.calculateTax(exclusiveReq);
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(exclusiveRes.getTotalNetAmount()));
        assertEquals(0, BigDecimal.valueOf(15.00).compareTo(exclusiveRes.getTotalTaxAmount()));
        assertEquals(0, BigDecimal.valueOf(115.00).compareTo(exclusiveRes.getTotalGrossAmount()));

        // 2. Inclusive Tax Calculation
        TaxCalculationRequest inclusiveReq = TaxCalculationRequest.builder()
                .companyId(company.getId())
                .transactionDate(LocalDate.now())
                .documentType("SALES_INVOICE")
                .customerId(customer.getId())
                .customerTaxProfile("STANDARD")
                .lines(List.of(TaxCalculationLineRequest.builder()
                        .lineId(1L)
                        .amount(BigDecimal.valueOf(115.00))
                        .taxInclusive(true)
                        .productTaxCategory(product.getCategory().getCode())
                        .build()))
                .build();

        TaxCalculationResult inclusiveRes = taxCalculationEngine.calculateTax(inclusiveReq);
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(inclusiveRes.getTotalNetAmount()));
        assertEquals(0, BigDecimal.valueOf(15.00).compareTo(inclusiveRes.getTotalTaxAmount()));
        assertEquals(0, BigDecimal.valueOf(115.00).compareTo(inclusiveRes.getTotalGrossAmount()));
    }

    @Test
    public void testExemptionCertificateLookup() {
        // Create an exemption certificate for customer
        taxExemptionCertificateRepository.save(TaxExemptionCertificate.builder()
                .company(company)
                .customerId(customer.getId())
                .certificateNumber("CERT-FREE-99")
                .exemptionReason("Government entity")
                .effectiveFrom(LocalDate.now().minusDays(5))
                .effectiveTo(LocalDate.now().plusDays(10))
                .active(true)
                .build());

        taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                .company(company)
                .ruleName("Fallback Sales Rule")
                .priority(100)
                .documentType("SALES_INVOICE")
                .taxGroup(vatGroup)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        TaxCalculationRequest req = TaxCalculationRequest.builder()
                .companyId(company.getId())
                .transactionDate(LocalDate.now())
                .documentType("SALES_INVOICE")
                .customerId(customer.getId())
                .customerTaxProfile("STANDARD")
                .lines(List.of(TaxCalculationLineRequest.builder()
                        .lineId(1L)
                        .amount(BigDecimal.valueOf(100.00))
                        .taxInclusive(false)
                        .productTaxCategory(product.getCategory().getCode())
                        .build()))
                .build();

        TaxCalculationResult res = taxCalculationEngine.calculateTax(req);
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(res.getTotalNetAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(res.getTotalTaxAmount())); // ZERO tax due to exemption certificate
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(res.getTotalGrossAmount()));
    }

    @Test
    public void testTaxDeterminationRulesPrecedence() {
        // Save Fallback Rule (Priority 100)
        taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                .company(company)
                .ruleName("Fallback Sales Rule")
                .priority(100)
                .documentType("SALES_INVOICE")
                .taxGroup(vatGroup)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        // Save High Specificity Rule (Priority 10) matching customer profile EXEMPT
        TaxCategory zeroCategory = taxCategoryRepository.save(TaxCategory.builder().code("VAT_0").name("Zero Rated").active(true).build());
        TaxRate zeroRate = taxRateRepository.save(TaxRate.builder().category(zeroCategory).ratePercent(BigDecimal.ZERO).effectiveFrom(LocalDate.now().minusDays(10)).active(true).build());
        taxPostingProfileRepository.save(TaxPostingProfile.builder().company(company).category(zeroCategory).inputTaxAccount(inputTaxAccount).outputTaxAccount(outputTaxAccount).active(true).build());
        TaxGroup zeroGroup = taxGroupRepository.save(TaxGroup.builder().code("VAT_GROUP_0").name("Zero Group").active(true).build());
        TaxGroupLine zeroLine = taxGroupLineRepository.save(TaxGroupLine.builder().group(zeroGroup).rate(zeroRate).build());
        zeroGroup.getLines().add(zeroLine);

        taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                .company(company)
                .ruleName("Specific Exempt Rule")
                .priority(10)
                .documentType("SALES_INVOICE")
                .customerTaxProfile("EXEMPT")
                .taxGroup(zeroGroup)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        // Resolve for STANDARD profile -> fallback group VAT_GROUP_15 (15% rate)
        TaxGroup resolvedStandard = taxDeterminationRuleEngine.determineTaxGroup(
                company.getId(), "SALES_INVOICE", "STANDARD", null, product.getCategory().getCode(),
                null, null, null, null, null, LocalDate.now()
        );
        assertEquals("VAT_GROUP_15", resolvedStandard.getCode());

        // Resolve for EXEMPT profile -> high priority group VAT_GROUP_0 (0% rate)
        TaxGroup resolvedExempt = taxDeterminationRuleEngine.determineTaxGroup(
                company.getId(), "SALES_INVOICE", "EXEMPT", null, product.getCategory().getCode(),
                null, null, null, null, null, LocalDate.now()
        );
        assertEquals("VAT_GROUP_0", resolvedExempt.getCode());
    }

    @Test
    public void testReverseChargeAccountingOffset() {
        // Setup a reverse charge posting profile
        TaxCategory rcCategory = taxCategoryRepository.save(TaxCategory.builder().code("VAT_RC").name("Reverse Charge VAT").active(true).build());
        taxPostingProfileRepository.save(TaxPostingProfile.builder()
                .company(company)
                .category(rcCategory)
                .inputTaxAccount(inputTaxAccount)
                .outputTaxAccount(outputTaxAccount)
                .recoverableAccount(recoverableAccount)
                .reverseChargeAccount(reverseChargeAccount)
                .active(true)
                .build());

        TaxRate rcRate = taxRateRepository.save(TaxRate.builder()
                .category(rcCategory)
                .ratePercent(BigDecimal.valueOf(15.00))
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        TaxGroup rcGroup = taxGroupRepository.save(TaxGroup.builder().code("VAT_GROUP_RC").name("Reverse Charge VAT Group").active(true).build());
        TaxGroupLine rcLine = taxGroupLineRepository.save(TaxGroupLine.builder().group(rcGroup).rate(rcRate).build());
        rcGroup.getLines().add(rcLine);

        taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                .company(company)
                .ruleName("Reverse Charge Rule")
                .priority(50)
                .documentType("PURCHASE_INVOICE")
                .taxGroup(rcGroup)
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build());

        TaxCalculationRequest req = TaxCalculationRequest.builder()
                .companyId(company.getId())
                .transactionDate(LocalDate.now())
                .documentType("PURCHASE_INVOICE")
                .lines(List.of(TaxCalculationLineRequest.builder()
                        .lineId(1L)
                        .amount(BigDecimal.valueOf(1000.00))
                        .taxInclusive(false)
                        .productTaxCategory(product.getCategory().getCode())
                        .build()))
                .build();

        TaxCalculationResult res = taxCalculationEngine.calculateTax(req);
        assertEquals(1, res.getLines().get(0).getTaxComponents().size());

        TaxComponentResult comp = res.getLines().get(0).getTaxComponents().get(0);
        assertTrue(comp.isRecoverable());
        assertEquals(0, BigDecimal.valueOf(150.00).compareTo(comp.getTaxAmount()));
        assertEquals(reverseChargeAccount.getId(), comp.getReverseChargeAccountId());
    }

    @Test
    public void testPreFilingValidations() {
        TaxCalendar calendar = taxCalendarRepository.save(TaxCalendar.builder()
                .company(company)
                .filingType("VAT_RETURN")
                .periodStart(LocalDate.now().minusDays(30))
                .periodEnd(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(20))
                .status("DRAFT")
                .build());

        // 1. Without Company TRN Registration -> Should fail validation
        PreFilingValidationResult validationRes1 = taxFilingService.validateFiling(calendar.getId());
        assertFalse(validationRes1.isValid());
        assertTrue(validationRes1.getErrors().stream().anyMatch(e -> e.contains("Missing company tax registration")));

        // 2. With Company TRN Registration -> Should pass validation
        taxRegistrationRepository.save(TaxRegistration.builder()
                .entityType("COMPANY")
                .entityId(company.getId())
                .taxScheme("TRN")
                .registrationNumber("123456789")
                .effectiveFrom(LocalDate.now().minusDays(10))
                .status("ACTIVE")
                .active(true)
                .build());

        PreFilingValidationResult validationRes2 = taxFilingService.validateFiling(calendar.getId());
        assertTrue(validationRes2.isValid());
        assertFalse(validationRes2.getWarnings().isEmpty()); // Warning about no invoices recorded
    }

    @Test
    public void testFilingLifecycleStateTransitions() {
        TaxCalendar calendar = taxCalendarRepository.save(TaxCalendar.builder()
                .company(company)
                .filingType("VAT_RETURN")
                .periodStart(LocalDate.now().minusDays(30))
                .periodEnd(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(20))
                .status("DRAFT")
                .build());

        taxRegistrationRepository.save(TaxRegistration.builder()
                .entityType("COMPANY")
                .entityId(company.getId())
                .taxScheme("TRN")
                .registrationNumber("123456789")
                .effectiveFrom(LocalDate.now().minusDays(10))
                .status("ACTIVE")
                .active(true)
                .build());

        // Transition: DRAFT -> CALCULATED
        TaxFiling filing = taxFilingService.calculateFiling(calendar.getId(), "admin@plus33.com");
        assertNotNull(filing);
        assertEquals("CALCULATED", calendar.getStatus());

        // Transition: CALCULATED -> SUBMITTED
        TaxFiling submitted = taxFilingService.submitFiling(filing.getId(), "admin@plus33.com");
        assertEquals("SUBMITTED", calendar.getStatus());
        assertNotNull(submitted.getGovernmentReceiptRef());
        assertNotNull(submitted.getSubmissionPayload());
    }

    @Test
    public void testComplianceAdapterAndConnectorStubs() {
        EInvoiceComplianceLog log = eInvoiceComplianceService.submitEInvoice(
                company.getId(), "SALES_INVOICE", 101L, "ZATCA", "<ubl><trn>123456789</trn></ubl>"
        );

        assertNotNull(log);
        assertEquals("ACCEPTED", log.getStatus());
        assertNotNull(log.getSignatureHash());
        assertNotNull(log.getGovernmentUuid());
    }
}
