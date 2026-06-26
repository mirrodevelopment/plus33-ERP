package com.plus33.erp.ar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.ar.dto.*;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.repository.CustomerInvoiceRepository;
import com.plus33.erp.sales.repository.CustomerRepository;
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
import org.springframework.test.web.servlet.MvcResult;
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
public class ARControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company company;
    private Customer customer;
    private CustomerInvoice invoice;

    @BeforeEach
    public void setUp() {
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        customer = customerRepository.save(Customer.builder()
                .company(company)
                .code("CUST_TEST_AR_01")
                .name("Acme AR Customer")
                .customerType(CustomerType.B2B)
                .status(CustomerStatus.ACTIVE)
                .billingAddress("123 Billing St")
                .shippingAddress("456 Shipping Ave")
                .pricingTier("WHOLESALE")
                .creditLimit(BigDecimal.valueOf(15000.00))
                .outstandingBalance(BigDecimal.valueOf(1050.00))
                .discountRate(BigDecimal.ZERO)
                .taxProfile(TaxProfile.STANDARD)
                .paymentTermsDays(30)
                .currencyCode("AED")
                .build());

        // Seed finance accounts if missing
        seedAccount(company, "1400", "Accounts Receivable", "ASSET");
        seedAccount(company, "5300", "Bad Debt Expense", "EXPENSE");

        User adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // Create an Approved Customer Invoice that is overdue (due date in the past)
        invoice = customerInvoiceRepository.save(CustomerInvoice.builder()
                .company(company)
                .customer(customer)
                .invoiceNumber("INV-TEST-AR-01")
                .clientReferenceId(UUID.randomUUID())
                .invoiceDate(LocalDate.now().minusDays(40))
                .dueDate(LocalDate.now().minusDays(10)) // Overdue by 10 days
                .subtotalAmount(BigDecimal.valueOf(1000.00))
                .taxAmount(BigDecimal.valueOf(50.00))
                .totalAmount(BigDecimal.valueOf(1050.00))
                .outstandingBalance(BigDecimal.valueOf(1050.00))
                .status(CustomerInvoiceStatus.APPROVED)
                .createdBy(adminUser)
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

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "AR_VIEW", "AR_STATEMENT_VIEW", "AR_WRITE_OFF_CREATE"
    })
    public void testARModuleWorkflow() throws Exception {
        // 1. Get Customer AR Balance
        mockMvc.perform(get("/api/v1/ar/customers/" + customer.getId() + "/balance")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.data.totalOutstanding").value(1050.00))
                .andExpect(jsonPath("$.data.totalOverdue").value(1050.00));

        // 2. Get Overdue Invoices
        mockMvc.perform(get("/api/v1/ar/overdue")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].invoiceId").value(invoice.getId()))
                .andExpect(jsonPath("$.data.content[0].outstandingBalance").value(1050.00));

        // 3. Get Customer Statement (before write-off)
        mockMvc.perform(get("/api/v1/ar/customers/" + customer.getId() + "/statement")
                        .param("companyId", company.getId().toString())
                        .param("from", LocalDate.now().minusDays(50).toString())
                        .param("to", LocalDate.now().plusDays(10).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.data.entries.length()").value(1))
                .andExpect(jsonPath("$.data.entries[0].referenceNumber").value(invoice.getInvoiceNumber()))
                .andExpect(jsonPath("$.data.entries[0].debitAmount").value(1050.00));

        // 4. Create bad-debt write-off
        ARWriteOffRequest writeOffReq = new ARWriteOffRequest(
                company.getId(),
                invoice.getId(),
                BigDecimal.valueOf(550.00),
                LocalDate.now(),
                "Partial bad debt write off"
        );

        MvcResult woResult = mockMvc.perform(post("/api/v1/ar/write-offs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeOffReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.writeOffAmount").value(550.00))
                .andExpect(jsonPath("$.data.invoiceNumber").value(invoice.getInvoiceNumber()))
                .andReturn();

        ApiResponse woResponse = objectMapper.readValue(woResult.getResponse().getContentAsString(), ApiResponse.class);
        Map<String, Object> woMap = (Map<String, Object>) woResponse.data();
        Long writeOffId = ((Number) woMap.get("id")).longValue();

        // 5. Get write-off by ID
        mockMvc.perform(get("/api/v1/ar/write-offs/" + writeOffId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(writeOffId))
                .andExpect(jsonPath("$.data.writeOffAmount").value(550.00));

        // 6. Search write-offs
        mockMvc.perform(get("/api/v1/ar/write-offs")
                        .param("companyId", company.getId().toString())
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(writeOffId));

        // 7. Verify customer balance and invoice outstanding balance are updated
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        assertEquals(0, BigDecimal.valueOf(500.00).compareTo(updatedCustomer.getOutstandingBalance()));

        CustomerInvoice updatedInvoice = customerInvoiceRepository.findById(invoice.getId()).orElseThrow();
        assertEquals(0, BigDecimal.valueOf(500.00).compareTo(updatedInvoice.getOutstandingBalance()));
        assertEquals(CustomerInvoiceStatus.APPROVED, updatedInvoice.getStatus());

        // 8. Get Customer Statement (after write-off)
        mockMvc.perform(get("/api/v1/ar/customers/" + customer.getId() + "/statement")
                        .param("companyId", company.getId().toString())
                        .param("from", LocalDate.now().minusDays(50).toString())
                        .param("to", LocalDate.now().plusDays(10).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.entries.length()").value(2))
                .andExpect(jsonPath("$.data.entries[1].entryType").value("WRITE_OFF"))
                .andExpect(jsonPath("$.data.entries[1].creditAmount").value(550.00))
                .andExpect(jsonPath("$.data.entries[1].runningBalance").value(500.00));
    }
}
