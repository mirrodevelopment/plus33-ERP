package com.plus33.erp.paymentrun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.paymentrun.dto.*;
import com.plus33.erp.paymentrun.entity.*;
import com.plus33.erp.paymentrun.repository.*;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
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

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentRunControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierInvoiceRepository supplierInvoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentAllocationRepository paymentAllocationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRunRepository paymentRunRepository;

    @Autowired
    private PaymentRunInvoiceRepository paymentRunInvoiceRepository;

    @Autowired
    private PaymentRunSupplierResultRepository supplierResultRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company company;
    private Supplier supplier1;
    private Supplier supplier2;
    private User adminUser;

    private SupplierInvoice invoiceSupplier1A;
    private SupplierInvoice invoiceSupplier1B;
    private SupplierInvoice invoiceSupplier2;
    private SupplierInvoice invoicePaid;
    private SupplierInvoice invoiceDraft;

    @BeforeEach
    public void setUp() {
        // Clean up database tables first to ensure clean state
        cleanupDatabase();

        // 1. Load Company
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        // 2. Load Admin User
        adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // 3. Create Suppliers with bank details
        supplier1 = supplierRepository.save(Supplier.builder()
                .company(company)
                .code("SUPP_RUN_01")
                .name("Acme Supplier 1")
                .bankName("Abu Dhabi Commercial Bank")
                .bankAccountNumber("ADCB123456789")
                .swiftCode("ADCBAEADXXX")
                .iban("AE930230000012345678901")
                .active(true)
                .build());

        supplier2 = supplierRepository.save(Supplier.builder()
                .company(company)
                .code("SUPP_RUN_02")
                .name("Global Supplier 2")
                .bankName("Emirates NBD")
                .bankAccountNumber("ENBD987654321")
                .swiftCode("ENBDAEADXXX")
                .iban("AE930240000098765432101")
                .active(true)
                .build());

        // 4. Seed basic accounts for GL integration
        seedAccount(company, "1200", "Bank Account", "ASSET");
        seedAccount(company, "2100", "Accounts Payable", "LIABILITY");
        seedAccount(company, "5200", "Expense Account", "EXPENSE");

        // 5. Seed Invoices
        // Supplier 1 — Invoice A (APPROVED, 1000.00, due TODAY)
        invoiceSupplier1A = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier1)
                .invoiceNumber("INV-RUN-SUP1-A")
                .invoiceDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(1000.00))
                .outstandingBalance(BigDecimal.valueOf(1000.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Supplier 1 — Invoice B (APPROVED, 500.00, due in 5 days)
        invoiceSupplier1B = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier1)
                .invoiceNumber("INV-RUN-SUP1-B")
                .invoiceDate(LocalDate.now().minusDays(15))
                .dueDate(LocalDate.now().plusDays(5))
                .totalAmount(BigDecimal.valueOf(500.00))
                .outstandingBalance(BigDecimal.valueOf(500.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Supplier 2 — Invoice (APPROVED, 2500.00, due in 6 days)
        invoiceSupplier2 = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier2)
                .invoiceNumber("INV-RUN-SUP2")
                .invoiceDate(LocalDate.now().minusDays(10))
                .dueDate(LocalDate.now().plusDays(6))
                .totalAmount(BigDecimal.valueOf(2500.00))
                .outstandingBalance(BigDecimal.valueOf(2500.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Paid Invoice (Should NOT be selected)
        invoicePaid = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier1)
                .invoiceNumber("INV-RUN-PAID")
                .invoiceDate(LocalDate.now().minusDays(30))
                .dueDate(LocalDate.now().minusDays(10))
                .totalAmount(BigDecimal.valueOf(300.00))
                .paidAmount(BigDecimal.valueOf(300.00))
                .outstandingBalance(BigDecimal.ZERO)
                .status(SupplierInvoiceStatus.PAID)
                .currencyCode("AED")
                .build());

        // Draft Invoice (Should NOT be selected)
        invoiceDraft = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier1)
                .invoiceNumber("INV-RUN-DRAFT")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .totalAmount(BigDecimal.valueOf(400.00))
                .outstandingBalance(BigDecimal.valueOf(400.00))
                .status(SupplierInvoiceStatus.DRAFT)
                .currencyCode("AED")
                .build());
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up database tables
        cleanupDatabase();

        // Clean up export files written to disk during tests
        Path path = Paths.get("storage", "exports", "bank");
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            }
        }
    }

    private void cleanupDatabase() {
        paymentRunInvoiceRepository.deleteAll();
        supplierResultRepository.deleteAll();
        
        // Break reservations on supplier invoices first
        List<SupplierInvoice> invoices = supplierInvoiceRepository.findAll();
        for (SupplierInvoice inv : invoices) {
            if (inv.getPaymentRun() != null) {
                inv.setPaymentRun(null);
                supplierInvoiceRepository.save(inv);
            }
        }
        
        paymentRunRepository.deleteAll();
        paymentAllocationRepository.deleteAll();
        paymentRepository.deleteAll();
        supplierInvoiceRepository.deleteAll();
        
        // Only delete seeded suppliers to avoid corrupting other test data
        supplierRepository.findAll().stream()
                .filter(s -> s.getCode().startsWith("SUPP_RUN_") || s.getCode().equals("SUPP_MISMATCH"))
                .forEach(s -> supplierRepository.delete(s));

        // Clean up company2 dependencies
        companyRepository.findByCode("COMP_TEST_ISO")
                .ifPresent(c -> companyRepository.delete(c));
    }

    private void seedAccount(Company company, String code, String name, String type) {
        if (accountRepository.findByCompanyIdAndAccountCode(company.getId(), code).isEmpty()) {
            Account newAccount = new Account();
            newAccount.setCompany(company);
            newAccount.setAccountCode(code);
            newAccount.setAccountName(name);
            newAccount.setAccountType(type);
            newAccount.setActive(true);
            accountRepository.save(newAccount);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 1. Security Permission Checks
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "user@plus33.com", authorities = {})
    public void testPaymentRunEndpointsPermissionRequired() throws Exception {
        mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentRunRequest(
                                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", null
                        ))))
                .andExpect(status().isForbidden());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 2. Draft Creation, Calculation & Reservation Locking
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"PAYMENT_RUN_CREATE", "PAYMENT_RUN_VIEW"})
    public void testCreateCalculateAndReservationLock() throws Exception {
        // Step 1: Create Draft Run
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(),
                LocalDate.now(),
                "BANK_TRANSFER",
                "AED",
                "1200",
                null,
                null,
                "CSV",
                null
        );

        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.runNumber").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        // Step 2: Calculate Invoices (adds eligible, reserves them)
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CALCULATED"))
                .andExpect(jsonPath("$.data.invoices.length()").value(3)) // 1A, 1B, 2
                .andExpect(jsonPath("$.data.totalAmount").value(4000.00)); // 1000 + 500 + 2500

        // Assert they are reserved in the database
        SupplierInvoice dbInvoice = supplierInvoiceRepository.findById(invoiceSupplier1A.getId()).orElseThrow();
        assertNotNull(dbInvoice.getPaymentRun());
        assertEquals(runId, dbInvoice.getPaymentRun().getId());

        // Step 3: Verify Lock - concurrent payment run calculation should exclude them
        PaymentRunRequest request2 = new PaymentRunRequest(
                company.getId(),
                LocalDate.now(),
                "BANK_TRANSFER",
                "AED",
                "1200",
                null,
                null,
                "CSV",
                null
        );
        String responseStr2 = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long runId2 = objectMapper.readTree(responseStr2).path("data").path("id").asLong();

        // Calculate second run - should select 0 invoices because all 3 outstanding ones are reserved by Run 1
        mockMvc.perform(post("/api/v1/payment-runs/" + runId2 + "/calculate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.invoices.length()").value(0))
                .andExpect(jsonPath("$.data.totalAmount").value(0.00));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 3. Partial Payments & Invoice Release
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"PAYMENT_RUN_CREATE", "PAYMENT_RUN_VIEW"})
    public void testPartialPaymentsAndRelease() throws Exception {
        // Step 1: Create and Calculate Run
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", null
        );
        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"));

        // Step 2: Update Invoices:
        // Invoice 1A: Partial payment of 600.00 (Outstanding 1000.00)
        // Invoice 1B: Excluded (paymentAmount = 0.00)
        // Invoice 2: Unchanged (omit from request, should keep original 2500.00)
        List<PaymentRunInvoiceRequest> updateRequests = List.of(
                new PaymentRunInvoiceRequest(invoiceSupplier1A.getId(), BigDecimal.valueOf(600.00), "PARTIAL_REF_A"),
                new PaymentRunInvoiceRequest(invoiceSupplier1B.getId(), BigDecimal.ZERO, null)
        );

        mockMvc.perform(put("/api/v1/payment-runs/" + runId + "/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.invoices.length()").value(2)) // 1A and 2 remain. 1B is removed.
                .andExpect(jsonPath("$.data.totalAmount").value(3100.00)); // 600 + 2500

        // Verify Invoice 1B was released (its paymentRun reference in DB is null)
        SupplierInvoice releasedInvoice = supplierInvoiceRepository.findById(invoiceSupplier1B.getId()).orElseThrow();
        assertNull(releasedInvoice.getPaymentRun());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4. Grouping by Supplier, Consolidated Payments, and Audit Trail
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PAYMENT_RUN_CREATE", "PAYMENT_RUN_APPROVE", "PAYMENT_RUN_EXECUTE", "PAYMENT_RUN_VIEW"
    })
    public void testGroupingConsolidationAndAuditTrail() throws Exception {
        UUID clientRef = UUID.randomUUID();
        // Step 1: Create Draft
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", clientRef
        );
        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        // Calculate and Approve
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"));
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.approvedByEmail").value("admin@plus33.com"))
                .andExpect(jsonPath("$.data.approvedAt").isNotEmpty());

        // Step 2: Execute Payment Run
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/execute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.successfulPaymentsCount").value(2)) // Supplier 1 and Supplier 2
                .andExpect(jsonPath("$.data.failedPaymentsCount").value(0))
                .andExpect(jsonPath("$.data.processedInvoicesCount").value(3))
                .andExpect(jsonPath("$.data.executedByEmail").value("admin@plus33.com"))
                .andExpect(jsonPath("$.data.executedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.exportFileName").isNotEmpty())
                .andExpect(jsonPath("$.data.exportStoragePath").isNotEmpty())
                .andExpect(jsonPath("$.data.exportChecksum").isNotEmpty())
                .andExpect(jsonPath("$.data.exportGeneratedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.supplierResults.length()").value(2));

        // Step 3: Verify Grouping & Allocations in DB
        // Supplier 1 had 2 invoices (1A: 1000, 1B: 500). Should result in exactly ONE consolidated Payment of 1500.00!
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(p -> p.getSupplier().getId().equals(supplier1.getId()))
                .toList();
        assertEquals(1, payments.size());
        Payment consolidatedPayment = payments.get(0);
        assertEquals(BigDecimal.valueOf(1500.00).setScale(2), consolidatedPayment.getAmount().setScale(2));

        // Verify that there are exactly TWO allocations for this payment
        List<PaymentAllocation> allocations = paymentAllocationRepository.findAll().stream()
                .filter(a -> a.getPayment().getId().equals(consolidatedPayment.getId()))
                .toList();
        assertEquals(2, allocations.size());

        // Verify outstanding balances are updated to 0, and invoice statuses are PAID
        SupplierInvoice si1A = supplierInvoiceRepository.findById(invoiceSupplier1A.getId()).orElseThrow();
        assertEquals(BigDecimal.ZERO.setScale(2), si1A.getOutstandingBalance().setScale(2));
        assertEquals(SupplierInvoiceStatus.PAID, si1A.getStatus());

        SupplierInvoice si1B = supplierInvoiceRepository.findById(invoiceSupplier1B.getId()).orElseThrow();
        assertEquals(BigDecimal.ZERO.setScale(2), si1B.getOutstandingBalance().setScale(2));
        assertEquals(SupplierInvoiceStatus.PAID, si1B.getStatus());

        // Step 4: Verify Idempotency - executing again should return the existing completed run safely
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/execute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5. Failure Isolation and Transaction Rollback
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PAYMENT_RUN_CREATE", "PAYMENT_RUN_APPROVE", "PAYMENT_RUN_EXECUTE", "PAYMENT_RUN_VIEW"
    })
    public void testFailureIsolationAndRollback() throws Exception {
        Company company2 = new Company();
        company2.setCode("COMP_TEST_ISO");
        company2.setName("Isolation Company");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        Supplier supplierMismatched = supplierRepository.save(Supplier.builder()
                .company(company2) // Mismatched Company!
                .code("SUPP_MISMATCH")
                .name("Mismatched Supplier")
                .active(true)
                .build());

        SupplierInvoice invoiceMismatched = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company2) // Mismatched Company!
                .supplier(supplierMismatched)
                .invoiceNumber("INV-RUN-MISMATCH")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(5))
                .totalAmount(BigDecimal.valueOf(500.00))
                .outstandingBalance(BigDecimal.valueOf(500.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Create and calculate a run for our main company
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", null
        );
        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        // Calculate (adds supplier1 invoices)
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"));

        // Now manually link the mismatched invoice to our run and add a PaymentRunInvoice record
        PaymentRun run = paymentRunRepository.findById(runId).orElseThrow();
        
        invoiceMismatched.setPaymentRun(run);
        supplierInvoiceRepository.save(invoiceMismatched);

        PaymentRunInvoice runInvoiceMismatched = PaymentRunInvoice.builder()
                .paymentRun(run)
                .supplierInvoice(invoiceMismatched)
                .invoiceOutstandingBalance(invoiceMismatched.getOutstandingBalance())
                .paymentAmount(invoiceMismatched.getOutstandingBalance())
                .paymentReference(run.getRunNumber())
                .build();
        paymentRunInvoiceRepository.save(runInvoiceMismatched);
        
        run.setTotalAmount(run.getTotalAmount().add(invoiceMismatched.getTotalAmount()));
        paymentRunRepository.saveAndFlush(run);

        // Approve
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/approve"));

        // Execute: Supplier 1 should succeed (status = SUCCESS), Supplier Mismatched should fail (status = FAILED)
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/execute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.successfulPaymentsCount").value(2)) // Supplier 1 and 2 succeeded
                .andExpect(jsonPath("$.data.failedPaymentsCount").value(1))     // Supplier Mismatched failed
                .andExpect(jsonPath("$.data.processedInvoicesCount").value(3))  // Supplier 1 (2) and Supplier 2 (1) = 3 processed
                .andExpect(jsonPath("$.data.supplierResults.length()").value(3));

        // Verify that Supplier 1's payment WAS created and committed
        List<Payment> paymentsS1 = paymentRepository.findAll().stream()
                .filter(p -> p.getSupplier().getId().equals(supplier1.getId()))
                .toList();
        assertFalse(paymentsS1.isEmpty());

        // Verify that Supplier Mismatched's payment WAS NOT created
        List<Payment> paymentsMismatch = paymentRepository.findAll().stream()
                .filter(p -> p.getSupplier().getId().equals(supplierMismatched.getId()))
                .toList();
        assertTrue(paymentsMismatch.isEmpty());

        // Verify that the mismatched invoice outstanding balance remains 500.00 and status remains APPROVED
        SupplierInvoice siMismatch = supplierInvoiceRepository.findById(invoiceMismatched.getId()).orElseThrow();
        assertEquals(BigDecimal.valueOf(500.00).setScale(2), siMismatch.getOutstandingBalance().setScale(2));
        assertEquals(SupplierInvoiceStatus.APPROVED, siMismatch.getStatus());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 6. Cancel Run Releases Invoices
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"PAYMENT_RUN_CREATE", "PAYMENT_RUN_VIEW"})
    public void testCancelReleasesInvoices() throws Exception {
        // Step 1: Create and Calculate Run
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", null
        );
        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"));

        // Verify they are reserved
        assertTrue(supplierInvoiceRepository.findById(invoiceSupplier1A.getId()).orElseThrow().getPaymentRun() != null);

        // Step 2: Cancel Run
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancelledByEmail").value("admin@plus33.com"))
                .andExpect(jsonPath("$.data.cancelledAt").isNotEmpty());

        // Verify they are released
        assertNull(supplierInvoiceRepository.findById(invoiceSupplier1A.getId()).orElseThrow().getPaymentRun());
        assertNull(supplierInvoiceRepository.findById(invoiceSupplier1B.getId()).orElseThrow().getPaymentRun());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 7. Dashboard KPIs
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PAYMENT_RUN_CREATE", "PAYMENT_RUN_APPROVE", "PAYMENT_RUN_EXECUTE", "PAYMENT_RUN_VIEW"
    })
    public void testDashboardKPIs() throws Exception {
        // Step 1: Get Initial Dashboard
        mockMvc.perform(get("/api/v1/payment-runs/dashboard").param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.totalPaymentRunsCount").value(0))
                .andExpect(jsonPath("$.data.paymentsDueTodayAmount").value(1000.00)) // invoiceSupplier1A due today
                .andExpect(jsonPath("$.data.paymentsDueThisWeekAmount").value(4000.00)); // 1A(1000) + 1B(500) + 2(2500)

        // Step 2: Create a completed run to check batch averages
        PaymentRunRequest request = new PaymentRunRequest(
                company.getId(), LocalDate.now(), "BANK_TRANSFER", "AED", "1200", null, null, "CSV", null
        );
        String responseStr = mockMvc.perform(post("/api/v1/payment-runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long runId = objectMapper.readTree(responseStr).path("data").path("id").asLong();

        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/calculate"));
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/approve"));
        mockMvc.perform(post("/api/v1/payment-runs/" + runId + "/execute"));

        // Step 3: Check Dashboard again
        mockMvc.perform(get("/api/v1/payment-runs/dashboard").param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPaymentRunsCount").value(1))
                .andExpect(jsonPath("$.data.completedTodayCount").value(1))
                .andExpect(jsonPath("$.data.largestPaymentRunAmount").value(4000.00))
                .andExpect(jsonPath("$.data.averagePaymentBatchSize").value(3.00)) // 3 invoices
                .andExpect(jsonPath("$.data.paymentsDueTodayAmount").value(0.00)) // all paid!
                .andExpect(jsonPath("$.data.paymentsDueThisWeekAmount").value(0.00));
    }
}
