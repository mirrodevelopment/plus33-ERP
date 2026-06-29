package com.plus33.erp.finance.tax;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.tax.compliance.*;
import com.plus33.erp.finance.tax.dto.*;
import com.plus33.erp.finance.tax.entity.*;
import com.plus33.erp.finance.tax.repository.*;
import com.plus33.erp.finance.tax.service.*;
import com.plus33.erp.finance.tax.queue.ComplianceQueueWorker;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(properties = { "spring.flyway.enabled=false" })
public class TaxManagementIntegrationTest {

        @Autowired
        private org.springframework.test.web.servlet.MockMvc mockMvc;

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
        @Autowired
        private TaxOverrideRequestRepository taxOverrideRequestRepository;
        @Autowired
        private TaxCalculationLogRepository taxCalculationLogRepository;
        @Autowired
        private ComplianceQueueRepository complianceQueueRepository;
        @Autowired
        private TaxConfigurationVersionRepository taxConfigurationVersionRepository;
        @Autowired
        private TaxEngineRegistry taxEngineRegistry;
        @Autowired
        private ComplianceQueueWorker complianceQueueWorker;
        @Autowired
        private TaxMetricsRegistry taxMetricsRegistry;
        @Autowired
        private TaxConfigurationCache taxConfigurationCache;

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
                taxConfigurationCache.invalidate();
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
                                        return productCategoryRepository.save(new ProductCategory(null, "CAT_TAX_TEST",
                                                        "Test Category", null, true, LocalDateTime.now(),
                                                        LocalDateTime.now()));
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
                                .orElseGet(() -> accountRepository.save(Account.builder().company(company)
                                                .accountCode("2200").accountName("Tax Payable").accountType("LIABILITY")
                                                .active(true).build()));

                outputTaxAccount = inputTaxAccount;

                reverseChargeAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2201")
                                .orElseGet(() -> accountRepository.save(Account.builder().company(company)
                                                .accountCode("2201").accountName("Reverse Charge Offset")
                                                .accountType("LIABILITY").active(true).build()));

                recoverableAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1410")
                                .orElseGet(() -> accountRepository.save(Account.builder().company(company)
                                                .accountCode("1410").accountName("Recoverable Tax").accountType("ASSET")
                                                .active(true).build()));

