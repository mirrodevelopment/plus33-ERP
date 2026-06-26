package com.plus33.erp.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.ap.dto.*;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class APControllerIntegrationTest {

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
    private ObjectMapper objectMapper;

    private Company company;
    private Supplier supplier;
    private User adminUser;

    private SupplierInvoice invoiceOverdue;
    private SupplierInvoice invoiceCurrent;
    private SupplierInvoice invoicePaid;
    private SupplierInvoice invoiceDraft;
    private SupplierInvoice invoiceSubmitted;

    @BeforeEach
    public void setUp() {
        // 1. Load PLUS33_GLOBAL company
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        // 2. Create Supplier
        supplier = supplierRepository.save(Supplier.builder()
                .company(company)
                .code("SUPP_TEST_AP_01")
                .name("Acme AP Supplier")
                .active(true)
                .build());

        // 3. Load Admin User
        adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // 4. Seed basic accounts for journal entry generation if needed
        seedAccount(company, "1300", "Inventory Asset", "ASSET");
        seedAccount(company, "2100", "Accounts Payable", "LIABILITY");
        seedAccount(company, "5200", "Cost of Goods Sold", "EXPENSE");

        // 5. Seed Invoices

        // Overdue Invoice (APPROVED, total 1000.00, overdue by 10 days)
        invoiceOverdue = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .invoiceNumber("INV-TEST-AP-OVERDUE")
                .invoiceDate(LocalDate.now().minusDays(40))
                .dueDate(LocalDate.now().minusDays(10))
                .totalAmount(BigDecimal.valueOf(1000.00))
                .outstandingBalance(BigDecimal.valueOf(1000.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Current Invoice (APPROVED, total 500.00, due in 15 days)
        invoiceCurrent = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .invoiceNumber("INV-TEST-AP-CURRENT")
                .invoiceDate(LocalDate.now().minusDays(15))
                .dueDate(LocalDate.now().plusDays(15))
                .totalAmount(BigDecimal.valueOf(500.00))
                .outstandingBalance(BigDecimal.valueOf(500.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build());

        // Paid Invoice (PAID, total 300.00, invoiceDate 20 days ago, due 5 days ago)
        invoicePaid = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .invoiceNumber("INV-TEST-AP-PAID")
                .invoiceDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(5))
                .totalAmount(BigDecimal.valueOf(300.00))
                .paidAmount(BigDecimal.valueOf(300.00))
                .outstandingBalance(BigDecimal.ZERO)
                .status(SupplierInvoiceStatus.PAID)
                .currencyCode("AED")
                .build());

        // Seed a Completed Payment and Payment Allocation to make Paid Invoice active in statement and average days
        Payment payment = paymentRepository.save(Payment.builder()
                .company(company)
                .supplier(supplier)
                .paymentNumber("PAY-TEST-AP-01")
                .paymentDate(LocalDate.now().minusDays(10)) // paid 10 days ago (so 10 days after invoice date)
                .paymentMethod("BANK_TRANSFER")
                .paymentType("PAYABLE")
                .amount(BigDecimal.valueOf(300.00))
                .status(PaymentStatus.COMPLETED)
                .createdBy(adminUser)
                .build());

        paymentAllocationRepository.save(PaymentAllocation.builder()
                .payment(payment)
                .supplierInvoice(invoicePaid)
                .allocatedAmount(BigDecimal.valueOf(300.00))
                .build());

        // Draft Invoice (DRAFT, total 200.00)
        invoiceDraft = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .invoiceNumber("INV-TEST-AP-DRAFT")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .totalAmount(BigDecimal.valueOf(200.00))
                .outstandingBalance(BigDecimal.valueOf(200.00))
                .status(SupplierInvoiceStatus.DRAFT)
                .currencyCode("AED")
                .build());

        // Submitted Invoice (SUBMITTED, total 400.00)
        invoiceSubmitted = supplierInvoiceRepository.save(SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .invoiceNumber("INV-TEST-AP-SUBMITTED")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .totalAmount(BigDecimal.valueOf(400.00))
                .outstandingBalance(BigDecimal.valueOf(400.00))
                .status(SupplierInvoiceStatus.SUBMITTED)
                .currencyCode("AED")
                .build());
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
    // Test Permissions
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "user@plus33.com", authorities = {})
    public void testAPEndpointsPermissionRequired() throws Exception {
        mockMvc.perform(get("/api/v1/ap/dashboard").param("companyId", company.getId().toString()))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/ap/aging").param("companyId", company.getId().toString()))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/ap/suppliers/" + supplier.getId() + "/balance").param("companyId", company.getId().toString()))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/ap/suppliers/" + supplier.getId() + "/statement")
                        .param("companyId", company.getId().toString())
                        .param("from", LocalDate.now().toString())
                        .param("to", LocalDate.now().toString()))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceDraft.getId() + "/void"))
                .andExpect(status().isForbidden());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Test End-to-End AP Workflow
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "AP_VIEW", "AP_STATEMENT_VIEW", "VENDOR_BILL_VOID", "SUPPLIER_INVOICE_UPDATE"
    })
    public void testAPDashboardAndReports() throws Exception {
        LocalDate today = LocalDate.now();

        // 1. Get Dashboard KPIs
        mockMvc.perform(get("/api/v1/ap/dashboard")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.totalPayables").value(1500.00)) // 1000 + 500
                .andExpect(jsonPath("$.data.currentDue").value(500.00))
                .andExpect(jsonPath("$.data.overdue").value(1000.00))
                .andExpect(jsonPath("$.data.openBills").value(2))
                .andExpect(jsonPath("$.data.averagePaymentDays").value(10.00)) // invoice date minusDays(20), paymentDate minusDays(10)
                .andExpect(jsonPath("$.data.billsAwaitingApproval").value(1)) // invoiceSubmitted
                .andExpect(jsonPath("$.data.topSuppliers.length()").value(1))
                .andExpect(jsonPath("$.data.topSuppliers[0].supplierId").value(supplier.getId()))
                .andExpect(jsonPath("$.data.topSuppliers[0].outstandingBalance").value(1500.00))
                .andExpect(jsonPath("$.data.cashRequirement.next30Days").value(1500.00)); // overdue immediately + current due in 15 days

        // 2. Get Custom Configurable Aging Report
        mockMvc.perform(get("/api/v1/ap/aging")
                        .param("companyId", company.getId().toString())
                        .param("supplierId", supplier.getId().toString())
                        .param("intervals", "15,30")) // Buckets: Current, 1-15, 16-30, 30+
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].totalOutstanding").value(1500.00))
                .andExpect(jsonPath("$.data[0].buckets.length()").value(4))
                .andExpect(jsonPath("$.data[0].buckets[0].bucketName").value("Current"))
                .andExpect(jsonPath("$.data[0].buckets[0].outstandingAmount").value(500.00)) // invoiceCurrent
                .andExpect(jsonPath("$.data[0].buckets[1].bucketName").value("1-15"))
                .andExpect(jsonPath("$.data[0].buckets[1].outstandingAmount").value(1000.00)) // invoiceOverdue (10 days overdue)
                .andExpect(jsonPath("$.data[0].buckets[2].bucketName").value("16-30"))
                .andExpect(jsonPath("$.data[0].buckets[2].outstandingAmount").value(0.00))
                .andExpect(jsonPath("$.data[0].buckets[3].bucketName").value("30+"))
                .andExpect(jsonPath("$.data[0].buckets[3].outstandingAmount").value(0.00));

        // 3. Get Supplier AP Balance
        mockMvc.perform(get("/api/v1/ap/suppliers/" + supplier.getId() + "/balance")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalOutstanding").value(1500.00))
                .andExpect(jsonPath("$.data.totalOverdue").value(1000.00))
                .andExpect(jsonPath("$.data.totalPaid").value(300.00))
                .andExpect(jsonPath("$.data.totalCredited").value(0.00));

        // 4. Get Supplier Statement Chronological Ledger
        mockMvc.perform(get("/api/v1/ap/suppliers/" + supplier.getId() + "/statement")
                        .param("companyId", company.getId().toString())
                        .param("from", today.minusDays(50).toString())
                        .param("to", today.plusDays(20).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.openingBalance").value(0.00))
                .andExpect(jsonPath("$.data.closingBalance").value(1500.00)) // Bill(300) - Pay(300) + Bill(1000) + Bill(500) = 1500? Wait, let's see.
                // Entries:
                // 1. BILL (invoiceOverdue, date today-40): credit=1000
                // 2. BILL (invoicePaid, date today-20): credit=300
                // 3. BILL (invoiceCurrent, date today-15): credit=500
                // 4. PAYMENT (payment, date today-10): debit=300
                // Total credits = 1800, total debits = 300, net balance = 1500.
                // Let's verify chronological sorting: today-40, today-20, today-15, today-10.
                .andExpect(jsonPath("$.data.entries.length()").value(4))
                .andExpect(jsonPath("$.data.entries[0].referenceNumber").value("INV-TEST-AP-OVERDUE"))
                .andExpect(jsonPath("$.data.entries[0].runningBalance").value(1000.00))
                .andExpect(jsonPath("$.data.entries[1].referenceNumber").value("INV-TEST-AP-PAID"))
                .andExpect(jsonPath("$.data.entries[1].runningBalance").value(1300.00))
                .andExpect(jsonPath("$.data.entries[2].referenceNumber").value("INV-TEST-AP-CURRENT"))
                .andExpect(jsonPath("$.data.entries[2].runningBalance").value(1800.00))
                .andExpect(jsonPath("$.data.entries[3].referenceNumber").value("PAY-TEST-AP-01"))
                .andExpect(jsonPath("$.data.entries[3].runningBalance").value(1500.00))
                .andExpect(jsonPath("$.data.closingBalance").value(1500.00));

        // 5. Get Overdue Bills
        mockMvc.perform(get("/api/v1/ap/overdue")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].billNumber").value("INV-TEST-AP-OVERDUE"))
                .andExpect(jsonPath("$.data.content[0].daysOverdue").value(10));

        // 6. Get AP Analytics
        mockMvc.perform(get("/api/v1/ap/analytics")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.averageInvoiceAmount").value(600.00)) // (1000 + 500 + 300) / 3 = 600
                .andExpect(jsonPath("$.data.averageDaysToPay").value(10.00))
                .andExpect(jsonPath("$.data.supplierConcentration.length()").value(1))
                .andExpect(jsonPath("$.data.largestOutstandingSuppliers[0].outstandingBalance").value(1500.00));

        // 7. Void Draft Supplier Invoice (Allowed)
        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceDraft.getId() + "/void"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("VOID"));

        // 8. Void Submitted Supplier Invoice (Allowed)
        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceSubmitted.getId() + "/void"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("VOID"));

        // 9. Void Approved Supplier Invoice (Blocked)
        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceOverdue.getId() + "/void"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("voided")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "AP_VIEW", "AP_STATEMENT_VIEW"
    })
    public void testCompanyIsolation() throws Exception {
        Company company2 = new Company();
        company2.setCode("COMPANY_ISO_TEST");
        company2.setName("Company Isolation Test");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        // Dashboard for company2 should have zero payables
        mockMvc.perform(get("/api/v1/ap/dashboard")
                        .param("companyId", company2.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPayables").value(0.00));

        // Getting statement for supplier with company2 should throw exception (company mismatch)
        mockMvc.perform(get("/api/v1/ap/suppliers/" + supplier.getId() + "/statement")
                        .param("companyId", company2.getId().toString())
                        .param("from", LocalDate.now().toString())
                        .param("to", LocalDate.now().toString()))
                .andExpect(status().isNotFound());
    }
}
