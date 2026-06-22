package com.plus33.erp.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.entity.TaxProfile;
import com.plus33.erp.sales.repository.CustomerRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company globalCompany;

    @BeforeEach
    public void setUp() {
        globalCompany = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        // Clean up test customers if any exist on the database outside transaction
        Optional<Customer> c1 = customerRepository.findByCompanyIdAndCode(globalCompany.getId(), "CUST_TEST_001");
        c1.ifPresent(customerRepository::delete);
        Optional<Customer> c2 = customerRepository.findByCompanyIdAndCode(globalCompany.getId(), "CUST_TEST_002");
        c2.ifPresent(customerRepository::delete);
        customerRepository.flush();
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "CUSTOMER_CREATE", "CUSTOMER_VIEW", "CUSTOMER_UPDATE", "CUSTOMER_DELETE",
            "CUSTOMER_ACTIVATE", "CUSTOMER_DEACTIVATE"
    })
    public void testCustomerCrudOperations() throws Exception {
        // 1. Create a customer with a specified code
        CustomerRequest createReq1 = new CustomerRequest(
                globalCompany.getId(),
                "CUST_TEST_001",
                "Acme Corporation",
                CustomerType.B2B,
                CustomerStatus.ACTIVE,
                "John Doe",
                "john@acme.com",
                "+971500000000",
                "123 Billing St, Dubai",
                "456 Shipping Ave, Jebel Ali",
                "TRN12345678",
                TaxProfile.STANDARD,
                BigDecimal.valueOf(50000.00),
                BigDecimal.ZERO,
                "WHOLESALE",
                BigDecimal.valueOf(10.00),
                30,
                "AED"
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("CUST_TEST_001"))
                .andExpect(jsonPath("$.data.name").value("Acme Corporation"))
                .andExpect(jsonPath("$.data.pricingTier").value("WHOLESALE"))
                .andExpect(jsonPath("$.data.creditLimit").value(50000.00))
                .andExpect(jsonPath("$.data.currencyCode").value("AED"))
                .andExpect(jsonPath("$.data.paymentTermsDays").value(30));

        // 2. Create customer with auto-generated code
        CustomerRequest createReq2 = new CustomerRequest(
                globalCompany.getId(),
                null, // Trigger auto-generation
                "Jane Doe B2C",
                CustomerType.B2C,
                CustomerStatus.ACTIVE,
                "Jane Doe",
                "jane@doe.com",
                "+971501111111",
                "789 Apartment St, Dubai",
                "789 Apartment St, Dubai",
                null,
                TaxProfile.EXEMPT,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "RETAIL",
                BigDecimal.ZERO,
                0,
                "INR"
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value(org.hamcrest.Matchers.startsWith("CUST-")))
                .andExpect(jsonPath("$.data.name").value("Jane Doe B2C"))
                .andExpect(jsonPath("$.data.currencyCode").value("INR"));

        // 3. Test uniqueness constraint on code within same company
        CustomerRequest dupCodeReq = new CustomerRequest(
                globalCompany.getId(),
                "CUST_TEST_001", // Duplicate code
                "Acme Corp Duplicate",
                CustomerType.B2B,
                CustomerStatus.ACTIVE,
                null, null, null, null, null, null, null,
                BigDecimal.ZERO, BigDecimal.ZERO, "RETAIL", BigDecimal.ZERO, 0, "INR"
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupCodeReq)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Customer code CUST_TEST_001 already exists in this company"));

        // 4. Test uniqueness constraint on email within same company
        CustomerRequest dupEmailReq = new CustomerRequest(
                globalCompany.getId(),
                "CUST_TEST_002",
                "Acme Corp Email Duplicate",
                CustomerType.B2B,
                CustomerStatus.ACTIVE,
                "Jane Doe",
                "john@acme.com", // Duplicate email
                null, null, null, null, null,
                BigDecimal.ZERO, BigDecimal.ZERO, "RETAIL", BigDecimal.ZERO, 0, "INR"
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupEmailReq)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Customer email john@acme.com already exists in this company"));

        // 5. Test search and pagination
        mockMvc.perform(get("/api/v1/customers")
                        .param("companyId", globalCompany.getId().toString())
                        .param("query", "Acme")
                        .param("customerType", "B2B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Acme Corporation"));

        // 6. Test update customer details
        Customer customer = customerRepository.findByCompanyIdAndCode(globalCompany.getId(), "CUST_TEST_001")
                .orElseThrow(() -> new AssertionError("Customer not found"));

        CustomerRequest updateReq = new CustomerRequest(
                globalCompany.getId(),
                "CUST_TEST_001",
                "Acme Corporation Ltd", // Updated Name
                CustomerType.B2B,
                CustomerStatus.ON_HOLD, // Updated Status
                "John Doe Jr",
                "john.jr@acme.com",
                "+971500000000",
                "123 Billing St, Dubai",
                "456 Shipping Ave, Jebel Ali",
                "TRN12345678",
                TaxProfile.STANDARD,
                BigDecimal.valueOf(100000.00), // Updated credit limit
                BigDecimal.ZERO,
                "WHOLESALE",
                BigDecimal.valueOf(15.00), // Updated discount
                45, // Updated terms
                "AED"
        );

        mockMvc.perform(put("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Acme Corporation Ltd"))
                .andExpect(jsonPath("$.data.status").value("ON_HOLD"))
                .andExpect(jsonPath("$.data.creditLimit").value(100000.00))
                .andExpect(jsonPath("$.data.discountRate").value(15.00))
                .andExpect(jsonPath("$.data.paymentTermsDays").value(45));

        // 7. Test deactivation
        mockMvc.perform(patch("/api/v1/customers/" + customer.getId() + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));

        // 8. Test update soft-deleted blocker
        mockMvc.perform(put("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Soft-deleted customers cannot be updated"));

        // 9. Test activation
        mockMvc.perform(patch("/api/v1/customers/" + customer.getId() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        // 10. Test soft deletion via DELETE
        mockMvc.perform(delete("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isOk());

        Customer softDeleted = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new AssertionError("Customer was hard deleted"));
        assertEquals(CustomerStatus.INACTIVE, softDeleted.getStatus());
    }
}
