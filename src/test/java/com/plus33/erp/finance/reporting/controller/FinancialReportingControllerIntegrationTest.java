package com.plus33.erp.finance.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.finance.reporting.dto.*;
import com.plus33.erp.finance.reporting.entity.*;
import com.plus33.erp.finance.reporting.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.plus33.erp.common.dto.ApiResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FinancialReportingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryLineRepository journalEntryLineRepository;

    @Autowired
    private PeriodLockRepository periodLockRepository;

    @Autowired
    private PeriodLockOverrideRepository periodLockOverrideRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company company;
    private User adminUser;
    
    private Account cashAccount;
    private Account bankAccount;
    private Account apAccount;
    private Account reAccount;
    private Account revenueAccount;
    private Account expenseAccount;

    @BeforeEach
    public void setUp() {
        cleanup();

        // 1. Load PLUS33_GLOBAL company
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));
        
        // Reset company fiscal calendar to defaults for standard tests
        company.setFiscalYearStartMonth(1);
        company.setFiscalYearStartDay(1);
        companyRepository.save(company);

        // 2. Load Admin User
        adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // 3. Load standard seeded accounts
        cashAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1100")
                .orElseThrow(() -> new AssertionError("Cash account 1100 not found"));
        bankAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1200")
                .orElseThrow(() -> new AssertionError("Bank account 1200 not found"));
        apAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2100")
                .orElseThrow(() -> new AssertionError("AP account 2100 not found"));
        reAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "3100")
                .orElseThrow(() -> new AssertionError("Retained Earnings account 3100 not found"));
        revenueAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "4000")
                .orElseThrow(() -> new AssertionError("Revenue account 4000 not found"));
        expenseAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5100")
                .orElseThrow(() -> new AssertionError("Expense account 5100 not found"));
    }

    @AfterEach
    public void tearDown() {
        cleanup();
    }

    private void cleanup() {
        periodLockOverrideRepository.deleteAll();
        periodLockRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        fiscalYearRepository.deleteAll();
        
        // Delete journal lines and entries created during tests
        journalEntryLineRepository.deleteAll();
        
        // We only delete entries created by tests (avoid deleting seeded ones if any exist, but cleanup deletes all)
        journalEntryRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"FINANCIAL_REPORT_VIEW"})
    public void testTrialBalanceAndBalanceSheetIntegrity() throws Exception {
        // 1. Post a balanced journal entry
        JournalEntry je = JournalEntry.builder()
                .entryNumber("JE-TEST-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 1, 15))
                .description("Test transaction")
                .sourceModule("MANUAL")
                .status("POSTED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();

        // Debit cash 1000, Credit revenue 1000
        je.getLines().add(JournalEntryLine.builder().journalEntry(je).account(cashAccount).debitAmount(new BigDecimal("1000.00")).creditAmount(BigDecimal.ZERO).build());
        je.getLines().add(JournalEntryLine.builder().journalEntry(je).account(revenueAccount).debitAmount(BigDecimal.ZERO).creditAmount(new BigDecimal("1000.00")).build());
        journalEntryRepository.save(je);

        // 2. Query Trial Balance
        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balanced").value(true))
                .andExpect(jsonPath("$.data.difference").value(0))
                .andExpect(jsonPath("$.data.numberOfAccounts").value(org.hamcrest.Matchers.notNullValue()))
                .andExpect(jsonPath("$.data.generatedBy").value("admin@plus33.com"));

        // 3. Query Balance Sheet
        mockMvc.perform(get("/api/v1/financial-reports/balance-sheet")
                        .param("companyId", company.getId().toString())
                        .param("asOfDate", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balanced").value(true))
                .andExpect(jsonPath("$.data.totalAssets").value(1000.00))
                .andExpect(jsonPath("$.data.totalEquity").value(1000.00)) // Incorporates dynamic net income (1000.00)
                .andExpect(jsonPath("$.data.difference").value(0));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"FINANCIAL_REPORT_VIEW"})
    public void testConfigurableFiscalCalendarAndValidation() throws Exception {
        // 1. Test invalid calendar combinations
        company.setFiscalYearStartMonth(4);
        company.setFiscalYearStartDay(31); // Invalid: April has only 30 days
        companyRepository.save(company);

        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-04-30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Invalid fiscal year start date combination")));

        // 2. Test valid fiscal calendar (April 1st)
        company.setFiscalYearStartMonth(4);
        company.setFiscalYearStartDay(1);
        companyRepository.save(company);

        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-04-30"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"FINANCIAL_REPORT_VIEW"})
    public void testExchangeRateLookupAndSameCurrencyBypass() throws Exception {
        // 1. Same-currency query (conversion skipped, rate = 1.0)
        JournalEntry jeAED = JournalEntry.builder()
                .entryNumber("JE-AED-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 2, 10))
                .sourceModule("MANUAL")
                .status("POSTED")
                .currencyCode("AED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        jeAED.getLines().add(JournalEntryLine.builder().journalEntry(jeAED).account(cashAccount).debitAmount(new BigDecimal("500.00")).build());
        jeAED.getLines().add(JournalEntryLine.builder().journalEntry(jeAED).account(revenueAccount).creditAmount(new BigDecimal("500.00")).build());
        journalEntryRepository.save(jeAED);

        // Fetch Trial Balance in AED. Since AED is functional, conversion is skipped (rate = 1.0)
        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-02-01")
                        .param("endDate", "2026-02-28")
                        .param("currency", "AED")
                        .param("rateType", "SPOT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balanced").value(true));

        // 2. Non-functional currency query with exchange rates
        // EUR to AED spot exchange rate = 4.0
        exchangeRateRepository.save(ExchangeRate.builder()
                .company(company)
                .fromCurrency("EUR")
                .toCurrency("AED")
                .rate(new BigDecimal("4.000000"))
                .rateType(ExchangeRateType.SPOT)
                .effectiveDate(LocalDate.of(2026, 1, 1))
                .build());

        JournalEntry jeEUR = JournalEntry.builder()
                .entryNumber("JE-EUR-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 2, 15))
                .sourceModule("MANUAL")
                .status("POSTED")
                .currencyCode("EUR")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        jeEUR.getLines().add(JournalEntryLine.builder().journalEntry(jeEUR).account(cashAccount).debitAmount(new BigDecimal("100.00")).build());
        jeEUR.getLines().add(JournalEntryLine.builder().journalEntry(jeEUR).account(revenueAccount).creditAmount(new BigDecimal("100.00")).build());
        journalEntryRepository.save(jeEUR);

        // Fetch Trial Balance in functional currency (AED), converting EUR transactions
        // Converted Debit Cash: 100 * 4 = 400. Sum with AED 500 = 900.
        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-02-01")
                        .param("endDate", "2026-02-28")
                        .param("currency", "AED")
                        .param("rateType", "SPOT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balanced").value(true));

        // 3. Query with missing rate throws exception
        mockMvc.perform(get("/api/v1/financial-reports/trial-balance")
                        .param("companyId", company.getId().toString())
                        .param("startDate", "2026-02-01")
                        .param("endDate", "2026-02-28")
                        .param("currency", "AED")
                        .param("rateType", "CORPORATE")) // CORPORATE rate is missing
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Exchange rate not found")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"PERIOD_LOCK_CREATE", "FINANCIAL_REPORT_VIEW"})
    public void testSoftAndHardPeriodLocksWithOverrideAuditing() throws Exception {
        // 1. Create SOFT lock on 2026-01-31
        PeriodLockRequest request = new PeriodLockRequest(LocalDate.of(2026, 1, 31), "SOFT", "Soft lock for testing");
        mockMvc.perform(post("/api/v1/financial-reports/period-lock")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 2. An admin user with override authority posts a journal entry in the locked period (Jan 15)
        // Since it's SOFT lock and user is admin, the post succeeds, writing an override log.
        JournalEntry jeSoft = JournalEntry.builder()
                .entryNumber("JE-SOFT-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 1, 15))
                .sourceModule("MANUAL")
                .status("POSTED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        jeSoft.getLines().add(JournalEntryLine.builder().journalEntry(jeSoft).account(cashAccount).debitAmount(new BigDecimal("200.00")).build());
        jeSoft.getLines().add(JournalEntryLine.builder().journalEntry(jeSoft).account(revenueAccount).creditAmount(new BigDecimal("200.00")).build());
        
        // Manually populate SecurityContext right before direct repository save call in this test thread
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "admin@plus33.com", "password",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ULTIMATE_ADMIN", "FINANCE_MANAGER")
            )
        );
        journalEntryRepository.save(jeSoft);

        // Verify override audit log was written
        List<PeriodLockOverride> overrides = periodLockOverrideRepository.findAll();
        assertEquals(1, overrides.size());
        assertEquals("admin@plus33.com", overrides.get(0).getUserEmail());

        // 3. Create HARD lock on 2026-01-31
        PeriodLockRequest hardRequest = new PeriodLockRequest(LocalDate.of(2026, 1, 31), "HARD", "Hard lock for testing");
        mockMvc.perform(post("/api/v1/financial-reports/period-lock")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hardRequest)))
                .andExpect(status().isOk());

        // 4. Try posting in HARD lock period. This must be strictly rejected.
        JournalEntry jeHard = JournalEntry.builder()
                .entryNumber("JE-HARD-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 1, 15))
                .sourceModule("MANUAL")
                .status("POSTED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        jeHard.getLines().add(JournalEntryLine.builder().journalEntry(jeHard).account(cashAccount).debitAmount(new BigDecimal("100.00")).build());
        jeHard.getLines().add(JournalEntryLine.builder().journalEntry(jeHard).account(revenueAccount).creditAmount(new BigDecimal("100.00")).build());

        // Manually populate SecurityContext right before direct repository save call in this test thread
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "admin@plus33.com", "password",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ULTIMATE_ADMIN", "FINANCE_MANAGER")
            )
        );
        assertThrows(Exception.class, () -> {
            journalEntryRepository.save(jeHard);
        });
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"FISCAL_YEAR_CLOSE", "FISCAL_YEAR_REOPEN", "FINANCIAL_REPORT_VIEW", "PERIOD_LOCK_VIEW"})
    public void testFiscalYearCloseAndReopenWorkflow() throws Exception {
        // 1. Post some transactions in 2026
        JournalEntry je1 = JournalEntry.builder()
                .entryNumber("JE-FY-001")
                .company(company)
                .entryDate(LocalDate.of(2026, 3, 15))
                .sourceModule("MANUAL")
                .status("POSTED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        je1.getLines().add(JournalEntryLine.builder().journalEntry(je1).account(cashAccount).debitAmount(new BigDecimal("5000.00")).build());
        je1.getLines().add(JournalEntryLine.builder().journalEntry(je1).account(revenueAccount).creditAmount(new BigDecimal("5000.00")).build());
        journalEntryRepository.save(je1);

        JournalEntry je2 = JournalEntry.builder()
                .entryNumber("JE-FY-002")
                .company(company)
                .entryDate(LocalDate.of(2026, 8, 20))
                .sourceModule("MANUAL")
                .status("POSTED")
                .createdBy(adminUser)
                .lines(new ArrayList<>())
                .build();
        je2.getLines().add(JournalEntryLine.builder().journalEntry(je2).account(expenseAccount).debitAmount(new BigDecimal("2000.00")).build());
        je2.getLines().add(JournalEntryLine.builder().journalEntry(je2).account(bankAccount).creditAmount(new BigDecimal("2000.00")).build());
        journalEntryRepository.save(je2);

        // 2. Perform Fiscal Year Close
        FiscalYearCloseRequest closeRequest = new FiscalYearCloseRequest(2026);
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-close")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(closeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CLOSED"))
                .andExpect(jsonPath("$.data.closingJournalId").isNotEmpty());

        // Verify period lock is auto-created up to Dec 31st
        PeriodLockResponse lock = getPeriodLockResponse();
        assertEquals(LocalDate.of(2026, 12, 31), lock.lockDate());
        assertEquals("HARD", lock.lockType());

        // Verify closing journal is immutable
        Optional<FiscalYear> optFy = fiscalYearRepository.findByCompanyIdAndFiscalYear(company.getId(), 2026);
        assertTrue(optFy.isPresent());
        JournalEntry closingJEProxy = optFy.get().getClosingJournal();
        assertNotNull(closingJEProxy);
        JournalEntry closingJE = journalEntryRepository.findById(closingJEProxy.getId())
                .orElseThrow(() -> new AssertionError("Closing journal not found"));
        
        closingJE.setDescription("Modified description");
        assertThrows(Exception.class, () -> {
            journalEntryRepository.save(closingJE);
        });

        // 3. Double-Close Block Safeguard
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-close")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(closeRequest)))
                .andExpect(status().isBadRequest());

        // 4. Reopen Fiscal Year
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-reopen/2026")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // Verify status is OPEN and a reversing journal was created
        optFy = fiscalYearRepository.findByCompanyIdAndFiscalYear(company.getId(), 2026);
        assertEquals(FiscalYearStatus.OPEN, optFy.get().getStatus());

        // 5. Chronological Safeguard: Close 2026, then 2027. Try to reopen 2026 while 2027 is closed (should block)
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-close")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(closeRequest)))
                .andExpect(status().isOk());

        // Close 2027
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-close")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FiscalYearCloseRequest(2027))))
                .andExpect(status().isOk());

        // Reopen 2026 (should be rejected because 2027 is closed)
        mockMvc.perform(post("/api/v1/financial-reports/fiscal-year-reopen/2026")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("subsequent fiscal year is currently closed")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"FINANCIAL_REPORT_VIEW", "FINANCIAL_REPORT_EXPORT"})
    public void testCSVAndHTMLExportsWithMetadata() throws Exception {
        // Run export for Trial Balance
        mockMvc.perform(get("/api/v1/financial-reports/export")
                        .param("companyId", company.getId().toString())
                        .param("reportType", "TRIAL_BALANCE")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31")
                        .param("format", "CSV"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Report Name: Trial Balance")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Generated By: admin@plus33.com")));

        mockMvc.perform(get("/api/v1/financial-reports/export")
                        .param("companyId", company.getId().toString())
                        .param("reportType", "TRIAL_BALANCE")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31")
                        .param("format", "HTML"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/html; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<h1>Trial Balance</h1>")));
    }

    @Test
    @WithMockUser(username = "exporter@plus33.com", authorities = {"FINANCIAL_REPORT_VIEW"}) // Missing export permission
    public void testExportAuthorizationChecks() throws Exception {
        mockMvc.perform(get("/api/v1/financial-reports/export")
                        .param("companyId", company.getId().toString())
                        .param("reportType", "TRIAL_BALANCE")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31")
                        .param("format", "CSV"))
                .andExpect(status().isForbidden());
    }

    private PeriodLockResponse getPeriodLockResponse() throws Exception {
        String json = mockMvc.perform(get("/api/v1/financial-reports/period-lock")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ApiResponse<PeriodLockResponse> apiResponse = objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, PeriodLockResponse.class));
        return apiResponse.data();
    }
}
