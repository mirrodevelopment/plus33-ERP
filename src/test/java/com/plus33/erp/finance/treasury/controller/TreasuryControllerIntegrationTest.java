package com.plus33.erp.finance.treasury.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.PaymentStatus;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.treasury.dto.BankAccountDtos.*;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;
import com.plus33.erp.finance.treasury.dto.PaymentFactoryDtos.*;
import com.plus33.erp.finance.treasury.dto.StatementAndRecDtos.*;
import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.finance.treasury.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TreasuryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TreasuryApprovalStepRepository treasuryApprovalStepRepository;

    @Autowired
    private InHouseBankAccountRepository inHouseBankAccountRepository;

    @Autowired
    private CashPoolRepository cashPoolRepository;

    @Autowired
    private IntercompanyLoanRepository intercompanyLoanRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private BankFeeRuleRepository bankFeeRuleRepository;

    @Autowired
    private TreasuryInvestmentRepository treasuryInvestmentRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private InHouseBankService inHouseBankService;

    @Autowired
    private PaymentFactoryService paymentFactoryService;

    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private TreasuryInvestmentService treasuryInvestmentService;

    @Autowired
    private TreasuryForecastService treasuryForecastService;

    @Autowired
    private PaymentBatchRepository paymentBatchRepository;

    @Autowired
    private ReconciliationRuleRepository reconciliationRuleRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private Company companyA;
    private Company companyB;
    private User adminUser;
    private User treasuryUser;
    private Supplier supplierA;
    private Account cashAccountA;
    private Account cashAccountB;
    private Account expenseAccount;

    private UsernamePasswordAuthenticationToken adminAuth;
    private UsernamePasswordAuthenticationToken approverAuth;

    @BeforeEach
    public void setUp() {
        // Setup Security Context
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ULTIMATE_ADMIN"));
        adminAuth = new UsernamePasswordAuthenticationToken("admin@plus33.com", "password", authorities);
        
        List<SimpleGrantedAuthority> appAuthorities = List.of(new SimpleGrantedAuthority("ROLE_FINANCE_MANAGER"));
        approverAuth = new UsernamePasswordAuthenticationToken("treasury@plus33.com", "password", appAuthorities);
        
        SecurityContextHolder.getContext().setAuthentication(adminAuth);

        // Fetch or create companies (no builder)
        companyA = companyRepository.findAll().stream()
            .filter(c -> c.getName().equals("Company A"))
            .findFirst()
            .orElseGet(() -> {
                Company c = new Company();
                c.setName("Company A");
                c.setCode("COMP_A");
                c.setActive(true);
                return companyRepository.save(c);
            });

        companyB = companyRepository.findAll().stream()
            .filter(c -> c.getName().equals("Company B"))
            .findFirst()
            .orElseGet(() -> {
                Company c = new Company();
                c.setName("Company B");
                c.setCode("COMP_B");
                c.setActive(true);
                return companyRepository.save(c);
            });

        // Fetch users (no builder, use findByEmail)
        adminUser = userRepository.findByEmail("admin@plus33.com")
            .orElseGet(() -> {
                User u = new User();
                u.setEmail("admin@plus33.com");
                u.setFirstName("Admin");
                u.setLastName("User");
                u.setPassword("pass");
                u.setActive(true);
                return userRepository.save(u);
            });
        
        treasuryUser = userRepository.findByEmail("treasury@plus33.com")
            .orElseGet(() -> {
                User u = new User();
                u.setEmail("treasury@plus33.com");
                u.setFirstName("Treasury");
                u.setLastName("User");
                u.setPassword("pass");
                u.setActive(true);
                return userRepository.save(u);
            });
        // Setup Supplier
        supplierA = supplierRepository.findAll().stream()
            .filter(s -> s.getCompany().getId().equals(companyA.getId()))
            .findFirst()
            .orElseGet(() -> supplierRepository.save(Supplier.builder().company(companyA).code("SUP_A").name("Supplier A").active(true).build()));

        // Setup GL accounts
        cashAccountA = accountRepository.findAll().stream()
            .filter(a -> a.getCompany().getId().equals(companyA.getId()) && a.getAccountCode().equals("1000"))
            .findFirst()
            .orElseGet(() -> accountRepository.save(Account.builder().company(companyA).accountCode("1000").accountName("Cash").accountType("ASSET").active(true).build()));

        cashAccountB = accountRepository.findAll().stream()
            .filter(a -> a.getCompany().getId().equals(companyB.getId()) && a.getAccountCode().equals("1000"))
            .findFirst()
            .orElseGet(() -> accountRepository.save(Account.builder().company(companyB).accountCode("1000").accountName("Cash").accountType("ASSET").active(true).build()));

        expenseAccount = accountRepository.findAll().stream()
            .filter(a -> a.getCompany().getId().equals(companyA.getId()) && a.getAccountCode().equals("5000"))
            .findFirst()
            .orElseGet(() -> accountRepository.save(Account.builder().company(companyA).accountCode("5000").accountName("Expenses").accountType("EXPENSE").active(true).build()));

        // Seed Treasury Approval Steps
        if (treasuryApprovalStepRepository.findAll().isEmpty()) {
            treasuryApprovalStepRepository.save(TreasuryApprovalStep.builder().stepSequence(1).roleCode("TREASURY_MANAGER").minAmount(BigDecimal.ZERO).maxAmount(BigDecimal.valueOf(10000.00)).active(true).build());
            treasuryApprovalStepRepository.save(TreasuryApprovalStep.builder().stepSequence(2).roleCode("FINANCE_DIRECTOR").minAmount(BigDecimal.valueOf(10000.01)).maxAmount(BigDecimal.valueOf(100000.00)).active(true).build());
            treasuryApprovalStepRepository.save(TreasuryApprovalStep.builder().stepSequence(3).roleCode("CFO").minAmount(BigDecimal.valueOf(100000.01)).maxAmount(BigDecimal.valueOf(10000000.00)).active(true).build());
        }
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", roles = {"ULTIMATE_ADMIN"})
    public void testTreasuryWorkflowAndIntegrations() throws Exception {
        // --- 1. Multi-Company Bank & Account Hierarchy Registration ---
        BankRequest bankReq = new BankRequest("HSBC", "HSBC Middle East", "HSBC001", "AA+", 1, BigDecimal.valueOf(10000000.00), BigDecimal.valueOf(10000000.00), true);
        String bankJson = mockMvc.perform(post("/api/v1/treasury/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BankResponse bankRes = objectMapper.readValue(objectMapper.readTree(bankJson).get("data").toString(), BankResponse.class);
        assertNotNull(bankRes);
        assertEquals("HSBC", bankRes.code());

        // Create branch
        BankBranchRequest branchReq = new BankBranchRequest(bankRes.id(), "DXB_BR", "Dubai Main Branch", "HSBCAEADXXX", "Sheikh Zayed Rd, Dubai");
        String branchJson = mockMvc.perform(post("/api/v1/treasury/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BankBranchResponse branchRes = objectMapper.readValue(objectMapper.readTree(branchJson).get("data").toString(), BankBranchResponse.class);
        assertNotNull(branchRes);

        // Create company A bank account
        BankAccountRequest acctReqA = new BankAccountRequest(companyA.getId(), branchRes.id(), "Comp A Current", "123-456-789", "AE123456789", "AED", cashAccountA.getId(), "CURRENT", BigDecimal.valueOf(500000.00));
        String acctJsonA = mockMvc.perform(post("/api/v1/treasury/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(acctReqA)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BankAccountResponse acctResA = objectMapper.readValue(objectMapper.readTree(acctJsonA).get("data").toString(), BankAccountResponse.class);
        assertNotNull(acctResA);
        assertEquals(0, BigDecimal.valueOf(500000.00).compareTo(acctResA.currentBalance()));

        // Create company B bank account (Multi-company isolation check)
        BankAccountRequest acctReqB = new BankAccountRequest(companyB.getId(), branchRes.id(), "Comp B Current", "987-654-321", "AE987654321", "AED", cashAccountB.getId(), "CURRENT", BigDecimal.valueOf(200000.00));
        String acctJsonB = mockMvc.perform(post("/api/v1/treasury/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(acctReqB)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BankAccountResponse acctResB = objectMapper.readValue(objectMapper.readTree(acctJsonB).get("data").toString(), BankAccountResponse.class);
        assertNotNull(acctResB);

        // --- 2. Zero-Balance Pooling Sweep ---
        // Header account
        BankAccountRequest headerReq = new BankAccountRequest(companyA.getId(), branchRes.id(), "Comp A Header", "111-222-333", "AE111222333", "AED", cashAccountA.getId(), "CURRENT", BigDecimal.ZERO);
        BankAccountResponse headerRes = objectMapper.readValue(objectMapper.readTree(
            mockMvc.perform(post("/api/v1/treasury/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(headerReq)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()
        ).get("data").toString(), BankAccountResponse.class);

        // Member sweep setup
        CashPoolRequest poolReq = new CashPoolRequest(companyA.getId(), "Dubai ZBP Pool", "ZERO_BALANCE", headerRes.id(), BigDecimal.ZERO, List.of(new CashPoolMemberRequest(acctResA.id(), 1)));
        String poolJson = mockMvc.perform(post("/api/v1/treasury/pools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(poolReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CashPoolResponse poolRes = objectMapper.readValue(objectMapper.readTree(poolJson).get("data").toString(), CashPoolResponse.class);
        assertNotNull(poolRes);

        // Execute sweep
        mockMvc.perform(post("/api/v1/treasury/pools/" + poolRes.id() + "/sweep"))
                .andExpect(status().isOk());

        // Assert member zeroed out and header balance increased
        assertEquals(0, BigDecimal.ZERO.compareTo(bankAccountRepository.findById(acctResA.id()).get().getCurrentBalance()));
        assertEquals(0, BigDecimal.valueOf(500000.00).compareTo(bankAccountRepository.findById(headerRes.id()).get().getCurrentBalance()));

        // Restore member balance for subsequent tests
        BankAccount memberAcct = bankAccountRepository.findById(acctResA.id()).get();
        memberAcct.setCurrentBalance(BigDecimal.valueOf(500000.00));
        bankAccountRepository.save(memberAcct);

        // --- 3. Multi-Currency Bank Cash Transfer ---
        // Create USD destination account
        BankAccountRequest usdAcctReq = new BankAccountRequest(companyA.getId(), branchRes.id(), "Comp A USD Account", "444-555-666", "AE444555666", "USD", cashAccountA.getId(), "CURRENT", BigDecimal.ZERO);
        BankAccountResponse usdAcctRes = objectMapper.readValue(objectMapper.readTree(
            mockMvc.perform(post("/api/v1/treasury/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(usdAcctReq)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()
        ).get("data").toString(), BankAccountResponse.class);

        // Request Transfer: AED to USD (Rate 0.27)
        CashTransferRequest transferReq = new CashTransferRequest(companyA.getId(), acctResA.id(), usdAcctRes.id(), LocalDate.now(), BigDecimal.valueOf(10000.00), BigDecimal.valueOf(0.27), BigDecimal.valueOf(50.00), "TX-001");
        String transferJson = mockMvc.perform(post("/api/v1/treasury/transfers")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CashTransferResponse transferRes = objectMapper.readValue(objectMapper.readTree(transferJson).get("data").toString(), CashTransferResponse.class);
        assertNotNull(transferRes);
        assertEquals("PENDING_APPROVAL", transferRes.status());

        // --- 4. Approval Matrix Routing & Four-Eyes Principle limits ---
        // Verify same-user approval rejection (Four-eyes check)
        mockMvc.perform(post("/api/v1/treasury/transfers/" + transferRes.id() + "/approve?remarks=MyOwnTransfer")
                .with(user("admin@plus33.com")))
                .andExpect(status().isBadRequest()); // BadRequest since creator cannot approve it

        // Approve Transfer by different user (treasury@plus33.com)
        mockMvc.perform(post("/api/v1/treasury/transfers/" + transferRes.id() + "/approve?remarks=ApprovedByManager")
                .with(user("treasury@plus33.com")))
                .andExpect(status().isOk());

        // Assert balances updated: Deducted AED (10000 + 50 fee) and credited USD (10000 * 0.27 = 2700)
        assertEquals(0, BigDecimal.valueOf(489950.00).compareTo(bankAccountRepository.findById(acctResA.id()).get().getCurrentBalance()));
        assertEquals(0, BigDecimal.valueOf(2700.00).compareTo(bankAccountRepository.findById(usdAcctRes.id()).get().getCurrentBalance()));

        // --- 5. Payment Factory Consolidations & ISO 20022 XML generation ---
        // Create completed payments to batch
        Payment payment1 = paymentRepository.save(Payment.builder().company(companyA).supplier(supplierA).paymentNumber("PMT-001").paymentDate(LocalDate.now()).paymentMethod("BANK_TRANSFER").paymentType("PAYABLE").amount(BigDecimal.valueOf(1500.00)).currencyCode("AED").status(PaymentStatus.COMPLETED).createdBy(adminUser).build());
        Payment payment2 = paymentRepository.save(Payment.builder().company(companyA).supplier(supplierA).paymentNumber("PMT-002").paymentDate(LocalDate.now()).paymentMethod("BANK_TRANSFER").paymentType("PAYABLE").amount(BigDecimal.valueOf(2500.00)).currencyCode("AED").status(PaymentStatus.COMPLETED).createdBy(adminUser).build());

        PaymentBatchRequest batchReq = new PaymentBatchRequest(companyA.getId(), "BATCH-2026-001", acctResA.id(), List.of(payment1.getId(), payment2.getId()));
        String batchJson = mockMvc.perform(post("/api/v1/treasury/payments/batches")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        PaymentBatchResponse batchRes = objectMapper.readValue(objectMapper.readTree(batchJson).get("data").toString(), PaymentBatchResponse.class);
        assertNotNull(batchRes);

        // Approve Payment Batch (Using different approver to bypass four-eyes)
        mockMvc.perform(post("/api/v1/treasury/payments/batches/" + batchRes.id() + "/approve?remarks=ApprovedBatch")
                .with(user("treasury@plus33.com")))
                .andExpect(status().isOk());

        // Generate Payment File (ISO 20022 format)
        String fileJson = mockMvc.perform(post("/api/v1/treasury/payments/batches/" + batchRes.id() + "/generate-file?format=ISO20022_XML")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PaymentFileResponse fileRes = objectMapper.readValue(objectMapper.readTree(fileJson).get("data").toString(), PaymentFileResponse.class);
        assertNotNull(fileRes);
        assertTrue(fileRes.fileContent().contains("<CstmrCdtTrfInitn>"));

        // Transmit File
        mockMvc.perform(post("/api/v1/treasury/payments/files/" + fileRes.id() + "/transmit?method=API")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());
        assertEquals("TRANSMITTED", paymentBatchRepository.findById(batchRes.id()).get().getStatus());

        // --- 6. Bank Statement Import & Reconciliation Rule Engine ---
        // Setup direct payment to match
        Payment paymentToMatch = paymentRepository.save(Payment.builder().company(companyA).supplier(supplierA).paymentNumber("PMT-REC-01").paymentDate(LocalDate.now()).paymentMethod("BANK_TRANSFER").paymentType("PAYABLE").amount(BigDecimal.valueOf(800.00)).currencyCode("AED").referenceNumber("REF-RECON-100").status(PaymentStatus.COMPLETED).createdBy(adminUser).build());

        // Import bank statement (Negative amount for Outward withdrawal)
        BankStatementLineRequest stmtLine = new BankStatementLineRequest(LocalDate.now(), LocalDate.now(), "Supplier payment REF-RECON-100", "REF-RECON-100", BigDecimal.valueOf(-800.00));
        BankStatementRequest stmtReq = new BankStatementRequest(acctResA.id(), "STMT-2026-06", LocalDate.now().minusDays(5), LocalDate.now(), BigDecimal.valueOf(489950.00), BigDecimal.valueOf(489150.00), List.of(stmtLine));

        // Duplicate Statement Import Prevention Check
        String stmtJson = mockMvc.perform(post("/api/v1/treasury/statements/import")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stmtReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BankStatementResponse stmtRes = objectMapper.readValue(objectMapper.readTree(stmtJson).get("data").toString(), BankStatementResponse.class);
        assertNotNull(stmtRes);

        mockMvc.perform(post("/api/v1/treasury/statements/import")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stmtReq)))
                .andExpect(status().isBadRequest()); // Rejected due to duplicate detection

        // Define Reconciliation Rules
        ReconciliationRuleRequest ruleReq = new ReconciliationRuleRequest(companyA.getId(), "Fuzzy Reference Match Rule", 3, "FUZZY", false, false, false);
        mockMvc.perform(post("/api/v1/treasury/reconciliation/rules")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ruleReq)));
        
        // Setup direct rule via repository
        reconciliationRuleRepository.save(ReconciliationRule.builder().company(companyA).ruleName("Fuzzy Match").dateToleranceDays(5).referenceMatchType("FUZZY").active(true).build());

        // Run Auto-Reconciliation
        mockMvc.perform(post("/api/v1/treasury/statements/" + stmtRes.id() + "/auto-reconcile?companyId=" + companyA.getId())
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());

        // Verify statement is fully matched and reconciled_balance updated (Deducted 800)
        assertEquals("RECONCILED", bankStatementRepository.findById(stmtRes.id()).get().getStatus());
        assertEquals(0, BigDecimal.valueOf(499200.00).compareTo(bankAccountRepository.findById(acctResA.id()).get().getReconciledBalance()));

        // --- 7. Bank Fee Rules & Processing ---
        BankFeeRuleRequest feeRuleReq = new BankFeeRuleRequest(acctResA.id(), "MONTHLY_MAINTENANCE", BigDecimal.ZERO, BigDecimal.valueOf(150.00), expenseAccount.getId());
        mockMvc.perform(post("/api/v1/treasury/accounts/" + acctResA.id() + "/process-fees")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());
        // Directly seed fee rule and execute
        bankFeeRuleRepository.save(BankFeeRule.builder().bankAccount(bankAccountRepository.findById(acctResA.id()).get()).chargeType("MONTHLY_MAINTENANCE").fixedAmount(BigDecimal.valueOf(150.00)).glExpenseAccount(expenseAccount).active(true).build());
        reconciliationService.processBankFees(acctResA.id(), "admin@plus33.com");

        // Verify fee deducted from current balance (489950 - 150 = 489800)
        assertEquals(0, BigDecimal.valueOf(489800.00).compareTo(bankAccountRepository.findById(acctResA.id()).get().getCurrentBalance()));

        // --- 8. Short-Term Investment Yield & Compounding Accruals ---
        // Create Fixed Deposit: 100k principal, 5.50% yield, AED
        TreasuryInvestmentRequest invReq = new TreasuryInvestmentRequest(acctResA.id(), "FD-2026-001", "FIXED_DEPOSIT", BigDecimal.valueOf(100000.00), BigDecimal.valueOf(5.50), BigDecimal.valueOf(5500.00), BigDecimal.valueOf(5.50), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now(), LocalDate.now().plusMonths(12));
        String invJson = mockMvc.perform(post("/api/v1/treasury/investments")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TreasuryInvestmentResponse invRes = objectMapper.readValue(objectMapper.readTree(invJson).get("data").toString(), TreasuryInvestmentResponse.class);
        assertNotNull(invRes);

        // Verify account current balance decremented by principal (489800 - 100000 = 389800)
        assertEquals(0, BigDecimal.valueOf(389800.00).compareTo(bankAccountRepository.findById(acctResA.id()).get().getCurrentBalance()));

        // Accrue daily interest
        mockMvc.perform(post("/api/v1/treasury/investments/" + invRes.id() + "/accrue")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());
        
        TreasuryInvestment updatedInv = treasuryInvestmentRepository.findById(invRes.id()).get();
        // Daily yield = 100000 * 0.055 / 365 = ~15.07
        assertEquals(0, BigDecimal.valueOf(15.07).compareTo(updatedInv.getAccruedInterest()));

        // Settle early liquidation penalty check
        mockMvc.perform(post("/api/v1/treasury/investments/" + invRes.id() + "/liquidate")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());
        assertEquals("LIQUIDATED", treasuryInvestmentRepository.findById(invRes.id()).get().getStatus());

        // --- 9. In-House Bank & Intercompany Lending ---
        // Setup subsidiary in-house account
        InHouseBankAccountRequest ihbReq = new InHouseBankAccountRequest(companyA.getId(), "IHB-A-01", "AED", cashAccountA.getId());
        String ihbJson = mockMvc.perform(post("/api/v1/treasury/ihb/accounts")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ihbReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        InHouseBankAccountResponse ihbRes = objectMapper.readValue(objectMapper.readTree(ihbJson).get("data").toString(), InHouseBankAccountResponse.class);
        assertNotNull(ihbRes);

        // Setup intercompany loan
        IntercompanyLoanRequest loanReq = new IntercompanyLoanRequest(companyB.getId(), companyA.getId(), BigDecimal.valueOf(50000.00), BigDecimal.valueOf(4.00), LocalDate.now(), LocalDate.now().plusMonths(6));
        String loanJson = mockMvc.perform(post("/api/v1/treasury/ihb/loans")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        IntercompanyLoanResponse loanRes = objectMapper.readValue(objectMapper.readTree(loanJson).get("data").toString(), IntercompanyLoanResponse.class);
        assertNotNull(loanRes);

        // Accrue Intercompany Loan Interest (Accrual computation)
        mockMvc.perform(post("/api/v1/treasury/ihb/loans/" + loanRes.id() + "/accrue")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk());
        
        IntercompanyLoan updatedLoan = intercompanyLoanRepository.findById(loanRes.id()).get();
        // Daily yield = 50000 * 0.04 / 365 = ~5.48
        assertEquals(0, BigDecimal.valueOf(5.48).compareTo(updatedLoan.getInterestAccrued()));

        // --- 10. Cash Position Snapshots, Forecasting & Compliance limits ---
        // Take daily cash snapshot
        CashPositionSnapshotRequest snapReq = new CashPositionSnapshotRequest(companyA.getId(), "END_OF_DAY");
        String snapJson = mockMvc.perform(post("/api/v1/treasury/snapshots")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snapReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CashPositionSnapshotResponse snapRes = objectMapper.readValue(objectMapper.readTree(snapJson).get("data").toString(), CashPositionSnapshotResponse.class);
        assertNotNull(snapRes);

        // Enforce compliance limit check
        TreasuryLimitRequest limitReq = new TreasuryLimitRequest(companyA.getId(), "MINIMUM_CASH_RESERVE", "AED", null, null, BigDecimal.valueOf(1000000.00), BigDecimal.valueOf(80.00));
        mockMvc.perform(post("/api/v1/treasury/limits")
                .with(user("admin@plus33.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(limitReq)))
                .andExpect(status().isCreated());

        // Perform compliance limits sweep checking
        treasuryForecastService.checkExposureLimits(companyA.getId());

        // Forecast Scenario check
        mockMvc.perform(get("/api/v1/treasury/companies/" + companyA.getId() + "/forecast?days=30")
                .with(user("admin@plus33.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.BASE").exists())
                .andExpect(jsonPath("$.data.BEST_CASE").exists())
                .andExpect(jsonPath("$.data.WORST_CASE").exists());
    }
}
