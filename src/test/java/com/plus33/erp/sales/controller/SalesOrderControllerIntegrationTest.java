package com.plus33.erp.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.repository.CustomerRepository;
import com.plus33.erp.sales.repository.SalesOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SalesOrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company globalCompany;
    private Company secondaryCompany;
    private Customer activeCustomer;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        globalCompany = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        Company secondary = new Company();
        secondary.setCode("SEC_COMP_TEST");
        secondary.setName("Secondary Test Company");
        secondary.setActive(true);
        secondaryCompany = companyRepository.save(secondary);

        // Create an active customer in PLUS33_GLOBAL
        activeCustomer = customerRepository.save(Customer.builder()
                .company(globalCompany)
                .code("CUST_TEST_SO_01")
                .name("Acme Order Customer")
                .customerType(CustomerType.B2B)
                .status(CustomerStatus.ACTIVE)
                .billingAddress("123 Billing St")
                .shippingAddress("456 Shipping Ave")
                .pricingTier("WHOLESALE")
                .creditLimit(BigDecimal.valueOf(1000.00))
                .outstandingBalance(BigDecimal.ZERO)
                .discountRate(BigDecimal.valueOf(10.00)) // 10%
                .taxProfile(TaxProfile.STANDARD)
                .paymentTermsDays(30)
                .currencyCode("AED")
                .build());

        // Retrieve seeded products
        List<Product> products = productRepository.findAll();
        product1 = products.stream()
                .filter(p -> p.getCode().equals("RAW-ARA-001"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("RAW-ARA-001 product not found"));

        product2 = products.stream()
                .filter(p -> p.getCode().equals("RAW-MLK-001"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("RAW-MLK-001 product not found"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "SALES_ORDER_CREATE", "SALES_ORDER_VIEW", "SALES_ORDER_UPDATE",
            "SALES_ORDER_SUBMIT", "SALES_ORDER_APPROVE", "SALES_ORDER_CANCEL"
    })
    public void testSalesOrderWorkflowAndCalculations() throws Exception {
        UUID clientRef = UUID.randomUUID();

        // 1. Create Sales Order Request
        // Item 1: Arabica Beans (price = 100, qty = 2, discount = 10%, tax = 5%)
        // Subtotal = 2 * 100 = 200
        // Discount = 200 * 10% = 20
        // Net = 180
        // Tax = 180 * 5% = 9
        // Line total = 180 + 9 = 189
        SalesOrderItemRequest item1 = new SalesOrderItemRequest(
                product1.getId(),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(5.00)
        );

        SalesOrderRequest createReq = new SalesOrderRequest(
                globalCompany.getId(),
                activeCustomer.getId(),
                clientRef,
                LocalDate.now().plusDays(5),
                List.of(item1)
        );

        String responseContent = mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderNumber").value(org.hamcrest.Matchers.startsWith("SO-")))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.subtotal").value(200.00))
                .andExpect(jsonPath("$.data.discountAmount").value(20.00))
                .andExpect(jsonPath("$.data.taxAmount").value(9.00))
                .andExpect(jsonPath("$.data.totalAmount").value(189.00))
                .andExpect(jsonPath("$.data.outstandingAmount").value(189.00))
                .andExpect(jsonPath("$.data.customerName").value("Acme Order Customer"))
                .andExpect(jsonPath("$.data.customerCode").value("CUST_TEST_SO_01"))
                .andExpect(jsonPath("$.data.billingAddress").value("123 Billing St"))
                .andReturn().getResponse().getContentAsString();

        SalesOrderResponse createdOrder = objectMapper.readValue(
                objectMapper.readTree(responseContent).get("data").toString(),
                SalesOrderResponse.class
        );

        // 2. Test Idempotency retry with same clientRef
        mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(createdOrder.id()))
                .andExpect(jsonPath("$.data.clientReferenceId").value(clientRef.toString()));

        // 3. Test update draft order
        SalesOrderItemRequest itemUpdate = new SalesOrderItemRequest(
                product1.getId(),
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(5.00)
        ); // Subtotal=300, Discount=30, Net=270, Tax=13.5, Total=283.5

        SalesOrderRequest updateReq = new SalesOrderRequest(
                globalCompany.getId(),
                activeCustomer.getId(),
                clientRef,
                LocalDate.now().plusDays(6),
                List.of(itemUpdate)
        );

        mockMvc.perform(put("/api/v1/sales-orders/" + createdOrder.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalAmount").value(283.50))
                .andExpect(jsonPath("$.data.requestedDeliveryDate").value(LocalDate.now().plusDays(6).toString()));

        // 4. Submit Order
        mockMvc.perform(post("/api/v1/sales-orders/" + createdOrder.id() + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data.submittedByUserName").value("System"))
                .andExpect(jsonPath("$.data.submittedAt").isNotEmpty());

        // 5. Approve Order
        BigDecimal customerBalanceBefore = customerRepository.findById(activeCustomer.getId()).get().getOutstandingBalance();

        mockMvc.perform(post("/api/v1/sales-orders/" + createdOrder.id() + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.approvedByUserName").value("System"))
                .andExpect(jsonPath("$.data.approvedAt").isNotEmpty());

        BigDecimal customerBalanceAfter = customerRepository.findById(activeCustomer.getId()).get().getOutstandingBalance();
        assertEquals(0, customerBalanceBefore.add(BigDecimal.valueOf(283.50)).compareTo(customerBalanceAfter));

        // 6. Blocker on updating approved orders
        mockMvc.perform(put("/api/v1/sales-orders/" + createdOrder.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Only orders in DRAFT status can be updated"));

        // 7. Cancel order and verify balance decrement
        mockMvc.perform(post("/api/v1/sales-orders/" + createdOrder.id() + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SalesOrderCancelRequest("Customer requested refund"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancelledByUserName").value("System"))
                .andExpect(jsonPath("$.data.cancellationReason").value("Customer requested refund"));

        BigDecimal customerBalanceAfterCancel = customerRepository.findById(activeCustomer.getId()).get().getOutstandingBalance();
        assertEquals(0, customerBalanceBefore.compareTo(customerBalanceAfterCancel));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"SALES_ORDER_CREATE"})
    public void testCrossCompanyValidation() throws Exception {
        // Customer is globalCompany. Request specifies secondaryCompany.
        SalesOrderItemRequest item1 = new SalesOrderItemRequest(
                product1.getId(), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO
        );

        SalesOrderRequest crossReq = new SalesOrderRequest(
                secondaryCompany.getId(),
                activeCustomer.getId(),
                UUID.randomUUID(),
                null,
                List.of(item1)
        );

        mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crossReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer does not belong to the requested company"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"SALES_ORDER_CREATE"})
    public void testDuplicateProductLinesRejected() throws Exception {
        SalesOrderItemRequest item1 = new SalesOrderItemRequest(
                product1.getId(), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO
        );
        SalesOrderItemRequest item2 = new SalesOrderItemRequest(
                product1.getId(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO
        ); // Duplicate product1

        SalesOrderRequest dupReq = new SalesOrderRequest(
                globalCompany.getId(),
                activeCustomer.getId(),
                UUID.randomUUID(),
                null,
                List.of(item1, item2)
        );

        mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Duplicate product lines detected for product ID: " + product1.getId()));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "SALES_ORDER_CREATE", "SALES_ORDER_SUBMIT", "SALES_ORDER_APPROVE"
    })
    public void testCreditLimitValidationWithoutOverride() throws Exception {
        // Customer has 1000 credit limit. Create order of 1500.
        SalesOrderItemRequest item1 = new SalesOrderItemRequest(
                product1.getId(),
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(100.00),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ); // total = 1500

        SalesOrderRequest createReq = new SalesOrderRequest(
                globalCompany.getId(),
                activeCustomer.getId(),
                UUID.randomUUID(),
                null,
                List.of(item1)
        );

        String responseContent = mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long orderId = objectMapper.readTree(responseContent).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/sales-orders/" + orderId + "/submit"))
                .andExpect(status().isOk());

        // Attempt approval without override permission
        mockMvc.perform(post("/api/v1/sales-orders/" + orderId + "/approve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credit limit exceeded and user does not have permission to override"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "SALES_ORDER_CREATE", "SALES_ORDER_SUBMIT", "SALES_ORDER_APPROVE", "SALES_ORDER_OVERRIDE_CREDIT_LIMIT"
    })
    public void testCreditLimitValidationWithOverride() throws Exception {
        // Customer has 1000 credit limit. Create order of 1500.
        SalesOrderItemRequest item1 = new SalesOrderItemRequest(
                product1.getId(),
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(100.00),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ); // total = 1500

        SalesOrderRequest createReq = new SalesOrderRequest(
                globalCompany.getId(),
                activeCustomer.getId(),
                UUID.randomUUID(),
                null,
                List.of(item1)
        );

        String responseContent = mockMvc.perform(post("/api/v1/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long orderId = objectMapper.readTree(responseContent).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/sales-orders/" + orderId + "/submit"))
                .andExpect(status().isOk());

        // Approve with override permission
        mockMvc.perform(post("/api/v1/sales-orders/" + orderId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.creditOverride").value(true));
    }
}