                // Create Tax Categories and Rates
                vatCategory = taxCategoryRepository.findByCode("VAT_15")
                                .orElseGet(() -> taxCategoryRepository.save(TaxCategory.builder().code("VAT_15")
                                                .name("Value Added Tax 15%").active(true).build()));

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
                                        TaxGroup tg = taxGroupRepository.save(TaxGroup.builder().code("VAT_GROUP_15")
                                                        .name("VAT Group 15%").active(true).build());
                                        TaxGroupLine line = taxGroupLineRepository
                                                        .save(TaxGroupLine.builder().group(tg).rate(vatRate).build());
                                        tg.getLines().add(line);
                                        return tg;
                                });

                for (TaxCategory cat : taxCategoryRepository.findAll()) {
                        taxPostingProfileRepository
                                        .findByCompanyIdAndCategoryId(company.getId(), cat.getId())
                                        .orElseGet(() -> taxPostingProfileRepository
                                                        .save(TaxPostingProfile.builder()
                                                                        .company(company)
                                                                        .category(cat)
                                                                        .inputTaxAccount(
                                                                                        inputTaxAccount)
                                                                        .outputTaxAccount(
                                                                                        outputTaxAccount)
                                                                        .recoverableAccount(
                                                                                        recoverableAccount)
                                                                        .reverseChargeAccount(
                                                                                        reverseChargeAccount)
                                                                        .active(true)
                                                                        .build()));
                }
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

                List<TaxRegistration> companyRegs = taxRegistrationRepository
                                .findByEntityTypeAndEntityIdAndActiveTrue("COMPANY", company.getId());
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
                                .ruleName("Exemption Test Rule")
                                .priority(0)
                                .documentType("SALES_INVOICE")
                                .customerTaxProfile("STANDARD")
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

                for (TaxCategory cat : taxCategoryRepository.findAll()) {
                        taxPostingProfileRepository.findByCompanyIdAndCategoryId(company.getId(), cat.getId())
                                        .orElseGet(() -> taxPostingProfileRepository.save(TaxPostingProfile
                                                        .builder()
                                                        .company(company)
                                                        .category(cat)
                                                        .inputTaxAccount(inputTaxAccount)
                                                        .outputTaxAccount(outputTaxAccount)
                                                        .recoverableAccount(recoverableAccount)
                                                        .reverseChargeAccount(reverseChargeAccount)
                                                        .active(true)
                                                        .build()));
                }

                TaxCalculationResult res = taxCalculationEngine.calculateTax(req);
                assertEquals(0, BigDecimal.valueOf(100.00).compareTo(res.getTotalNetAmount()));
                assertEquals(0, BigDecimal.ZERO.compareTo(res.getTotalTaxAmount())); // ZERO tax due to exemption
                                                                                     // certificate
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
                TaxCategory zeroCategory = taxCategoryRepository
                                .save(TaxCategory.builder().code("VAT_0").name("Zero Rated").active(true).build());
                TaxRate zeroRate = taxRateRepository
                                .save(TaxRate.builder().category(zeroCategory).ratePercent(BigDecimal.ZERO)
                                                .effectiveFrom(LocalDate.now().minusDays(10)).active(true).build());
                taxPostingProfileRepository.save(TaxPostingProfile.builder().company(company).category(zeroCategory)
                                .inputTaxAccount(inputTaxAccount).outputTaxAccount(outputTaxAccount).active(true)
                                .build());
                TaxGroup zeroGroup = taxGroupRepository
                                .save(TaxGroup.builder().code("VAT_GROUP_0").name("Zero Group").active(true).build());
                TaxGroupLine zeroLine = taxGroupLineRepository
                                .save(TaxGroupLine.builder().group(zeroGroup).rate(zeroRate).build());
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
                                null, null, null, null, null, LocalDate.now());
                assertEquals("VAT_GROUP_15", resolvedStandard.getCode());

                // Resolve for EXEMPT profile -> high priority group VAT_GROUP_0 (0% rate)
                TaxGroup resolvedExempt = taxDeterminationRuleEngine.determineTaxGroup(
                                company.getId(), "SALES_INVOICE", "EXEMPT", null, product.getCategory().getCode(),
                                null, null, null, null, null, LocalDate.now());
                assertEquals("VAT_GROUP_0", resolvedExempt.getCode());
        }

        @Test
        public void testReverseChargeAccountingOffset() {
                // Setup a reverse charge posting profile
                TaxCategory rcCategory = taxCategoryRepository.save(
                                TaxCategory.builder().code("VAT_RC").name("Reverse Charge VAT").active(true).build());
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

                TaxGroup rcGroup = taxGroupRepository.save(TaxGroup.builder().code("VAT_GROUP_RC")
                                .name("Reverse Charge VAT Group").active(true).build());
                TaxGroupLine rcLine = taxGroupLineRepository
                                .save(TaxGroupLine.builder().group(rcGroup).rate(rcRate).build());
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
                assertTrue(validationRes1.getErrors().stream()
                                .anyMatch(e -> e.contains("Missing company tax registration")));

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
                                company.getId(), "SALES_INVOICE", 101L, "ZATCA", "<ubl><trn>123456789</trn></ubl>");

                assertNotNull(log);
                assertEquals("ACCEPTED", log.getStatus());
                assertNotNull(log.getSignatureHash());
                assertNotNull(log.getGovernmentUuid());
        }

        @Test
        public void testTaxEngineRegistryResolution() {
                TaxEngineProvider vatProvider = taxEngineRegistry.resolve("VAT");
                assertNotNull(vatProvider);
                assertEquals("VAT", vatProvider.getTaxType());

                TaxEngineProvider gstProvider = taxEngineRegistry.resolve("GST");
                assertNotNull(gstProvider);
                assertEquals("GST", gstProvider.getTaxType());

                TaxEngineProvider salesProvider = taxEngineRegistry.resolve("SALES_TAX");
                assertNotNull(salesProvider);
                assertEquals("SALES_TAX", salesProvider.getTaxType());

                // Test fallback to VAT
                TaxEngineProvider defaultProvider = taxEngineRegistry.resolve("UNKNOWN_SCHEME");
                assertNotNull(defaultProvider);
                assertEquals("VAT", defaultProvider.getTaxType());
        }

        @Test
        public void testGstTaxEngineProviderIntraStateVsInterState() {
                // Prepare GST Tax Group
                TaxCategory gstCategory = taxCategoryRepository.save(TaxCategory.builder()
                                .code("GST_18")
                                .name("Goods and Services Tax 18%")
                                .active(true)
                                .build());

                taxPostingProfileRepository.save(TaxPostingProfile.builder()
                                .company(company)
                                .category(gstCategory)
                                .inputTaxAccount(inputTaxAccount)
                                .outputTaxAccount(outputTaxAccount)
                                .recoverableAccount(recoverableAccount)
                                .reverseChargeAccount(reverseChargeAccount)
                                .active(true)
                                .build());

                TaxRate gstRate = taxRateRepository.save(TaxRate.builder()
                                .category(gstCategory)
                                .ratePercent(BigDecimal.valueOf(18.00))
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                TaxGroup gstGroup = taxGroupRepository.save(TaxGroup.builder()
                                .code("GST_GROUP")
                                .name("GST Group 18%")
                                .taxType("GST")
                                .active(true)
                                .build());

                TaxGroupLine line = taxGroupLineRepository
                                .save(TaxGroupLine.builder().group(gstGroup).rate(gstRate).build());
                gstGroup.getLines().add(line);

                // Register determination rule
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("GST Rule")
                                .priority(1)
                                .documentType("SALES_INVOICE")
                                .productTaxCategory("GST_CAT")
                                .taxGroup(gstGroup)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                // 1. Intra-state calculation (originState == destState) -> Splitting into CGST
                // and SGST
                TaxCalculationRequest intraReq = TaxCalculationRequest.builder()
                                .companyId(company.getId())
                                .transactionDate(LocalDate.now())
                                .documentType("SALES_INVOICE")
                                .originState("KA")
                                .destState("KA")
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .taxInclusive(false)
                                                .productTaxCategory("GST_CAT")
                                                .build()))
                                .build();

                TaxCalculationResult intraRes = taxCalculationEngine.calculateTax(intraReq);
                assertEquals(0, BigDecimal.valueOf(18.00).compareTo(intraRes.getTotalTaxAmount()));
                assertEquals(2, intraRes.getLines().get(0).getTaxComponents().size());

                boolean hasCgst = false;
                boolean hasSgst = false;
                for (TaxComponentResult comp : intraRes.getLines().get(0).getTaxComponents()) {
                        if ("CGST".equals(comp.getTaxCategoryCode())) {
                                hasCgst = true;
                                assertEquals(0, BigDecimal.valueOf(9.00).compareTo(comp.getTaxAmount()));
                        } else if ("SGST".equals(comp.getTaxCategoryCode())) {
                                hasSgst = true;
                                assertEquals(0, BigDecimal.valueOf(9.00).compareTo(comp.getTaxAmount()));
                        }
                }
                assertTrue(hasCgst && hasSgst);

                // 2. Inter-state calculation (originState != destState) -> Single IGST
                TaxCalculationRequest interReq = TaxCalculationRequest.builder()
                                .companyId(company.getId())
                                .transactionDate(LocalDate.now())
                                .documentType("SALES_INVOICE")
                                .originState("KA")
                                .destState("MH")
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .taxInclusive(false)
                                                .productTaxCategory("GST_CAT")
                                                .build()))
                                .build();

                TaxCalculationResult interRes = taxCalculationEngine.calculateTax(interReq);
                assertEquals(0, BigDecimal.valueOf(18.00).compareTo(interRes.getTotalTaxAmount()));
                assertEquals(1, interRes.getLines().get(0).getTaxComponents().size());
                TaxComponentResult igstComp = interRes.getLines().get(0).getTaxComponents().get(0);
                assertEquals("IGST", igstComp.getTaxCategoryCode());
                assertEquals(0, BigDecimal.valueOf(18.00).compareTo(igstComp.getTaxAmount()));
        }

        @Test
        public void testSalesTaxEngineProvider() {
                TaxCategory salesCategory = taxCategoryRepository.save(TaxCategory.builder()
                                .code("SALES_8")
                                .name("US Sales Tax 8%")
                                .active(true)
                                .build());

                taxPostingProfileRepository.save(TaxPostingProfile.builder()
                                .company(company)
                                .category(salesCategory)
                                .inputTaxAccount(inputTaxAccount)
                                .outputTaxAccount(outputTaxAccount)
                                .active(true)
                                .build());

                TaxRate salesRate = taxRateRepository.save(TaxRate.builder()
                                .category(salesCategory)
                                .ratePercent(BigDecimal.valueOf(8.00))
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                TaxGroup salesGroup = taxGroupRepository.save(TaxGroup.builder()
                                .code("SALES_GROUP")
                                .name("US Sales Group 8%")
                                .taxType("SALES_TAX")
                                .active(true)
                                .build());

                TaxGroupLine line = taxGroupLineRepository
                                .save(TaxGroupLine.builder().group(salesGroup).rate(salesRate).build());
                salesGroup.getLines().add(line);

                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Sales Tax Rule")
                                .priority(1)
                                .documentType("SALES_INVOICE")
                                .productTaxCategory("SALES_CAT")
                                .taxGroup(salesGroup)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                TaxCalculationRequest req = TaxCalculationRequest.builder()
                                .companyId(company.getId())
                                .transactionDate(LocalDate.now())
                                .documentType("SALES_INVOICE")
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .taxInclusive(false)
                                                .productTaxCategory("SALES_CAT")
                                                .build()))
                                .build();

                TaxCalculationResult res = taxCalculationEngine.calculateTax(req);
                assertEquals(0, BigDecimal.valueOf(8.00).compareTo(res.getTotalTaxAmount()));
                assertEquals(0, BigDecimal.valueOf(108.00).compareTo(res.getTotalGrossAmount()));
                assertFalse(res.getLines().get(0).getTaxComponents().get(0).isRecoverable());
        }

        @Test
        public void testSpecificitySortingPrecedence() {
                // We set up rules with different specificity tiers and priority
                // Default Rule
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Specificity Tier 4 Default")
                                .priority(100)
                                .documentType("SALES_INVOICE")
                                .taxGroup(vatGroup)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                // Rule with place of supply (Tier 3)
                TaxGroup tier3Group = taxGroupRepository
                                .save(TaxGroup.builder().code("T3_GROUP").name("Tier 3 Group").active(true).build());
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Specificity Tier 3 Match")
                                .priority(50)
                                .documentType("SALES_INVOICE")
                                .destCountry("US")
                                .taxGroup(tier3Group)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                // Rule with customer profile match (Tier 1 - Highest)
                TaxGroup tier1Group = taxGroupRepository
                                .save(TaxGroup.builder().code("T1_GROUP").name("Tier 1 Group").active(true).build());
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Specificity Tier 1 Match")
                                .priority(1000) // Lower numeric priority, but higher specificity!
                                .documentType("SALES_INVOICE")
                                .customerTaxProfile("PREMIUM")
                                .taxGroup(tier1Group)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                // Query with premium profile and destination US.
                // It matches Tier 1 premium profile rule, Tier 3 US destination rule, and Tier
                // 4 default rule.
                // It must resolve to Tier 1 group (T1_GROUP) despite having the lowest
                // configured priority (1000).
                TaxGroup resolved = taxDeterminationRuleEngine.determineTaxGroup(
                                company.getId(), "SALES_INVOICE", "PREMIUM", null, product.getCategory().getCode(),
                                null, null, "US", null, null, LocalDate.now());

                assertEquals("T1_GROUP", resolved.getCode());
        }

        @Test
        public void testApprovedOverridePrecedenceAndAuditTrail() {
                Long docId = 999L;

                // Clear log repository prior to test
                taxCalculationLogRepository.deleteAll();

                // Save approved override request
                taxOverrideRequestRepository.save(TaxOverrideRequest.builder()
                                .company(company)
                                .documentType("SALES_INVOICE")
                                .documentId(docId)
                                .originalTaxAmount(BigDecimal.valueOf(15.00))
                                .requestedTaxAmount(BigDecimal.valueOf(5.00))
                                .reason("Customer disputed standard tax")
                                .status("APPROVED")
                                .build());

                // Request calculation with documentId populated
                TaxCalculationRequest req = TaxCalculationRequest.builder()
                                .companyId(company.getId())
                                .transactionDate(LocalDate.now())
                                .documentType("SALES_INVOICE")
                                .documentId(docId)
                                .customerId(customer.getId())
                                .lines(List.of(
                                                TaxCalculationLineRequest.builder()
                                                                .lineId(1L)
                                                                .amount(BigDecimal.valueOf(100.00))
                                                                .productTaxCategory(product.getCategory().getCode())
                                                                .build()))
                                .build();

                TaxCalculationResult result = taxCalculationEngine.calculateTax(req);

                // Assert override applied successfully
                assertTrue(result.isOverrideApplied());
                assertEquals("Customer disputed standard tax", result.getOverrideReason());
                assertEquals(0, BigDecimal.valueOf(5.00).compareTo(result.getTotalTaxAmount()));

                // Assert audit trail persisted successfully
                List<TaxCalculationLog> logs = taxCalculationLogRepository.findAll();
                assertEquals(1, logs.size());
                TaxCalculationLog log = logs.get(0);
                assertTrue(log.isOverrideApplied());
                assertEquals(0, BigDecimal.valueOf(5.00).compareTo(log.getCalculatedTaxAmount()));
                assertEquals("SALES_INVOICE", log.getDocumentType());
                assertEquals(docId, log.getDocumentId());
        }

        @Test
        public void testConfigurationVersioningDynamicResolution() {
                // Save version 3 for company
                TaxConfigurationVersion v3 = taxConfigurationVersionRepository.save(TaxConfigurationVersion.builder()
                                .company(company)
                                .versionNumber(3)
                                .effectiveFrom(LocalDateTime.now().minusDays(2))
                                .effectiveTo(LocalDateTime.now().plusDays(2))
                                .publishedBy("QA")
                                .description("Config Version 3")
                                .active(true)
                                .build());

                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Fallback Version Test")
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
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .productTaxCategory(product.getCategory().getCode())
                                                .build()))
                                .build();

                TaxCalculationResult res = taxCalculationEngine.calculateTax(req);
                assertEquals(3, res.getConfigurationVersion());
        }

        @Test
        public void testComplianceQueueWorkerProcessingAndRetries() {
                complianceQueueRepository.deleteAll();

                // 1. Create a queue item targeting ZATCA compliance provider
                ComplianceQueueItem item = complianceQueueRepository.save(ComplianceQueueItem.builder()
                                .companyId(company.getId())
                                .documentType("SALES_INVOICE")
                                .documentId(500L)
                                .providerType("ZATCA")
                                .payload("<invoice><trn>123456789</trn></invoice>")
                                .status("PENDING")
                                .retryCount(0)
                                .maxRetries(3)
                                .build());

                // Process queue
                complianceQueueWorker.processQueue();

                // Since the ZATCA mock provider succeeds (it accepts anything in the
                // stub/compliance system), it should be COMPLETED
                ComplianceQueueItem processed = complianceQueueRepository.findById(item.getId()).orElseThrow();
                assertEquals("COMPLETED", processed.getStatus());
                assertNull(processed.getLastError());

                // 2. Create a queue item with unsupported provider to trigger FAILED retry
                // increment
                ComplianceQueueItem failItem = complianceQueueRepository.save(ComplianceQueueItem.builder()
                                .companyId(company.getId())
                                .documentType("SALES_INVOICE")
                                .documentId(501L)
                                .providerType("NON_EXISTENT_PORTAL")
                                .payload("<invoice>test</invoice>")
                                .status("PENDING")
                                .retryCount(0)
                                .maxRetries(2)
                                .build());

                // Run worker once -> should increment retry to 1 and mark as FAILED
                complianceQueueWorker.processQueue();
                ComplianceQueueItem failed = complianceQueueRepository.findById(failItem.getId()).orElseThrow();
                assertEquals("FAILED", failed.getStatus());
                assertEquals(1, failed.getRetryCount());
                assertNotNull(failed.getLastError());

                // Run worker twice -> retry count to 2, status remains FAILED
                complianceQueueWorker.processQueue();
                failed = complianceQueueRepository.findById(failItem.getId()).orElseThrow();
                assertEquals("FAILED", failed.getStatus());
                assertEquals(2, failed.getRetryCount());

                // Run worker third time -> retry count is now equal to maxRetries (2), should
                // transition to DEAD_LETTER
                complianceQueueWorker.processQueue();
                failed = complianceQueueRepository.findById(failItem.getId()).orElseThrow();
                assertEquals("DEAD_LETTER", failed.getStatus());
                assertEquals(2, failed.getRetryCount());
        }

        @Test
        public void testTaxMetricsRegistryCollection() {
                taxMetricsRegistry.resetMetrics();
                taxConfigurationCache.invalidate();

                // Create determination rule required for calculation
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Metrics Test Fallback Rule")
                                .priority(100)
                                .documentType("SALES_INVOICE")
                                .taxGroup(vatGroup)
                                .effectiveFrom(LocalDate.now().minusDays(10))
                                .active(true)
                                .build());

                // Perform calculation
                TaxCalculationRequest req = TaxCalculationRequest.builder()
                                .companyId(company.getId())
                                .transactionDate(LocalDate.now())
                                .documentType("SALES_INVOICE")
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .productTaxCategory(product.getCategory().getCode())
                                                .build()))
                                .build();

                taxCalculationEngine.calculateTax(req);

                Map<String, Object> diagnostics = taxMetricsRegistry.getDiagnostics();
                assertNotNull(diagnostics);
                assertEquals(1L, diagnostics.get("totalRequests"));
                assertTrue((Long) diagnostics.get("totalDurationMs") >= 0);
                assertTrue((Long) diagnostics.get("maxDurationMs") >= 0);

                Map<String, Long> providers = (Map<String, Long>) diagnostics.get("providerUsage");
                assertEquals(1L, providers.get("VAT"));
        }

        @Test
        public void testTaxConfigurationCacheHitsAndMisses() {
                taxMetricsRegistry.resetMetrics();
                taxConfigurationCache.invalidate();

                // Create determination rule required for calculation
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Cache Test Fallback Rule")
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
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .productTaxCategory(product.getCategory().getCode())
                                                .build()))
                                .build();

                // 1st calculation -> Cache misses
                taxCalculationEngine.calculateTax(req);
                Map<String, Object> stats1 = (Map<String, Object>) taxMetricsRegistry.getDiagnostics()
                                .get("cacheStatistics");
                assertEquals(1L, stats1.get("ruleMisses"));
                assertEquals(0L, stats1.get("ruleHits"));
                assertEquals(1L, stats1.get("rateMisses"));
                assertEquals(0L, stats1.get("rateHits"));

                // 2nd calculation -> Cache hits
                taxCalculationEngine.calculateTax(req);
                Map<String, Object> stats2 = (Map<String, Object>) taxMetricsRegistry.getDiagnostics()
                                .get("cacheStatistics");
                assertEquals(1L, stats2.get("ruleMisses"));
                assertEquals(1L, stats2.get("ruleHits"));
                assertEquals(1L, stats2.get("rateMisses"));
                assertEquals(1L, stats2.get("rateHits"));
        }

        @Test
        public void testTaxConfigurationCacheEviction() {
                taxMetricsRegistry.resetMetrics();
                taxConfigurationCache.invalidate();

                // Create determination rule required for calculation
                taxDeterminationRuleRepository.save(TaxDeterminationRule.builder()
                                .company(company)
                                .ruleName("Eviction Test Fallback Rule")
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
                                .lines(List.of(TaxCalculationLineRequest.builder()
                                                .lineId(1L)
                                                .amount(BigDecimal.valueOf(100.00))
                                                .productTaxCategory(product.getCategory().getCode())
                                                .build()))
                                .build();

                // Calculate once to populate cache
                taxCalculationEngine.calculateTax(req);
                Map<String, Object> stats1 = (Map<String, Object>) taxMetricsRegistry.getDiagnostics()
                                .get("cacheStatistics");
                assertEquals(1L, stats1.get("ruleMisses"));
                assertEquals(1L, stats1.get("rateMisses"));

                // Save new TaxConfigurationVersion -> evicts cache via aspect
                taxConfigurationVersionRepository.save(TaxConfigurationVersion.builder()
                                .company(company)
                                .versionNumber(99)
                                .effectiveFrom(LocalDateTime.now().minusDays(5))
                                .publishedBy("Test runner")
                                .description("Version 99")
                                .active(true)
                                .build());

                // Calculate again -> should result in cache misses again!
                taxCalculationEngine.calculateTax(req);
                Map<String, Object> stats2 = (Map<String, Object>) taxMetricsRegistry.getDiagnostics()
                                .get("cacheStatistics");
                assertEquals(2L, stats2.get("ruleMisses"));
                assertEquals(2L, stats2.get("rateMisses"));
        }

        @Test
        public void testTaxDiagnosticsEndpoint() throws Exception {
                // Test endpoint with authorized user (TAX_VIEW authority)
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/v1/tax/diagnostics/metrics")
                                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                                .user("tax_manager").authorities(
                                                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                                                "TAX_VIEW"))))
                                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status()
                                                .isOk())
                                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                                .jsonPath("$.totalRequests").exists())
                                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                                .jsonPath("$.cacheStatistics").exists());

                // Test endpoint with unauthorized user (no authority)
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/v1/tax/diagnostics/metrics")
                                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                                .user("regular_user").authorities(
                                                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                                                "EMPLOYEE_VIEW"))))
                                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status()
                                                .isForbidden());
        }
}
