package com.plus33.erp.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.dto.PaymentAllocationRequest;
import com.plus33.erp.finance.dto.PaymentCancelRequest;
import com.plus33.erp.finance.dto.PaymentRequest;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CustomerInvoiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long companyId;
    private Long customerId;

    @BeforeEach
    public void setUp() {
        // Fetch or seed default company
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setCode("PLUS33_GLOBAL");
                    newCompany.setName("Plus33 Global");
                    newCompany.setActive(true);
                    return companyRepository.save(newCompany);
                });
        companyId = company.getId();

        // Seed accounts if missing
        seedAccount(company, "1000", "Assets", "ASSET", null);
        seedAccount(company, "1400", "Accounts Receivable", "ASSET", "1000");
        seedAccount(company, "1100", "Cash", "ASSET", "1000");
        seedAccount(company, "1200", "Bank", "ASSET", "1000");
        seedAccount(company, "2000", "Liabilities", "LIABILITY", null);
        seedAccount(company, "2200", "Tax Payable", "LIABILITY", "2000");
        seedAccount(company, "4000", "Revenue", "REVENUE", null);

        // Fetch or seed default customer
        Customer customer = customerRepository.findByCompanyIdAndCode(companyId, "CUST-TEST-INV")
                .orElseGet(() -> customerRepository.save(Customer.builder()
                        .company(company)
                        .code("CUST-TEST-INV")
                        .name("Test Invoice Customer")
                        .customerType(com.plus33.erp.sales.entity.CustomerType.B2B)
                        .status(CustomerStatus.ACTIVE)
                        .billingAddress("Billing Rd 10")
                        .shippingAddress("Shipping Rd 10")
                        .taxProfile(com.plus33.erp.sales.entity.TaxProfile.STANDARD)
                        .outstandingBalance(BigDecimal.ZERO)
                        .creditLimit(BigDecimal.valueOf(50000.00))
                        .currencyCode("AED")
                        .build()));
        customerId = customer.getId();
    }

    private void seedAccount(Company company, String code, String name, String type, String parentCode) {
        if (accountRepository.findByCompanyIdAndAccountCode(company.getId(), code).isEmpty()) {
            Account parent = null;
            if (parentCode != null) {
                parent = accountRepository.findByCompanyIdAndAccountCode(company.getId(), parentCode).orElse(null);
            }
            Account newAccount = new Account();
            newAccount.setCompany(company);
            newAccount.setAccountCode(code);
            newAccount.setAccountName(name);
            newAccount.setAccountType(type);
            newAccount.setParentAccount(parent);
            newAccount.setActive(true);
            accountRepository.save(newAccount);
        }
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "CUSTOMER_INVOICE_CREATE", "CUSTOMER_INVOICE_VIEW", "CUSTOMER_INVOICE_UPDATE",
            "CUSTOMER_INVOICE_SUBMIT", "CUSTOMER_INVOICE_APPROVE", "CUSTOMER_INVOICE_CANCEL",
            "SUPPLIER_INVOICE_CREATE", "SUPPLIER_INVOICE_VIEW", "SUPPLIER_INVOICE_UPDATE",
            "PAYMENT_CREATE", "PAYMENT_VIEW", "PAYMENT_CANCEL"
    })
    public void testCustomerInvoiceLifecycleAndPaymentAllocation() throws Exception {
        // 1. Create Ad-hoc Customer Invoice in DRAFT
        UUID clientRef = UUID.randomUUID();
        CustomerInvoiceItemRequest itemReq = new CustomerInvoiceItemRequest(
                null, null, 1L, BigDecimal.valueOf(2.00), BigDecimal.valueOf(100.00), BigDecimal.ZERO, BigDecimal.valueOf(5.00)
        );

        CustomerInvoiceRequest createReq = new CustomerInvoiceRequest(
                companyId, customerId, null, clientRef, LocalDate.now(), LocalDate.now().plusDays(30), "AED", List.of(itemReq)
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/customer-invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.subtotalAmount").value(200.00))
                .andExpect(jsonPath("$.data.taxAmount").value(10.00))
                .andExpect(jsonPath("$.data.totalAmount").value(210.00))
                .andExpect(jsonPath("$.data.outstandingBalance").value(210.00))
                .andReturn();

        ApiResponse createResponse = objectMapper.readValue(createResult.getResponse().getContentAsString(), ApiResponse.class);
        Map<String, Object> invoiceMap = (Map<String, Object>) createResponse.data();
        Long invoiceId = ((Number) invoiceMap.get("id")).longValue();

        // 2. Submit Customer Invoice
        mockMvc.perform(post("/api/v1/customer-invoices/" + invoiceId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        // Verify Customer Balance before approval is still 0
        Customer customerBefore = customerRepository.findById(customerId).get();
        assertEquals(0, BigDecimal.ZERO.compareTo(customerBefore.getOutstandingBalance()));

        // 3. Approve Customer Invoice (Posts to Ledger, Increases outstanding balance since ad-hoc)
        mockMvc.perform(post("/api/v1/customer-invoices/" + invoiceId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.journalEntryId").value(org.hamcrest.Matchers.notNullValue()));

        // Verify Customer Outstanding Balance is now 210.00
        Customer customerAfterApprove = customerRepository.findById(customerId).get();
        assertEquals(0, BigDecimal.valueOf(210.00).compareTo(customerAfterApprove.getOutstandingBalance()));

        // 4. Create Customer Receivable Payment and Allocate
        PaymentAllocationRequest allocationReq = new PaymentAllocationRequest(null, invoiceId, BigDecimal.valueOf(210.00));
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .companyId(companyId)
                .customerId(customerId)
                .paymentDate(LocalDate.now())
                .paymentMethod("BANK_TRANSFER")
                .amount(BigDecimal.valueOf(210.00))
                .currencyCode("AED")
                .allocations(List.of(allocationReq))
                .build();

        MvcResult paymentResult = mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse paymentResponse = objectMapper.readValue(paymentResult.getResponse().getContentAsString(), ApiResponse.class);
        Map<String, Object> payMap = (Map<String, Object>) paymentResponse.data();
        Long paymentId = ((Number) payMap.get("id")).longValue();

        // Verify Invoice is now PAID
        mockMvc.perform(get("/api/v1/customer-invoices/" + invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PAID"))
                .andExpect(jsonPath("$.data.paidAmount").value(210.00))
                .andExpect(jsonPath("$.data.outstandingBalance").value(0.00));

        // Verify Customer Outstanding Balance is reduced back to 0
        Customer customerAfterPayment = customerRepository.findById(customerId).get();
        assertEquals(0, BigDecimal.ZERO.compareTo(customerAfterPayment.getOutstandingBalance()));

        // 5. Cancel/Reverse Payment
        PaymentCancelRequest cancelRequest = new PaymentCancelRequest("Reversal of payment");
        mockMvc.perform(post("/api/v1/payments/" + paymentId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk());

        // Verify Invoice transitions back to APPROVED
        mockMvc.perform(get("/api/v1/customer-invoices/" + invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.paidAmount").value(0.00))
                .andExpect(jsonPath("$.data.outstandingBalance").value(210.00));

        // Verify Customer Outstanding Balance goes back to 210.00
        Customer customerAfterCancel = customerRepository.findById(customerId).get();
        assertEquals(0, BigDecimal.valueOf(210.00).compareTo(customerAfterCancel.getOutstandingBalance()));

        // 6. Cancel Customer Invoice (Reverses general ledger postings)
        mockMvc.perform(post("/api/v1/customer-invoices/" + invoiceId + "/cancel?reason=Cancelled by admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));

        // Verify Customer Outstanding Balance goes back to 0
        Customer customerAfterInvoiceCancel = customerRepository.findById(customerId).get();
        assertEquals(0, BigDecimal.ZERO.compareTo(customerAfterInvoiceCancel.getOutstandingBalance()));
    }
}
