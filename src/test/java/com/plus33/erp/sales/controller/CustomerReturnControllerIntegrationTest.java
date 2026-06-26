package com.plus33.erp.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.repository.CustomerInvoiceItemRepository;
import com.plus33.erp.sales.repository.CustomerInvoiceRepository;
import com.plus33.erp.sales.repository.CustomerRepository;
import com.plus33.erp.sales.repository.CustomerReturnRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;

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
public class CustomerReturnControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private CustomerInvoiceItemRepository customerInvoiceItemRepository;

    @Autowired
    private CustomerReturnRepository customerReturnRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;



    private Company company;
    private Customer customer;
    private Product product;
    private Warehouse warehouse;
    private CustomerInvoice invoice;
    private CustomerInvoiceItem invoiceItem;

    @BeforeEach
    public void setUp() {

        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        product = productRepository.findAll().stream()
                .filter(p -> p.getCode().equals("RAW-ARA-001"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("RAW-ARA-001 product not found"));

        warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        customer = customerRepository.save(Customer.builder()
                .company(company)
                .code("CUST_TEST_RET_01")
                .name("Acme Return Customer")
                .customerType(CustomerType.B2B)
                .status(CustomerStatus.ACTIVE)
                .billingAddress("123 Billing St")
                .shippingAddress("456 Shipping Ave")
                .pricingTier("WHOLESALE")
                .creditLimit(BigDecimal.valueOf(10000.00))
                .outstandingBalance(BigDecimal.valueOf(1000.00))
                .discountRate(BigDecimal.ZERO)
                .taxProfile(TaxProfile.STANDARD)
                .paymentTermsDays(30)
                .currencyCode("AED")
                .build());

        // Seed finance accounts if missing
        seedAccount(company, "1400", "Accounts Receivable", "ASSET");
        seedAccount(company, "2200", "Tax Payable", "LIABILITY");
        seedAccount(company, "4000", "Revenue", "REVENUE");

        User adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // Create an Approved Customer Invoice for 5 units
        invoice = CustomerInvoice.builder()
                .company(company)
                .customer(customer)
                .invoiceNumber("INV-TEST-RET-01")
                .clientReferenceId(UUID.randomUUID())
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .subtotalAmount(BigDecimal.valueOf(500.00))
                .taxAmount(BigDecimal.valueOf(25.00))
                .totalAmount(BigDecimal.valueOf(525.00))
                .outstandingBalance(BigDecimal.valueOf(525.00))
                .status(CustomerInvoiceStatus.APPROVED)
                .createdBy(adminUser)
                .build();

        invoiceItem = CustomerInvoiceItem.builder()
                .customerInvoice(invoice)
                .product(product)
                .quantity(BigDecimal.valueOf(5.00))
                .unitPrice(BigDecimal.valueOf(100.00))
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.valueOf(5.00))
                .netAmount(BigDecimal.valueOf(500.00))
                .taxAmount(BigDecimal.valueOf(25.00))
                .totalAmount(BigDecimal.valueOf(525.00))
                .build();

        invoice.getItems().add(invoiceItem);
        customerInvoiceRepository.save(invoice);
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
            "CUSTOMER_RETURN_CREATE", "CUSTOMER_RETURN_VIEW", "CUSTOMER_RETURN_UPDATE",
            "CUSTOMER_RETURN_APPROVE", "CUSTOMER_RETURN_RECEIVE", "CUSTOMER_RETURN_INSPECT",
            "CUSTOMER_RETURN_CLOSE", "CUSTOMER_RETURN_CANCEL", "CREDIT_NOTE_VIEW"
    })
    public void testCustomerReturnFullLifecycle() throws Exception {
        // 1. Create Customer Return Request in RETURN_REQUESTED
        UUID clientRef = UUID.randomUUID();
        CustomerReturnItemRequest itemReq = new CustomerReturnItemRequest(
                null, invoiceItem.getId(), product.getId(), BigDecimal.valueOf(2.00), null, null
        );

        CustomerReturnRequest createReq = new CustomerReturnRequest(
                company.getId(), customer.getId(), null, invoice.getId(), warehouse.getId(), null,
                clientRef, ReturnReason.DAMAGED, "Damaged items return request", List.of(itemReq)
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/customer-returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("RETURN_REQUESTED"))
                .andReturn();

        ApiResponse createResponse = objectMapper.readValue(createResult.getResponse().getContentAsString(), ApiResponse.class);
        Map<String, Object> returnMap = (Map<String, Object>) createResponse.data();
        Long returnId = ((Number) returnMap.get("id")).longValue();

        // Test Idempotency
        mockMvc.perform(post("/api/v1/customer-returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(returnId));

        // 2. Approve Return Request
        ReturnApprovalRequest approvalReq = new ReturnApprovalRequest("Approved by QA");
        mockMvc.perform(post("/api/v1/customer-returns/" + returnId + "/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvalReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        // 3. Receive Return
        mockMvc.perform(post("/api/v1/customer-returns/" + returnId + "/receive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RECEIVED"));

        // 4. Record Inspection Outcome (RESTOCK)
        InspectionRequest.ItemInspection itemInsp = new InspectionRequest.ItemInspection(
                product.getId(), com.plus33.erp.sales.entity.InspectionResult.RESTOCK, "Pristine condition restock"
        );
        InspectionRequest inspectReq = new InspectionRequest(List.of(itemInsp), "Inspection passes");

        mockMvc.perform(post("/api/v1/customer-returns/" + returnId + "/inspect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INSPECTED"));

        // 5. Close Return (Generates credit note, updates balance and invoice, posts GL)
        ReturnCloseRequest closeReq = new ReturnCloseRequest("Closing returned chairs");
        mockMvc.perform(post("/api/v1/customer-returns/" + returnId + "/close")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(closeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CLOSED"));

        // Assert customer balance was updated: Credit Note amount = 2 units * 100 AED + 5% tax = 210.00 AED
        // Customer outstanding balance drops from 0 to -210.00 since invoice outstanding was 525
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertEquals(0, BigDecimal.valueOf(790.00).compareTo(updatedCustomer.getOutstandingBalance()));

        // Assert invoice creditedAmount = 210.00, outstandingBalance = 315.00
        CustomerInvoice updatedInvoice = customerInvoiceRepository.findById(invoice.getId()).get();
        assertEquals(0, BigDecimal.valueOf(210.00).compareTo(updatedInvoice.getCreditedAmount()));
        assertEquals(0, BigDecimal.valueOf(315.00).compareTo(updatedInvoice.getOutstandingBalance()));
        assertEquals(CustomerInvoiceStatus.PARTIALLY_CREDITED, updatedInvoice.getStatus());

        // Assert invoice item returned quantity = 2.00
        CustomerInvoiceItem updatedItem = customerInvoiceItemRepository.findById(invoiceItem.getId()).get();
        assertEquals(0, BigDecimal.valueOf(2.00).compareTo(updatedItem.getReturnedQuantity()));
    }
}
