package com.plus33.erp.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.inventory.service.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import com.plus33.erp.procurement.dto.GoodsReceiptItemRequest;
import com.plus33.erp.procurement.dto.GoodsReceiptRequest;
import com.plus33.erp.procurement.dto.GoodsReceiptResponse;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.procurement.service.GoodsReceiptService;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryTraceabilityControllerIntegrationTest {

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
    private UserRepository userRepository;

    @Autowired
    private InventoryLotRepository inventoryLotRepository;

    @Autowired
    private InventorySerialRepository inventorySerialRepository;

    @Autowired
    private InventoryTraceEventRepository inventoryTraceEventRepository;

    @Autowired
    private InventoryRecallRepository inventoryRecallRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private GoodsReceiptService goodsReceiptService;

    @Autowired
    private InventoryTransferService inventoryTransferService;

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @Autowired
    private StockCountService stockCountService;

    @Autowired
    private InventoryTraceabilityService inventoryTraceabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "INVENTORY_LOT_CREATE", "INVENTORY_LOT_VIEW", "INVENTORY_SERIAL_VIEW",
            "INVENTORY_TRACE_VIEW", "INVENTORY_RECALL_CREATE", "INVENTORY_RECALL_VIEW"
    })
    public void testInventoryTraceabilityFlows() throws Exception {
        // Setup base data
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        Warehouse tempWh = new Warehouse();
        tempWh.setCode("WH-TRACE-TEST-" + System.nanoTime());
        tempWh.setName("Warehouse Trace Test");
        tempWh.setRegion(region);
        tempWh.setActive(true);
        final Warehouse wh = warehouseRepository.save(tempWh);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        // 1. Create Lot via API
        String lotNumber = "LOT-" + System.nanoTime();
        InventoryLotRequest lotRequest = new InventoryLotRequest(
                company.getId(),
                product.getId(),
                lotNumber,
                LocalDate.now().plusMonths(6),
                LocalDate.now().minusDays(5),
                InventoryLotStatus.ACTIVE
        );

        String lotCreateResp = mockMvc.perform(post("/api/v1/inventory-lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.lotNumber").value(lotNumber))
                .andReturn().getResponse().getContentAsString();

        Long lotId = objectMapper.readTree(lotCreateResp).get("data").get("id").asLong();

        // 2. Retrieve Lot by ID
        mockMvc.perform(get("/api/v1/inventory-lots/" + lotId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.lotNumber").value(lotNumber));

        // 3. Get all Lots
        mockMvc.perform(get("/api/v1/inventory-lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Create Serial directly in DB for status transition testing
        String serialNumber = "SN-" + System.nanoTime();
        InventorySerial serial = InventorySerial.builder()
                .company(company)
                .product(product)
                .lot(inventoryLotRepository.findById(lotId).orElse(null))
                .serialNumber(serialNumber)
                .warehouse(wh)
                .status(InventorySerialStatus.IN_STOCK)
                .build();
        serial = inventorySerialRepository.save(serial);

        // 5. List Serials via API
        mockMvc.perform(get("/api/v1/inventory-serials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[?(@.serialNumber == '" + serialNumber + "')]").exists());

        // 6. Test Serial Status Transitions (using service directly)
        // IN_STOCK -> TRANSIT (Valid)
        inventoryTraceabilityService.transitionSerialStatus(serial.getId(), InventorySerialStatus.TRANSIT, null, null);
        assertEquals(InventorySerialStatus.TRANSIT, inventorySerialRepository.findById(serial.getId()).get().getStatus());

        // TRANSIT -> SOLD (Invalid)
        final Long serialId = serial.getId();
        assertThrows(BusinessException.class, () -> {
            inventoryTraceabilityService.transitionSerialStatus(serialId, InventorySerialStatus.SOLD, null, null);
        });

        // TRANSIT -> IN_STOCK (Valid)
        inventoryTraceabilityService.transitionSerialStatus(serialId, InventorySerialStatus.IN_STOCK, wh.getId(), null);
        assertEquals(InventorySerialStatus.IN_STOCK, inventorySerialRepository.findById(serialId).get().getStatus());

        // IN_STOCK -> SOLD (Valid, terminal)
        inventoryTraceabilityService.transitionSerialStatus(serialId, InventorySerialStatus.SOLD, null, null);
        assertEquals(InventorySerialStatus.SOLD, inventorySerialRepository.findById(serialId).get().getStatus());

        // SOLD -> IN_STOCK (Invalid - SOLD is terminal)
        assertThrows(BusinessException.class, () -> {
            inventoryTraceabilityService.transitionSerialStatus(serialId, InventorySerialStatus.IN_STOCK, wh.getId(), null);
        });

        // 7. FEFO Allocation Ordering
        // Create another lot with closer expiry date
        String closerLotNumber = "LOT-FEFO-CLOSE-" + System.nanoTime();
        InventoryLotRequest closerLotReq = new InventoryLotRequest(
                company.getId(),
                product.getId(),
                closerLotNumber,
                LocalDate.now().plusMonths(1), // closer expiry
                LocalDate.now().minusDays(5),
                InventoryLotStatus.ACTIVE
        );
        inventoryTraceabilityService.createLot(closerLotReq);

        List<InventoryLotResponse> fefoLots = inventoryTraceabilityService.getLotsForFefoAllocation(product.getId());
        assertTrue(fefoLots.size() >= 2);
        // Closer lot should be first in FEFO ordering
        assertEquals(closerLotNumber, fefoLots.get(0).lotNumber());

        // 8. Expiry Scheduler Test
        // Create an expired lot in the DB
        String expiredLotNumber = "LOT-EXP-" + System.nanoTime();
        InventoryLot expiredLot = InventoryLot.builder()
                .company(company)
                .product(product)
                .lotNumber(expiredLotNumber)
                .expiryDate(LocalDate.now().minusDays(1)) // expired yesterday
                .manufacturedDate(LocalDate.now().minusDays(10))
                .status(InventoryLotStatus.ACTIVE)
                .build();
        inventoryLotRepository.save(expiredLot);

        // Run scheduler method
        inventoryTraceabilityService.processExpiredLots();
        assertEquals(InventoryLotStatus.EXPIRED, inventoryLotRepository.findByCompanyIdAndProductIdAndLotNumber(company.getId(), product.getId(), expiredLotNumber).get().getStatus());

        // 9. Recall Workflows
        // Create another active lot
        String recallLotNumber = "LOT-REC-" + System.nanoTime();
        InventoryLotResponse recallLotResponse = inventoryTraceabilityService.createLot(new InventoryLotRequest(
                company.getId(),
                product.getId(),
                recallLotNumber,
                LocalDate.now().plusYears(1),
                LocalDate.now(),
                InventoryLotStatus.ACTIVE
        ));

        // Create recall via API
        InventoryRecallRequest recallReq = new InventoryRecallRequest(
                company.getId(),
                product.getId(),
                recallLotResponse.id(),
                null,
                "Quality defect found in batch",
                "REF-REC-101"
        );

        mockMvc.perform(post("/api/v1/inventory-recalls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recallReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        // Verify lot status updated to RECALLED
        assertEquals(InventoryLotStatus.RECALLED, inventoryLotRepository.findById(recallLotResponse.id()).get().getStatus());

        // Try duplicate active recall (should fail)
        mockMvc.perform(post("/api/v1/inventory-recalls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recallReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // Get recalls
        mockMvc.perform(get("/api/v1/inventory-recalls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // 10. Trace logs querying
        mockMvc.perform(get("/api/v1/inventory-trace/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());

        mockMvc.perform(get("/api/v1/inventory-trace/lot/" + recallLotNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "GOODS_RECEIPT_CREATE", "GOODS_RECEIPT_VIEW",
            "INVENTORY_TRANSFER_CREATE", "INVENTORY_TRANSFER_VIEW", "INVENTORY_TRANSFER_UPDATE", "INVENTORY_TRANSFER_DELETE", "INVENTORY_TRANSFER_SUBMIT", "INVENTORY_TRANSFER_APPROVE", "INVENTORY_TRANSFER_DISPATCH", "INVENTORY_TRANSFER_RECEIVE",
            "INVENTORY_ADJUSTMENT_CREATE", "INVENTORY_ADJUSTMENT_VIEW", "INVENTORY_ADJUSTMENT_SUBMIT", "INVENTORY_ADJUSTMENT_APPROVE", "INVENTORY_ADJUSTMENT_POST",
            "STOCK_COUNT_CREATE", "STOCK_COUNT_VIEW", "STOCK_COUNT_ASSIGN", "STOCK_COUNT_START", "STOCK_COUNT_SUBMIT", "STOCK_COUNT_APPROVE", "STOCK_COUNT_POST",
            "INVENTORY_TRACE_VIEW"
    })
    public void testUpstreamModuleAutomatedTraceEvents() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No region found for PLUS33_GLOBAL"));

        Warehouse tempWh1 = new Warehouse();
        tempWh1.setCode("WH-UPSTREAM-TEST-1-" + System.nanoTime());
        tempWh1.setName("Upstream Warehouse 1");
        tempWh1.setRegion(region);
        tempWh1.setActive(true);
        final Warehouse wh = warehouseRepository.save(tempWh1);

        Warehouse tempWh2 = new Warehouse();
        tempWh2.setCode("WH-UPSTREAM-TEST-2-" + System.nanoTime());
        tempWh2.setName("Upstream Warehouse 2");
        tempWh2.setRegion(region);
        tempWh2.setActive(true);
        final Warehouse destWh = warehouseRepository.save(tempWh2);

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        Supplier supplier = supplierRepository.findAll().stream()
                .filter(s -> s.getCompany().getId().equals(company.getId()) && s.getActive())
                .findFirst()
                .orElseGet(() -> {
                    Supplier s = new Supplier();
                    s.setCode("TEST_SUPP_TRACE_" + System.nanoTime());
                    s.setName("Test Supplier Trace");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // 1. Goods Receipt trace logging
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-TRACE-TEST-" + System.nanoTime());
        po.setOrderedBy(userRepository.findAll().get(0));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(10));
        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedAt(LocalDateTime.now());
        po.setIssuedBy(userRepository.findAll().get(0));

        PurchaseOrderItem poItem = new PurchaseOrderItem();
        poItem.setProduct(product);
        poItem.setOrderedQuantity(BigDecimal.valueOf(10));
        poItem.setUnitPrice(BigDecimal.TEN);
        poItem.setReceivedQuantity(BigDecimal.ZERO);
        poItem.setRemainingQuantity(BigDecimal.valueOf(10));
        poItem.setPurchaseOrder(po);

        po.getItems().add(poItem);
        po.setSubtotalAmount(BigDecimal.valueOf(100));
        po.setTotalAmount(BigDecimal.valueOf(100));
        po = purchaseOrderRepository.save(po);

        GoodsReceiptRequest grReq = new GoodsReceiptRequest(
                po.getId(), company.getId(), wh.getId(), null,
                "Trace test", "DN-1", "INV-1", UUID.randomUUID(),
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.valueOf(5), "Remarks"))
        );

        goodsReceiptService.createGoodsReceipt(grReq);

        // Verify trace event exists for RECEIPT
        List<InventoryTraceEvent> traceEvents = inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(product.getId());
        assertTrue(traceEvents.stream().anyMatch(e -> e.getEventType() == InventoryTraceEventType.RECEIPT));

        // 2. Transfer trace logging
        InventoryTransferRequest transferRequest = new InventoryTransferRequest(
                company.getId(),
                wh.getId(), null,
                destWh.getId(), null,
                UUID.randomUUID(),
                "Transfer trace logging test",
                List.of(new InventoryTransferItemRequest(product.getId(), BigDecimal.valueOf(2)))
        );
        var createResult = inventoryTransferService.createTransfer(transferRequest);
        Long transferId = createResult.data().id();

        inventoryTransferService.submitTransfer(transferId);
        inventoryTransferService.approveTransfer(transferId);
        inventoryTransferService.dispatchTransfer(transferId);

        // Verify TRANSFER_OUT trace event exists
        assertTrue(inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(product.getId())
                .stream().anyMatch(e -> e.getEventType() == InventoryTraceEventType.TRANSFER_OUT));

        inventoryTransferService.receiveTransfer(transferId);

        // Verify TRANSFER_IN trace event exists
        assertTrue(inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(product.getId())
                .stream().anyMatch(e -> e.getEventType() == InventoryTraceEventType.TRANSFER_IN));

        // 3. Adjustment trace logging
        InventoryAdjustmentRequest adjRequest = new InventoryAdjustmentRequest(
                company.getId(),
                wh.getId(), null,
                InventoryAdjustmentType.MANUAL_CORRECTION,
                UUID.randomUUID(),
                "Adjustment trace logging test",
                List.of(new InventoryAdjustmentItemRequest(product.getId(), BigDecimal.valueOf(3)))
        );
        var adjCreateResult = inventoryAdjustmentService.createAdjustment(adjRequest);
        Long adjId = adjCreateResult.data().id();

        inventoryAdjustmentService.submitAdjustment(adjId);
        inventoryAdjustmentService.postAdjustment(adjId);

        // Verify ADJUSTMENT trace event exists
        assertTrue(inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(product.getId())
                .stream().anyMatch(e -> e.getEventType() == InventoryTraceEventType.ADJUSTMENT));

        // 4. Stock Count trace logging
        StockCountRequest countReq = new StockCountRequest(
                company.getId(),
                wh.getId(), null,
                StockCountType.CYCLE,
                false,
                BigDecimal.valueOf(1.00),
                UUID.randomUUID(),
                "Stock count trace test",
                List.of(product.getId())
        );
        var countCreateResult = stockCountService.createCount(countReq);
        Long countId = countCreateResult.data().id();

        stockCountService.assignCount(countId, userRepository.findAll().get(0).getId());
        stockCountService.startCount(countId);
        // submit count with a variance of 4 (e.g. system is now 6, counted is 10, variance is +4)
        stockCountService.submitCount(countId, new StockCountSubmitRequest(
                List.of(new StockCountItemCountRequest(product.getId(), BigDecimal.valueOf(10)))
        ));

        stockCountService.approveCount(countId);

        // Verify COUNT_VARIANCE trace event exists
        assertTrue(inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(product.getId())
                .stream().anyMatch(e -> e.getEventType() == InventoryTraceEventType.COUNT_VARIANCE));
    }
}
