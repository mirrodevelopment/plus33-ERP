package com.plus33.erp.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
public class ReplenishmentControllerIntegrationTest {

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
    private ReplenishmentRuleRepository replenishmentRuleRepository;

    @Autowired
    private ReplenishmentSuggestionRepository replenishmentSuggestionRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "REPLENISHMENT_RULE_CREATE", "REPLENISHMENT_RULE_VIEW", "REPLENISHMENT_RULE_UPDATE", "REPLENISHMENT_RULE_DELETE",
            "REPLENISHMENT_SUGGESTION_VIEW", "REPLENISHMENT_SUGGESTION_ACKNOWLEDGE", "REPLENISHMENT_SUGGESTION_ORDER", "REPLENISHMENT_SUGGESTION_CANCEL"
    })
    public void testReplenishmentWorkflow() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        // Setup Warehouse
        Warehouse wh = new Warehouse();
        wh.setCode("WH-REPL-TEST-" + System.nanoTime());
        wh.setName("Warehouse Replenishment Test");
        wh.setRegion(region);
        wh.setActive(true);
        wh = warehouseRepository.save(wh);

        // Setup Warehouse 2
        Warehouse wh2 = new Warehouse();
        wh2.setCode("WH-REPL-TEST-2-" + System.nanoTime());
        wh2.setName("Warehouse Replenishment Test 2");
        wh2.setRegion(region);
        wh2.setActive(true);
        final Warehouse finalWh2 = warehouseRepository.save(wh2);

        // Setup Store
        Store store = new Store();
        store.setCode("STR-REPL-TEST-" + System.nanoTime());
        store.setName("Store Replenishment Test");
        store.setRegion(region);
        store.setActive(true);
        store = storeRepository.save(store);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // Setup a supplier for company
        Supplier supplier = new Supplier();
        supplier.setCompany(company);
        supplier.setCode("SUP-REPL-TEST-" + System.nanoTime());
        supplier.setName("Supplier Replenishment Test");
        supplier.setActive(true);
        supplierRepository.save(supplier);

        // 1. Create a Replenishment Rule (for warehouse)
        UUID ruleClientRef = UUID.randomUUID();
        ReplenishmentRuleRequest createRuleRequest = new ReplenishmentRuleRequest(
                company.getId(),
                product.getId(),
                wh.getId(),
                null,
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(30.00),
                BigDecimal.valueOf(50.00),
                5,
                true,
                ruleClientRef
        );

        String createRuleResp = mockMvc.perform(post("/api/v1/replenishment/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRuleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.minQuantity").value(10.00))
                .andExpect(jsonPath("$.data.maxQuantity").value(100.00))
                .andExpect(jsonPath("$.data.active").value(true))
                .andReturn().getResponse().getContentAsString();

        Long ruleId = objectMapper.readTree(createRuleResp).get("data").get("id").asLong();

        // 2. Validate Idempotent creation replay
        mockMvc.perform(post("/api/v1/replenishment/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRuleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(ruleId));

        // 3. Create rule with invalid quantities (reorder_quantity > max_quantity)
        ReplenishmentRuleRequest invalidRuleRequest = new ReplenishmentRuleRequest(
                company.getId(),
                product.getId(),
                null,
                store.getId(),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(30.00),
                BigDecimal.valueOf(150.00), // Invalid: reorderQuantity > maxQuantity
                5,
                true,
                UUID.randomUUID()
        );
        mockMvc.perform(post("/api/v1/replenishment/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRuleRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // 4. Update the replenishment rule
        ReplenishmentRuleUpdateRequest updateRequest = new ReplenishmentRuleUpdateRequest(
                BigDecimal.valueOf(15.00),
                BigDecimal.valueOf(120.00),
                BigDecimal.valueOf(40.00),
                BigDecimal.valueOf(60.00),
                6,
                true
        );
        mockMvc.perform(put("/api/v1/replenishment/rules/" + ruleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.minQuantity").value(15.00))
                .andExpect(jsonPath("$.data.maxQuantity").value(120.00));

        // 5. Evaluate all - no suggestion when stock is sufficient
        // Pre-populate stock: quantity = 50.00, which is > reorderPoint (40.00)
        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        stock.setWarehouse(wh);
        stock.setQuantity(BigDecimal.valueOf(50.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        mockMvc.perform(post("/api/v1/replenishment/evaluate")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty()); // No suggestion created

        // 6. Evaluate all - OPEN suggestion created when stock <= reorderPoint
        // Reduce stock: quantity = 25.00 <= reorderPoint (40.00)
        stock.setQuantity(BigDecimal.valueOf(25.00));
        inventoryStockRepository.save(stock);

        String evaluateResp = mockMvc.perform(post("/api/v1/replenishment/evaluate")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].status").value("OPEN"))
                .andExpect(jsonPath("$.data[0].suggestedQuantity").value(95.00)) // max(120.00) - available(25.00) = 95.00
                .andReturn().getResponse().getContentAsString();

        Long suggestionId = objectMapper.readTree(evaluateResp).get("data").get(0).get("id").asLong();

        // 7. Evaluate all again - duplicate suggestion not created if OPEN already exists
        mockMvc.perform(post("/api/v1/replenishment/evaluate")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(suggestionId));

        // 8. Acknowledge suggestion
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + suggestionId + "/acknowledge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ACKNOWLEDGED"));

        // 9. Cancel suggestion
        ReplenishmentSuggestionCancelRequest cancelRequest = new ReplenishmentSuggestionCancelRequest("Not needed anymore");
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + suggestionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.notes").value("Not needed anymore"));

        // 10. Cancel non-active suggestion should be rejected
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + suggestionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // 11. Create a suggestion for Store to test Transfer generation
        // First create a store rule
        UUID storeRuleClientRef = UUID.randomUUID();
        ReplenishmentRuleRequest storeRuleRequest = new ReplenishmentRuleRequest(
                company.getId(),
                product.getId(),
                null,
                store.getId(),
                BigDecimal.valueOf(5.00),
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(15.00),
                BigDecimal.valueOf(20.00),
                2,
                true,
                storeRuleClientRef
        );

        String storeRuleResp = mockMvc.perform(post("/api/v1/replenishment/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRuleRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long storeRuleId = objectMapper.readTree(storeRuleResp).get("data").get("id").asLong();

        // Populate stock for store: quantity = 10.00 <= reorderPoint(15.00)
        InventoryStock storeStock = new InventoryStock();
        storeStock.setProduct(product);
        storeStock.setStore(store);
        storeStock.setQuantity(BigDecimal.valueOf(10.00));
        storeStock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(storeStock);

        String storeEvalResp = mockMvc.perform(post("/api/v1/replenishment/rules/" + storeRuleId + "/evaluate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("OPEN"))
                .andExpect(jsonPath("$.data.suggestedQuantity").value(40.00)) // max(50.00) - available(10.00) = 40.00
                .andReturn().getResponse().getContentAsString();
        Long storeSuggestionId = objectMapper.readTree(storeEvalResp).get("data").get("id").asLong();

        // 12. Create inventory transfer from suggestion
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + storeSuggestionId + "/create-transfer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ORDERED"))
                .andExpect(jsonPath("$.data.transferId").isNotEmpty());

        // 13. Create transfer from warehouse suggestion should fail
        // Create another rule for Warehouse and evaluate to get suggestion
        ReplenishmentRuleRequest whRule2 = new ReplenishmentRuleRequest(
                company.getId(),
                product.getId(),
                finalWh2.getId(),
                null,
                BigDecimal.valueOf(5.00),
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(20.00),
                BigDecimal.valueOf(20.00),
                2,
                true,
                UUID.randomUUID()
        );
        String whRule2Resp = mockMvc.perform(post("/api/v1/replenishment/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(whRule2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long whRule2Id = objectMapper.readTree(whRule2Resp).get("data").get("id").asLong();

        // Set warehouse 2 stock low
        InventoryStock stock2 = new InventoryStock();
        stock2.setProduct(product);
        stock2.setWarehouse(finalWh2);
        stock2.setQuantity(BigDecimal.valueOf(5.00));
        stock2.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock2);

        String whSuggestionResp = mockMvc.perform(post("/api/v1/replenishment/rules/" + whRule2Id + "/evaluate"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long whSuggestionId = objectMapper.readTree(whSuggestionResp).get("data").get("id").asLong();

        // Try creating transfer for warehouse location -> should fail
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + whSuggestionId + "/create-transfer"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Inventory transfers can only replenish Store locations")));

        // 14. Create Purchase Request from warehouse suggestion -> should succeed
        mockMvc.perform(post("/api/v1/replenishment/suggestions/" + whSuggestionId + "/create-purchase-request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ORDERED"))
                .andExpect(jsonPath("$.data.purchaseRequestId").isNotEmpty());

        // 15. Deactivate rule
        mockMvc.perform(delete("/api/v1/replenishment/rules/" + ruleId))
                .andExpect(status().isOk());
        assertFalse(replenishmentRuleRepository.findById(ruleId).orElseThrow().isActive());
    }
}
