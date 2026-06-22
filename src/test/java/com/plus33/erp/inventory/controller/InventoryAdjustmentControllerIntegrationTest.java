package com.plus33.erp.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryAdjustmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryStockRepository inventoryStockRepository;

    @Autowired
    private InventoryAdjustmentRepository inventoryAdjustmentRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_ADJUSTMENT_CREATE", "INVENTORY_ADJUSTMENT_VIEW", "INVENTORY_ADJUSTMENT_UPDATE",
            "INVENTORY_ADJUSTMENT_APPROVE", "INVENTORY_ADJUSTMENT_POST", "INVENTORY_ADJUSTMENT_CANCEL"
    })
    public void testInventoryAdjustmentWorkflowWithReduction() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        // Setup Warehouse
        Warehouse wh = new Warehouse();
        wh.setCode("WH-ADJ-TEST-1-" + System.nanoTime());
        wh.setName("Warehouse Adj Test 1");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        // Setup Store
        Store store = new Store();
        store.setCode("ST-ADJ-TEST-1-" + System.nanoTime());
        store.setName("Store Adj Test 1");
        store.setRegion(region);
        store.setActive(true);
        store = storeRepository.save(store);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // Pre-populate stock
        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        stock.setWarehouse(wh);
        stock.setQuantity(BigDecimal.valueOf(50.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        UUID clientRef = UUID.randomUUID();

        // 1. Validation: Reject if both warehouse and store are specified
        InventoryAdjustmentRequest invalidLocRequest = new InventoryAdjustmentRequest(
                company.getId(), wh.getId(), store.getId(), InventoryAdjustmentType.DAMAGE, clientRef, "Both locations test",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(-5.00)))
        );
        mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLocRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("must reference exactly one location")));

        // 2. Valid creation of DRAFT adjustment (with negative quantity, i.e. reduction)
        InventoryAdjustmentRequest validRequest = new InventoryAdjustmentRequest(
                company.getId(), wh.getId(), null, InventoryAdjustmentType.DAMAGE, clientRef, "Valid reduction adjustment",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(-10.00)))
        );

        String responseStr = mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.adjustmentNumber").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Long adjustmentId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

        // 3. Validation: Idempotent replay of creation returns 200 OK
        mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(adjustmentId));

        // 4. Update the draft adjustment
        InventoryAdjustmentUpdateRequest updateRequest = new InventoryAdjustmentUpdateRequest(
                wh.getId(), null, InventoryAdjustmentType.SHRINKAGE, "Updated remarks",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(-15.00)))
        );
        mockMvc.perform(put("/api/v1/inventory-adjustments/" + adjustmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.remarks").value("Updated remarks"))
                .andExpect(jsonPath("$.data.adjustmentType").value("SHRINKAGE"))
                .andExpect(jsonPath("$.data.items[0].quantity").value(-15.00));

        // 5. Submit the adjustment. Since it has a negative quantity, it must transition to SUBMITTED (requiring manual approval)
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        // 6. Manual approval transitions status to APPROVED
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        // 7. Post the adjustment (reduces stock)
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("POSTED"));

        // Verify stock is updated: 50.00 + (-15.00) = 35.00
        InventoryStock stockAfterPost = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh.getId()).orElseThrow();
        assertEquals(0, stockAfterPost.getQuantity().compareTo(BigDecimal.valueOf(35.00)));

        // Verify stock movement was registered
        List<StockMovement> movements = stockMovementRepository.findAll().stream()
                .filter(m -> m.getReferenceId().equals(adjustmentId) && m.getReferenceType() == StockMovementReferenceType.INVENTORY_ADJUSTMENT)
                .toList();
        assertFalse(movements.isEmpty());
        assertEquals(0, movements.get(0).getQuantity().compareTo(BigDecimal.valueOf(-15.00)));

        // 8. Validation: Cancellation after posting is rejected
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/cancel")
                        .param("reason", "Cancel posted test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Cannot cancel adjustment after posting")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_ADJUSTMENT_CREATE", "INVENTORY_ADJUSTMENT_VIEW", "INVENTORY_ADJUSTMENT_POST", "INVENTORY_ADJUSTMENT_CANCEL"
    })
    public void testInventoryAdjustmentWorkflowWithAutoApproval() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL").orElseThrow();
        Region region = regionRepository.findAll().stream().filter(r -> r.getCompany().getId().equals(company.getId())).findFirst().orElseThrow();

        Warehouse wh = new Warehouse();
        wh.setCode("WH-ADJ-TEST-2-" + System.nanoTime());
        wh.setName("Warehouse Adj Test 2");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        Product product = productRepository.findAll().stream().filter(Product::getActive).findFirst().orElseThrow();

        // 1. Create a draft adjustment for pure stock addition (positive delta)
        InventoryAdjustmentRequest request = new InventoryAdjustmentRequest(
                company.getId(), wh.getId(), null, InventoryAdjustmentType.FOUND_STOCK, UUID.randomUUID(), "Auto-approval test",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(25.00)))
        );

        String responseStr = mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long adjustmentId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

        // 2. Submit the adjustment. Since it has only positive quantities, it should automatically transition to APPROVED
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        // 3. Post the adjustment
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("POSTED"));

        // Verify stock is incremented: default was 0 (or new created stock), so now it is 25.00
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh.getId()).orElseThrow();
        assertEquals(0, stock.getQuantity().compareTo(BigDecimal.valueOf(25.00)));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_ADJUSTMENT_CREATE", "INVENTORY_ADJUSTMENT_VIEW", "INVENTORY_ADJUSTMENT_APPROVE", "INVENTORY_ADJUSTMENT_POST"
    })
    public void testInventoryAdjustmentCannotReduceStockBelowZero() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL").orElseThrow();
        Region region = regionRepository.findAll().stream().filter(r -> r.getCompany().getId().equals(company.getId())).findFirst().orElseThrow();

        Warehouse wh = new Warehouse();
        wh.setCode("WH-ADJ-TEST-3-" + System.nanoTime());
        wh.setName("Warehouse Adj Test 3");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        Product product = productRepository.findAll().stream().filter(Product::getActive).findFirst().orElseThrow();

        // Stock quantity in DB is 5.00
        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        stock.setWarehouse(wh);
        stock.setQuantity(BigDecimal.valueOf(5.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        // Request deduction of 10.00 (which would leave -5.00)
        InventoryAdjustmentRequest request = new InventoryAdjustmentRequest(
                company.getId(), wh.getId(), null, InventoryAdjustmentType.DAMAGE, UUID.randomUUID(), "Excess reduction test",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(-10.00)))
        );

        String responseStr = mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long adjustmentId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

        // Submit and approve
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/submit")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/approve")).andExpect(status().isOk());

        // Posting should fail due to stock dropping below zero
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/post"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Insufficient stock")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_ADJUSTMENT_CREATE", "INVENTORY_ADJUSTMENT_VIEW", "INVENTORY_ADJUSTMENT_CANCEL"
    })
    public void testCancelDraftAdjustment() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL").orElseThrow();
        Region region = regionRepository.findAll().stream().filter(r -> r.getCompany().getId().equals(company.getId())).findFirst().orElseThrow();

        Warehouse wh = new Warehouse();
        wh.setCode("WH-ADJ-TEST-4-" + System.nanoTime());
        wh.setName("Warehouse Adj Test 4");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        Product product = productRepository.findAll().stream().filter(Product::getActive).findFirst().orElseThrow();

        InventoryAdjustmentRequest request = new InventoryAdjustmentRequest(
                company.getId(), wh.getId(), null, InventoryAdjustmentType.MANUAL_CORRECTION, UUID.randomUUID(), "Draft cancel test",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(5.00)))
        );

        String responseStr = mockMvc.perform(post("/api/v1/inventory-adjustments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Long adjustmentId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

        // Cancel draft
        mockMvc.perform(post("/api/v1/inventory-adjustments/" + adjustmentId + "/cancel")
                        .param("reason", "No longer needed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancellationReason").value("No longer needed"));
    }
}
