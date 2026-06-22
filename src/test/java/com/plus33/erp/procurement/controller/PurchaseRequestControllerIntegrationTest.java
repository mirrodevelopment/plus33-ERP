package com.plus33.erp.procurement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.PurchaseRequestRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PurchaseRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PURCHASE_REQUEST_CREATE", "PURCHASE_REQUEST_VIEW", "PURCHASE_REQUEST_UPDATE",
            "PURCHASE_REQUEST_SUBMIT", "PURCHASE_REQUEST_APPROVE", "PURCHASE_REQUEST_REJECT",
            "PURCHASE_REQUEST_CANCEL", "PURCHASE_REQUEST_CONVERT"
    })
    public void testPurchaseRequestWorkflowAndValidations() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findByCode("UAE_REGION")
                .orElseThrow(() -> new AssertionError("UAE_REGION not found"));

        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        Store store = storeRepository.findByCode("DUBAI_MALL_STORE")
                .orElseThrow(() -> new AssertionError("DUBAI_MALL_STORE not found"));

        // 1. Get or create Supplier under PLUS33_GLOBAL
        Supplier supplier = supplierRepository.findAll().stream()
                .filter(s -> s.getCompany().getId().equals(company.getId()) && s.getActive())
                .findFirst()
                .orElseGet(() -> {
                    Supplier s = new Supplier();
                    s.setCode("TEST_SUPP");
                    s.setName("Test Supplier");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // 2. Get or create Product
        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // Test Validation: Past Date
        PurchaseRequestRequest pastDateRequest = new PurchaseRequestRequest(
                company.getId(), supplier.getId(), warehouse.getId(), null,
                LocalDate.now().minusDays(1), "Notes",
                List.of(new PurchaseRequestItemRequest(product.getId(), BigDecimal.TEN, "PCS", "Remarks")));

        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pastDateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Required date cannot be in the past"));

        // Test Validation: XOR violation (both destination fields set)
        PurchaseRequestRequest xorRequest = new PurchaseRequestRequest(
                company.getId(), supplier.getId(), warehouse.getId(), store.getId(),
                LocalDate.now().plusDays(5), "Notes",
                List.of(new PurchaseRequestItemRequest(product.getId(), BigDecimal.TEN, "PCS", "Remarks")));

        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(xorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only one destination type is allowed: must specify either warehouseId or storeId"));

        // Test Validation: Destination-company mismatch
        Company company2 = new Company();
        company2.setCode("COMP_TEST_PO_" + System.nanoTime());
        company2.setName("Company Test PO");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        Region region2 = new Region();
        region2.setCode("REG_TEST_PO_" + System.nanoTime());
        region2.setName("Region Test PO");
        region2.setCompany(company2);
        region2 = regionRepository.save(region2);

        Store store2 = new Store();
        store2.setCode("STORE_TEST_PO_" + System.nanoTime());
        store2.setName("Store Test PO");
        store2.setRegion(region2);
        store2.setActive(true);
        store2 = storeRepository.save(store2);

        PurchaseRequestRequest companyMismatchRequest = new PurchaseRequestRequest(
                company.getId(), supplier.getId(), null, store2.getId(),
                LocalDate.now().plusDays(5), "Notes",
                List.of(new PurchaseRequestItemRequest(product.getId(), BigDecimal.TEN, "PCS", "Remarks")));

        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyMismatchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Store does not belong to the selected company"));

        // 3. Create a valid Draft Purchase Request
        PurchaseRequestRequest validRequest = new PurchaseRequestRequest(
                company.getId(), supplier.getId(), warehouse.getId(), null,
                LocalDate.now().plusDays(5), "Standard refill",
                List.of(new PurchaseRequestItemRequest(product.getId(), BigDecimal.TEN, "PCS", "Refill line")));

        String createdJson = mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.requestNumber").value(org.hamcrest.Matchers.startsWith("PR-")))
                .andReturn().getResponse().getContentAsString();

        Long prId = objectMapper.readTree(createdJson).path("data").path("id").asLong();

        // Test transition guard: Approve Draft Request (should fail)
        mockMvc.perform(post("/api/v1/purchase-requests/" + prId + "/approve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Unauthorized transitions: Cannot approve Purchase Request from DRAFT status"));

        // 4. Submit Draft Request
        mockMvc.perform(post("/api/v1/purchase-requests/" + prId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data.submittedAt").isNotEmpty());

        // Test immutability: Cannot update a submitted request
        mockMvc.perform(put("/api/v1/purchase-requests/" + prId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Terminal states cannot be modified: Purchase Request is in SUBMITTED status"));

        // 5. Approve Request
        mockMvc.perform(post("/api/v1/purchase-requests/" + prId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.approvedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.items[0].approvedQuantity").value(10.0));

        // 6. Convert Approved Request to PO
        String poResponseJson = mockMvc.perform(post("/api/v1/purchase-requests/" + prId + "/convert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CONVERTED_TO_PO"))
                .andExpect(jsonPath("$.data.convertedToPoAt").isNotEmpty())
                .andExpect(jsonPath("$.data.purchaseOrderId").isNotEmpty())
                .andExpect(jsonPath("$.data.purchaseOrderNumber").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        // 7. Double Conversion Guard
        mockMvc.perform(post("/api/v1/purchase-requests/" + prId + "/convert"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Double conversion attempts")));
    }
}
