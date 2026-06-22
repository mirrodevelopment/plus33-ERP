package com.plus33.erp.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import com.plus33.erp.security.entity.User;
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
public class StockCountControllerIntegrationTest {

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
    private StockCountRepository stockCountRepository;

    @Autowired
    private InventoryAdjustmentRepository inventoryAdjustmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "STOCK_COUNT_CREATE", "STOCK_COUNT_VIEW", "STOCK_COUNT_UPDATE",
            "STOCK_COUNT_ASSIGN", "STOCK_COUNT_SUBMIT", "STOCK_COUNT_APPROVE", "STOCK_COUNT_POST"
    })
    public void testStockCountWorkflowWithHighVarianceManualApprovalAndReopen() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        // Setup Warehouse
        Warehouse wh = new Warehouse();
        wh.setCode("WH-SC-TEST-1-" + System.nanoTime());
        wh.setName("Warehouse SC Test 1");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // Pre-populate stock
        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        stock.setWarehouse(wh);
        stock.setQuantity(BigDecimal.valueOf(100.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        User currentUser = userRepository.findByEmail("admin@plus33.com").orElseThrow();

        UUID clientRef = UUID.randomUUID();

        // 1. Create a draft Stock Count (CYCLE count)
        StockCountRequest createRequest = new StockCountRequest(
                company.getId(), wh.getId(), null, StockCountType.CYCLE, true, BigDecimal.valueOf(5.00),
                clientRef, "Cycle count test remarks", List.of(product.getId())
        );

        String createRespStr = mockMvc.perform(post("/api/v1/stock-counts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.countNumber").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Long countId = objectMapper.readTree(createRespStr).get("data").get("id").asLong();

        // 2. Validate Idempotent creation replay
        mockMvc.perform(post("/api/v1/stock-counts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(countId));

        // 3. Validation: Reject concurrent active overlapping count at same warehouse
        StockCountRequest overlappingRequest = new StockCountRequest(
                company.getId(), wh.getId(), null, StockCountType.CYCLE, true, BigDecimal.valueOf(5.00),
                UUID.randomUUID(), "Overlap test", List.of(product.getId())
        );
        mockMvc.perform(post("/api/v1/stock-counts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overlappingRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("An active stock count already exists")));

        // 4. Update the draft stock count
        StockCountUpdateRequest updateRequest = new StockCountUpdateRequest(
                wh.getId(), null, StockCountType.CYCLE, true, BigDecimal.valueOf(5.00),
                "Updated remarks", List.of(product.getId())
        );
        mockMvc.perform(put("/api/v1/stock-counts/" + countId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.remarks").value("Updated remarks"));

        // 5. Assign the stock count
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/assign")
                        .param("userId", currentUser.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.data.assignedToId").value(currentUser.getId()));

        // 6. Start the stock count (takes original snapshot)
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));

        // 7. Verify Blind count rule (systemQuantity, reservedQuantity, availableQuantity, variance must be null in responses)
        mockMvc.perform(get("/api/v1/stock-counts/" + countId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].systemQuantity").isEmpty())
                .andExpect(jsonPath("$.data.items[0].reservedQuantity").isEmpty())
                .andExpect(jsonPath("$.data.items[0].availableQuantity").isEmpty())
                .andExpect(jsonPath("$.data.items[0].variance").isEmpty());

        // 8. Submit count with high variance (Counted = 90.00, System = 100.00 -> 10% variance, exceeds 5% threshold)
        StockCountSubmitRequest submitReqHigh = new StockCountSubmitRequest(
                List.of(new StockCountItemCountRequest(product.getId(), BigDecimal.valueOf(90.00)))
        );
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submitReqHigh)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data.approvalRequired").value(true));

        // System quantity & variance should be visible now that it is SUBMITTED
        mockMvc.perform(get("/api/v1/stock-counts/" + countId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].systemQuantity").value(100.00))
                .andExpect(jsonPath("$.data.items[0].variance").value(-10.00));

        // 9. Posting high-variance count directly is blocked (must be APPROVED first)
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/post"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Only APPROVED counts can be posted")));

        // 10. Reject the stock count
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/reject")
                        .param("reason", "Needs recount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REJECTED"))
                .andExpect(jsonPath("$.data.rejectionReason").value("Needs recount"));

        // 11. Reopen the count for recounts
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/reopen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data.recountNumber").value(1))
                .andExpect(jsonPath("$.data.items[0].countedQuantity").isEmpty())
                .andExpect(jsonPath("$.data.items[0].variance").isEmpty());

        // 12. Submit count again with low variance (Counted = 98.00, System = 100.00 -> 2% variance, within 5% threshold)
        StockCountSubmitRequest submitReqLow = new StockCountSubmitRequest(
                List.of(new StockCountItemCountRequest(product.getId(), BigDecimal.valueOf(98.00)))
        );
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submitReqLow)))
                .andExpect(status().isOk())
                // Since variance is within threshold, it transitions SUBMITTED ➔ APPROVED auto
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.approvalRequired").value(false));

        // Verify that the automatically approved count successfully created an InventoryAdjustment of type STOCK_COUNT_VARIANCE in APPROVED status
        String approvedCountStr = mockMvc.perform(get("/api/v1/stock-counts/" + countId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long adjustmentId = objectMapper.readTree(approvedCountStr).get("data").get("adjustmentId").asLong();
        assertNotNull(adjustmentId);

        // Fetch InventoryAdjustment to verify it is approved and contains variance quantity -2.00
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(adjustmentId).orElseThrow();
        assertEquals(InventoryAdjustmentStatus.APPROVED, adjustment.getStatus());
        assertEquals(InventoryAdjustmentType.STOCK_COUNT_VARIANCE, adjustment.getAdjustmentType());
        assertEquals(0, adjustment.getItems().get(0).getQuantity().compareTo(BigDecimal.valueOf(-2.00)));

        // 13. Post the approved count (should execute InventoryAdjustment posting)
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("POSTED"));

        // Verify stock is updated: 100.00 - 2.00 = 98.00
        InventoryStock stockAfterPost = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), wh.getId()).orElseThrow();
        assertEquals(0, stockAfterPost.getQuantity().compareTo(BigDecimal.valueOf(98.00)));

        // 14. Close the stock count session
        mockMvc.perform(post("/api/v1/stock-counts/" + countId + "/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CLOSED"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "STOCK_COUNT_CREATE", "STOCK_COUNT_VIEW", "STOCK_COUNT_UPDATE",
            "STOCK_COUNT_ASSIGN", "STOCK_COUNT_SUBMIT", "STOCK_COUNT_APPROVE", "STOCK_COUNT_POST"
    })
    public void testStockCountOverlappingActiveCountsFullCountAndCycleCount() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL").orElseThrow();
        Region region = regionRepository.findAll().stream().filter(r -> r.getCompany().getId().equals(company.getId())).findFirst().orElseThrow();

        Warehouse wh = new Warehouse();
        wh.setCode("WH-SC-TEST-2-" + System.nanoTime());
        wh.setName("Warehouse SC Test 2");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        Product p1 = productRepository.findAll().stream().filter(Product::getActive).findFirst().orElseThrow();

        // Pre-populate stock
        InventoryStock stock = new InventoryStock();
        stock.setProduct(p1);
        stock.setWarehouse(wh);
        stock.setQuantity(BigDecimal.valueOf(50.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        // Create cycle count for p1
        StockCountRequest cycleRequest = new StockCountRequest(
                company.getId(), wh.getId(), null, StockCountType.CYCLE, true, BigDecimal.valueOf(5.00),
                UUID.randomUUID(), "Cycle count p1", List.of(p1.getId())
        );
        mockMvc.perform(post("/api/v1/stock-counts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cycleRequest)))
                .andExpect(status().isCreated());

        // Creating full count at same warehouse should be rejected because cycle count covers p1 which overlaps with full count
        StockCountRequest fullRequest = new StockCountRequest(
                company.getId(), wh.getId(), null, StockCountType.FULL, true, BigDecimal.valueOf(5.00),
                UUID.randomUUID(), "Full count", null
        );
        mockMvc.perform(post("/api/v1/stock-counts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fullRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("An active stock count already exists")));
    }
}
