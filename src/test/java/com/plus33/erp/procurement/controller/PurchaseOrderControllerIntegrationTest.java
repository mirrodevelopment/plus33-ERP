package com.plus33.erp.procurement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.PurchaseRequestRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PurchaseOrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseRequest createApprovedPurchaseRequest(Company company, Supplier supplier) {
        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));
        PurchaseRequest pr = new PurchaseRequest();
        pr.setCompany(company);
        pr.setSupplier(supplier);
        pr.setWarehouse(warehouse);
        pr.setRequestNumber("PR-TEST-" + System.nanoTime());
        pr.setRequestedBy(userRepository.findAll().get(0));
        pr.setRequiredDate(LocalDate.now().plusDays(10));
        pr.setStatus(PurchaseRequestStatus.APPROVED);
        pr.setApprovedAt(LocalDateTime.now());
        return purchaseRequestRepository.save(pr);
    }

    private PurchaseRequest createDraftPurchaseRequest(Company company, Supplier supplier) {
        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));
        PurchaseRequest pr = new PurchaseRequest();
        pr.setCompany(company);
        pr.setSupplier(supplier);
        pr.setWarehouse(warehouse);
        pr.setRequestNumber("PR-TEST-DRAFT-" + System.nanoTime());
        pr.setRequestedBy(userRepository.findAll().get(0));
        pr.setRequiredDate(LocalDate.now().plusDays(10));
        pr.setStatus(PurchaseRequestStatus.DRAFT);
        return purchaseRequestRepository.save(pr);
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PURCHASE_ORDER_CREATE", "PURCHASE_ORDER_VIEW", "PURCHASE_ORDER_UPDATE",
            "PURCHASE_ORDER_APPROVE", "PURCHASE_ORDER_CANCEL", "PURCHASE_ORDER_CLOSE"
    })
    public void testPurchaseOrderWorkflowAndValidations() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        Supplier supplier = supplierRepository.findAll().stream()
                .filter(s -> s.getCompany().getId().equals(company.getId()) && s.getActive())
                .findFirst()
                .orElseGet(() -> {
                    Supplier s = new Supplier();
                    s.setCode("TEST_SUPP_PO_" + System.nanoTime());
                    s.setName("Test Supplier PO");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // Test Validation 1: Past Expected Delivery Date
        PurchaseOrderRequest pastDateRequest = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), null,
                LocalDate.now().minusDays(1), "Notes",
                BigDecimal.ZERO, BigDecimal.ZERO, "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(5.5), "Remarks"))
        );

        mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pastDateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Expected delivery date cannot be in the past"));

        // Test Validation 2: Link Purchase Request not APPROVED (e.g. DRAFT)
        PurchaseRequest draftPr = createDraftPurchaseRequest(company, supplier);
        PurchaseOrderRequest linkDraftPrRequest = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), draftPr.getId(),
                LocalDate.now().plusDays(5), "Notes",
                BigDecimal.ZERO, BigDecimal.ZERO, "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(5.5), "Remarks"))
        );

        mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDraftPrRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Purchase Request must be in APPROVED status"));

        // Test Validation 3: Link PR from different company
        Company company2 = new Company();
        company2.setCode("COMP_TEST_PO_2_" + System.nanoTime());
        company2.setName("Company Test PO 2");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        Supplier supplier2 = new Supplier();
        supplier2.setCode("SUPP_TEST_PO_2_" + System.nanoTime());
        supplier2.setName("Supplier Test PO 2");
        supplier2.setCompany(company2);
        supplier2.setActive(true);
        supplier2 = supplierRepository.save(supplier2);

        PurchaseRequest approvedPrDiffCompany = createApprovedPurchaseRequest(company2, supplier2);
        PurchaseOrderRequest linkDiffPrRequest = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), approvedPrDiffCompany.getId(),
                LocalDate.now().plusDays(5), "Notes",
                BigDecimal.ZERO, BigDecimal.ZERO, "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(5.5), "Remarks"))
        );

        mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDiffPrRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Purchase Request company must match Purchase Order company"));

        // Test Creation of a Valid Purchase Order
        PurchaseRequest approvedPr = createApprovedPurchaseRequest(company, supplier);
        PurchaseOrderRequest validRequest = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), approvedPr.getId(),
                LocalDate.now().plusDays(5), "Valid Draft Order",
                BigDecimal.valueOf(10.00), BigDecimal.valueOf(15.00), "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.valueOf(100), BigDecimal.valueOf(4.50), "Remarks"))
        );

        String createdJson = mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.orderNumber").value(org.hamcrest.Matchers.startsWith("PO-")))
                .andExpect(jsonPath("$.data.subtotalAmount").value(450.00)) // 100 * 4.50
                .andExpect(jsonPath("$.data.totalAmount").value(455.00)) // 450 + 15 (tax) - 10 (discount)
                .andReturn().getResponse().getContentAsString();

        Long poId = objectMapper.readTree(createdJson).path("data").path("id").asLong();

        // Test Validation 4: Double Linkage Guard
        PurchaseOrderRequest doubleLinkRequest = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), approvedPr.getId(),
                LocalDate.now().plusDays(5), "Duplicate PR Link Attempt",
                BigDecimal.ZERO, BigDecimal.ZERO, "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(5.5), "Remarks"))
        );

        mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doubleLinkRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Purchase Request is already linked to another Purchase Order"));

        // Test Validation 5: Cancel Draft PO (should fail, cancel is only allowed for ISSUED)
        mockMvc.perform(post("/api/v1/purchase-orders/" + poId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Cancelled"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only ISSUED purchase orders can be cancelled"));

        // Approve DRAFT PO -> transitions to ISSUED
        mockMvc.perform(post("/api/v1/purchase-orders/" + poId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ISSUED"))
                .andExpect(jsonPath("$.data.issuedAt").isNotEmpty());

        // Test Immutability: Cannot update after ISSUED
        mockMvc.perform(put("/api/v1/purchase-orders/" + poId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Terminal or non-draft states cannot be modified")));

        // Test Immutability: Cannot delete after ISSUED
        mockMvc.perform(delete("/api/v1/purchase-orders/" + poId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only DRAFT purchase orders can be deleted"));

        // Test Validation 6: Cancel after Partial Receipt (cancellation of PARTIALLY_RECEIVED should fail)
        // Manually update the status to PARTIALLY_RECEIVED to mock goods receipt progression
        PurchaseOrder po = purchaseOrderRepository.findById(poId).orElseThrow();
        po.setStatus(PurchaseOrderStatus.PARTIALLY_RECEIVED);
        purchaseOrderRepository.save(po);

        mockMvc.perform(post("/api/v1/purchase-orders/" + poId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Cancel after receipt"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only ISSUED purchase orders can be cancelled"));

        // Test Validation 7: Close of PARTIALLY_RECEIVED should fail (only RECEIVED can be closed)
        mockMvc.perform(post("/api/v1/purchase-orders/" + poId + "/close"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only RECEIVED purchase orders can be closed"));

        // Test Close of RECEIVED PO -> successful closure
        po = purchaseOrderRepository.findById(poId).orElseThrow();
        po.setStatus(PurchaseOrderStatus.RECEIVED);
        purchaseOrderRepository.save(po);

        mockMvc.perform(post("/api/v1/purchase-orders/" + poId + "/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CLOSED"))
                .andExpect(jsonPath("$.data.closedAt").isNotEmpty());

        // Test Delete DRAFT PO -> successful hard deletion
        PurchaseOrderRequest draftReq = new PurchaseOrderRequest(
                company.getId(), supplier.getId(), null,
                LocalDate.now().plusDays(5), "Draft to be deleted",
                BigDecimal.ZERO, BigDecimal.ZERO, "AED",
                List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(5.5), "Remarks"))
        );
        String draftCreated = mockMvc.perform(post("/api/v1/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draftReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long draftId = objectMapper.readTree(draftCreated).path("data").path("id").asLong();

        mockMvc.perform(delete("/api/v1/purchase-orders/" + draftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Purchase order deleted successfully"));

        assertFalse(purchaseOrderRepository.existsById(draftId));
    }
}
