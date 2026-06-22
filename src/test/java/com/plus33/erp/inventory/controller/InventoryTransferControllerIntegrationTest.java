package com.plus33.erp.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.analytics.service.AnalyticsRefreshService;
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
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryTransferControllerIntegrationTest {

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
    private InventoryTransferRepository inventoryTransferRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_TRANSFER_CREATE", "INVENTORY_TRANSFER_VIEW", "INVENTORY_TRANSFER_UPDATE",
            "INVENTORY_TRANSFER_APPROVE", "INVENTORY_TRANSFER_DISPATCH", "INVENTORY_TRANSFER_RECEIVE",
            "INVENTORY_TRANSFER_CANCEL"
    })
    public void testInventoryTransferWorkflow() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        // Setup Warehouses and Stores
        Warehouse wh1 = new Warehouse();
        wh1.setCode("WH-TEST-1-" + System.nanoTime());
        wh1.setName("Warehouse Test 1");
        wh1.setRegion(region);
        wh1.setActive(true);
        wh1 = warehouseRepository.save(wh1);

        Warehouse wh2 = new Warehouse();
        wh2.setCode("WH-TEST-2-" + System.nanoTime());
        wh2.setName("Warehouse Test 2");
        wh2.setRegion(region);
        wh2.setActive(true);
        wh2 = warehouseRepository.save(wh2);

        Store store1 = new Store();
        store1.setCode("ST-TEST-1-" + System.nanoTime());
        store1.setName("Store Test 1");
        store1.setRegion(region);
        store1.setActive(true);
        store1 = storeRepository.save(store1);

        Store store2 = new Store();
        store2.setCode("ST-TEST-2-" + System.nanoTime());
        store2.setName("Store Test 2");
        store2.setRegion(region);
        store2.setActive(true);
        store2 = storeRepository.save(store2);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // Pre-populate source stock (wh1) with quantity 100
        InventoryStock srcStock = new InventoryStock();
        srcStock.setProduct(product);
        srcStock.setWarehouse(wh1);
        srcStock.setQuantity(BigDecimal.valueOf(100.00));
        srcStock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(srcStock);

        UUID clientRef = UUID.randomUUID();

        // 1. Validation: Reject same source and destination
        InventoryTransferRequest sameLocRequest = new InventoryTransferRequest(
                company.getId(), wh1.getId(), null, wh1.getId(), null, clientRef, "Same locations test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.TEN))
        );
        mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sameLocRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cannot be the same")));

        // 2. Validation: Reject Store-to-Warehouse transfers
        InventoryTransferRequest storeToWhRequest = new InventoryTransferRequest(
                company.getId(), null, store1.getId(), wh2.getId(), null, clientRef, "Store to Warehouse test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.TEN))
        );
        mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeToWhRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("prohibited")));

        // 3. Valid creation: wh1 -> store1
        InventoryTransferRequest validRequest = new InventoryTransferRequest(
                company.getId(), wh1.getId(), null, null, store1.getId(), clientRef, "Valid transfer request",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.valueOf(10.00)))
        );

        String responseStr = mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.transferNumber").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Long transferId = objectMapper.readTree(responseStr).get("data").get("id").asLong();

        // 4. Validation: Idempotent replay of creation returns 200 OK
        mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(transferId));

        // 5. Submit the transfer
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        // 6. Validation: Insufficient stock rejection
        // Create another transfer of 1000 units (source only has 100)
        UUID clientRefOver = UUID.randomUUID();
        InventoryTransferRequest overRequest = new InventoryTransferRequest(
                company.getId(), wh1.getId(), null, null, store1.getId(), clientRefOver, "Over request test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.valueOf(1000.00)))
        );
        String overStr = mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long overTransferId = objectMapper.readTree(overStr).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/inventory-transfers/" + overTransferId + "/submit"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/inventory-transfers/" + overTransferId + "/approve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Insufficient stock")));

        // 7. Approve the valid transfer (which reserves 10 units)
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        // Verify stock reservation in database
        InventoryStock stockAfterApprove = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh1.getId()).orElseThrow();
        assertEquals(0, stockAfterApprove.getQuantity().compareTo(BigDecimal.valueOf(100.00)));
        assertEquals(0, stockAfterApprove.getReservedQuantity().compareTo(BigDecimal.valueOf(10.00)));

        // 8. Validation: Dispatch before approval should be blocked for a new transfer
        UUID clientRefNoApp = UUID.randomUUID();
        InventoryTransferRequest noAppRequest = new InventoryTransferRequest(
                company.getId(), wh1.getId(), null, null, store1.getId(), clientRefNoApp, "No app test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.TEN))
        );
        String noAppStr = mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noAppRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long noAppId = objectMapper.readTree(noAppStr).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/inventory-transfers/" + noAppId + "/dispatch"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Only APPROVED transfers can be dispatched")));

        // 9. Dispatch the valid transfer (reduces stock and reservations at source)
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/dispatch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("IN_TRANSIT"));

        // Verify source stock is reduced
        InventoryStock stockAfterDispatch = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh1.getId()).orElseThrow();
        assertEquals(0, stockAfterDispatch.getQuantity().compareTo(BigDecimal.valueOf(90.00)));
        assertEquals(0, stockAfterDispatch.getReservedQuantity().compareTo(BigDecimal.ZERO));

        // 10. Validation: Cancellation after dispatch is rejected
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/cancel")
                        .param("reason", "Cancelled dispatch test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Cannot cancel transfer after dispatch")));

        // 11. Receive the transfer (increments destination stock, sets received quantities, and closes transfer)
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/receive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CLOSED"));

        // Verify destination store stock is incremented by 10
        InventoryStock destStock = inventoryStockRepository.findByProductIdAndStoreId(product.getId(), store1.getId()).orElseThrow();
        assertEquals(0, destStock.getQuantity().compareTo(BigDecimal.valueOf(10.00)));
        assertEquals(0, destStock.getReservedQuantity().compareTo(BigDecimal.ZERO));

        // 12. Validation: Duplicate receive request is rejected
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/receive"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Only IN_TRANSIT transfers can be received")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_TRANSFER_CREATE", "INVENTORY_TRANSFER_VIEW", "INVENTORY_TRANSFER_APPROVE", "INVENTORY_TRANSFER_CANCEL"
    })
    public void testCancelTransferReleasesReservation() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL").orElseThrow();
        Region region = regionRepository.findAll().stream().filter(r -> r.getCompany().getId().equals(company.getId())).findFirst().orElseThrow();

        Warehouse wh1 = new Warehouse();
        wh1.setCode("WH-TEST-CANCEL-1-" + System.nanoTime());
        wh1.setName("WH Cancel 1");
        wh1.setRegion(region);
        wh1.setActive(true);
        wh1 = warehouseRepository.save(wh1);

        Warehouse wh2 = new Warehouse();
        wh2.setCode("WH-TEST-CANCEL-2-" + System.nanoTime());
        wh2.setName("WH Cancel 2");
        wh2.setRegion(region);
        wh2.setActive(true);
        wh2 = warehouseRepository.save(wh2);

        Product product = productRepository.findAll().stream().filter(Product::getActive).findFirst().orElseThrow();

        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        stock.setWarehouse(wh1);
        stock.setQuantity(BigDecimal.valueOf(50.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        // Create, submit and approve a transfer of 20 units
        InventoryTransferRequest req = new InventoryTransferRequest(
                company.getId(), wh1.getId(), null, wh2.getId(), null, UUID.randomUUID(), "Cancel test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.valueOf(20.00)))
        );

        String str = mockMvc.perform(post("/api/v1/inventory-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        Long transferId = objectMapper.readTree(str).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/submit")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/approve")).andExpect(status().isOk());

        // Verify reserved is 20
        InventoryStock stockApp = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh1.getId()).orElseThrow();
        assertEquals(0, stockApp.getReservedQuantity().compareTo(BigDecimal.valueOf(20.00)));

        // Cancel
        mockMvc.perform(post("/api/v1/inventory-transfers/" + transferId + "/cancel")
                        .param("reason", "Customer cancelled transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancellationReason").value("Customer cancelled transaction"));

        // Verify reserved is released (0)
        InventoryStock stockCancel = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh1.getId()).orElseThrow();
        assertEquals(0, stockCancel.getReservedQuantity().compareTo(BigDecimal.ZERO));
        assertEquals(0, stockCancel.getQuantity().compareTo(BigDecimal.valueOf(50.00)));
    }
}
