package com.plus33.erp.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockMovement;
import com.plus33.erp.inventory.entity.StockMovementReferenceType;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.StockMovementRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.repository.*;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PickListControllerIntegrationTest {

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
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private PickListRepository pickListRepository;

    @Autowired
    private InventoryAllocationRepository inventoryAllocationRepository;

    @Autowired
    private InventoryStockRepository inventoryStockRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company globalCompany;
    private Customer activeCustomer;
    private Product product1;
    private Warehouse warehouse1;
    private SalesOrder approvedOrder;

    @BeforeEach
    public void setUp() {
        globalCompany = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        activeCustomer = customerRepository.save(Customer.builder()
                .company(globalCompany)
                .code("CUST_TEST_PK_01")
                .name("Acme Picking Customer")
                .customerType(CustomerType.B2B)
                .status(CustomerStatus.ACTIVE)
                .billingAddress("123 Billing St")
                .shippingAddress("456 Shipping Ave")
                .pricingTier("WHOLESALE")
                .creditLimit(BigDecimal.valueOf(10000.00))
                .outstandingBalance(BigDecimal.ZERO)
                .discountRate(BigDecimal.ZERO)
                .taxProfile(TaxProfile.STANDARD)
                .paymentTermsDays(30)
                .currencyCode("AED")
                .build());

        List<Product> products = productRepository.findAll();
        product1 = products.stream()
                .filter(p -> p.getCode().equals("RAW-ARA-001"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("RAW-ARA-001 product not found"));

        warehouse1 = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        User adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // Seed stock for testing: 100 units
        Optional<InventoryStock> stockOpt = inventoryStockRepository.findByProductIdAndWarehouseId(
                product1.getId(), warehouse1.getId()
        );
        InventoryStock stock = stockOpt.orElseGet(() -> {
            InventoryStock s = new InventoryStock();
            s.setProduct(product1);
            s.setWarehouse(warehouse1);
            return s;
        });
        stock.setQuantity(BigDecimal.valueOf(100.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        // Create an Approved Sales Order for 10 units of product1
        SalesOrder order = SalesOrder.builder()
                .company(globalCompany)
                .customer(activeCustomer)
                .orderNumber("SO-TEST-PK-01")
                .clientReferenceId(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .currencyCode("AED")
                .paymentTermsDays(30)
                .billingAddress("123 Billing St")
                .shippingAddress("456 Shipping Ave")
                .status(SalesOrderStatus.APPROVED)
                .customerName(activeCustomer.getName())
                .customerCode(activeCustomer.getCode())
                .customerType(activeCustomer.getCustomerType().name())
                .pricingTier(activeCustomer.getPricingTier())
                .discountRate(activeCustomer.getDiscountRate())
                .taxProfile(activeCustomer.getTaxProfile().name())
                .subtotal(BigDecimal.valueOf(1000.00))
                .discountAmount(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(1000.00))
                .outstandingAmount(BigDecimal.valueOf(1000.00))
                .orderedBy(adminUser)
                .build();

        SalesOrderItem item = SalesOrderItem.builder()
                .product(product1)
                .orderedQuantity(BigDecimal.valueOf(10.00))
                .unitPrice(BigDecimal.valueOf(100.00))
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.ZERO)
                .lineTotal(BigDecimal.valueOf(1000.00))
                .build();
        order.addItem(item);

        approvedOrder = salesOrderRepository.save(order);
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PICK_LIST_CREATE", "PICK_LIST_VIEW", "PICK_LIST_RELEASE",
            "PICK_LIST_PICK", "PICK_LIST_PACK", "PICK_LIST_SHIP", "PICK_LIST_CANCEL"
    })
    public void testFullPickingWorkflow() throws Exception {
        UUID clientRef = UUID.randomUUID();

        // 1. Create Pick List in DRAFT status
        PickListRequest createReq = new PickListRequest(
                globalCompany.getId(),
                approvedOrder.getId(),
                warehouse1.getId(),
                null,
                clientRef
        );

        String createResp = mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.pickNumber").value(org.hamcrest.Matchers.startsWith("PK-")))
                .andExpect(jsonPath("$.data.items[0].orderedQuantity").value(10.00))
                .andExpect(jsonPath("$.data.items[0].allocatedQuantity").value(0.00))
                .andReturn().getResponse().getContentAsString();

        PickListResponse pickResponse = objectMapper.readValue(
                objectMapper.readTree(createResp).get("data").toString(),
                PickListResponse.class
        );

        // 2. Test Idempotency: duplicate request should return existing pick list
        mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(pickResponse.id()))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));

        // 3. Prevent duplicate active pick lists for same SO + location
        PickListRequest duplicateActiveReq = new PickListRequest(
                globalCompany.getId(),
                approvedOrder.getId(),
                warehouse1.getId(),
                null,
                UUID.randomUUID()
        );
        mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateActiveReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("An active pick list already exists for this sales order at the specified warehouse"));

        // 4. Release Pick List -> Allocates stock
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/release"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RELEASED"))
                .andExpect(jsonPath("$.data.items[0].allocatedQuantity").value(10.00))
                .andExpect(jsonPath("$.data.releasedByUserName").value("System"))
                .andExpect(jsonPath("$.data.releasedAt").isNotEmpty());

        // Verify stock is reserved in DB
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(stock.getQuantity()));
        assertEquals(0, BigDecimal.valueOf(10.00).compareTo(stock.getReservedQuantity()));

        // Verify Inventory Allocation is created
        List<InventoryAllocation> allocations = inventoryAllocationRepository.findByPickListId(pickResponse.id());
        assertEquals(1, allocations.size());
        assertEquals(AllocationStatus.ACTIVE, allocations.get(0).getAllocationStatus());
        assertEquals(0, BigDecimal.valueOf(10.00).compareTo(allocations.get(0).getAllocatedQuantity()));

        // 5. Direct Ship Blocker: Block ship from RELEASED (must be PACKED)
        ShipRequest shipReq = new ShipRequest(List.of(new ShipRequest.ItemShipmentUpdate(product1.getId(), BigDecimal.valueOf(10.00))));
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shipReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Pick list must be in PACKED status to ship"));

        // 6. Start Picking
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/start-picking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PICKING"));

        // 7. Complete Picking with validation checks
        CompletePickingRequest pickOverQty = new CompletePickingRequest(List.of(
                new CompletePickingRequest.ItemPickingUpdate(product1.getId(), new BigDecimal("12.00"))
        ));
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/complete-picking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pickOverQty)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cannot exceed allocated quantity")));

        CompletePickingRequest pickValid = new CompletePickingRequest(List.of(
                new CompletePickingRequest.ItemPickingUpdate(product1.getId(), BigDecimal.valueOf(10.00))
        ));
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/complete-picking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pickValid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PICKED"))
                .andExpect(jsonPath("$.data.items[0].pickedQuantity").value(10.00))
                .andExpect(jsonPath("$.data.pickedByUserName").value("System"))
                .andExpect(jsonPath("$.data.pickedAt").isNotEmpty());

        // 8. Pack pick list
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/pack"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PACKED"))
                .andExpect(jsonPath("$.data.packedByUserName").value("System"))
                .andExpect(jsonPath("$.data.packedAt").isNotEmpty());

        // 9. Ship Pick List
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shipReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SHIPPED"))
                .andExpect(jsonPath("$.data.items[0].shippedQuantity").value(10.00))
                .andExpect(jsonPath("$.data.shippedByUserName").value("System"))
                .andExpect(jsonPath("$.data.shippedAt").isNotEmpty());

        // Verify stock is decremented and reservation is cleared in DB
        stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(90.00).compareTo(stock.getQuantity()));
        assertEquals(0, BigDecimal.ZERO.compareTo(stock.getReservedQuantity()));

        // Verify Inventory Allocation is marked as CONSUMED
        allocations = inventoryAllocationRepository.findByPickListId(pickResponse.id());
        assertEquals(1, allocations.size());
        assertEquals(AllocationStatus.CONSUMED, allocations.get(0).getAllocationStatus());

        // Verify Sales Order item is fulfilled and status is updated
        SalesOrder salesOrder = salesOrderRepository.findById(approvedOrder.getId()).get();
        assertEquals(SalesOrderStatus.FULFILLED, salesOrder.getStatus());
        assertEquals(0, BigDecimal.valueOf(10.00).compareTo(salesOrder.getItems().get(0).getFulfilledQuantity()));
        assertEquals(0, BigDecimal.ZERO.compareTo(salesOrder.getItems().get(0).getAllocatedQuantity()));

        // Verify Stock Movements were logged
        List<StockMovement> movements = stockMovementRepository.findAll().stream()
                .filter(m -> m.getReferenceNo().equals(pickResponse.pickNumber()))
                .toList();
        assertTrue(movements.stream().anyMatch(m -> m.getMovementType().equals("SALES_RESERVE")));
        assertTrue(movements.stream().anyMatch(m -> m.getMovementType().equals("SALES_SHIPMENT")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PICK_LIST_CREATE", "PICK_LIST_RELEASE", "PICK_LIST_CANCEL", "PICK_LIST_VIEW"
    })
    public void testCancellationWorkflowAndRollback() throws Exception {
        UUID clientRef = UUID.randomUUID();

        // 1. Create & Release Pick List
        PickListRequest createReq = new PickListRequest(
                globalCompany.getId(),
                approvedOrder.getId(),
                warehouse1.getId(),
                null,
                clientRef
        );

        String createResp = mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PickListResponse pickResponse = objectMapper.readValue(
                objectMapper.readTree(createResp).get("data").toString(),
                PickListResponse.class
        );

        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/release"))
                .andExpect(status().isOk());

        // Reserved stock is now 10
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(10.00).compareTo(stock.getReservedQuantity()));

        // Cancel the pick list
        SalesOrderCancelRequest cancelReq = new SalesOrderCancelRequest("Damaged goods");
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancellationReason").value("Damaged goods"))
                .andExpect(jsonPath("$.data.cancelledByUserName").value("System"))
                .andExpect(jsonPath("$.data.cancelledAt").isNotEmpty());

        // Verify reserved stock is rolled back to 0
        stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.ZERO.compareTo(stock.getReservedQuantity()));

        // Verify SalesOrder item allocated quantity is restored to 0
        SalesOrder salesOrder = salesOrderRepository.findById(approvedOrder.getId()).get();
        assertEquals(0, BigDecimal.ZERO.compareTo(salesOrder.getItems().get(0).getAllocatedQuantity()));

        // Verify Inventory Allocation is marked as CANCELLED
        List<InventoryAllocation> allocations = inventoryAllocationRepository.findByPickListId(pickResponse.id());
        assertEquals(1, allocations.size());
        assertEquals(AllocationStatus.CANCELLED, allocations.get(0).getAllocationStatus());

        // Verify SALES_RELEASE movement was logged
        List<StockMovement> movements = stockMovementRepository.findAll().stream()
                .filter(m -> m.getReferenceNo().equals(pickResponse.pickNumber()))
                .toList();
        assertTrue(movements.stream().anyMatch(m -> m.getMovementType().equals("SALES_RELEASE")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PICK_LIST_CREATE", "PICK_LIST_RELEASE", "PICK_LIST_VIEW"
    })
    public void testPartialAllocationSupport() throws Exception {
        // Set physical stock to 4 units (which is less than the requested 10 units)
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        stock.setQuantity(BigDecimal.valueOf(4.00));
        stock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(stock);

        PickListRequest createReq = new PickListRequest(
                globalCompany.getId(),
                approvedOrder.getId(),
                warehouse1.getId(),
                null,
                UUID.randomUUID()
        );

        String createResp = mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PickListResponse pickResponse = objectMapper.readValue(
                objectMapper.readTree(createResp).get("data").toString(),
                PickListResponse.class
        );

        // Release Pick List -> Should allocate only 4 units
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/release"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].allocatedQuantity").value(4.00));

        // Verify reservations in DB
        stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(4.00).compareTo(stock.getReservedQuantity()));

        SalesOrder salesOrder = salesOrderRepository.findById(approvedOrder.getId()).get();
        assertEquals(0, BigDecimal.valueOf(4.00).compareTo(salesOrder.getItems().get(0).getAllocatedQuantity()));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PICK_LIST_CREATE", "PICK_LIST_RELEASE", "PICK_LIST_PICK",
            "PICK_LIST_PACK", "PICK_LIST_SHIP", "PICK_LIST_VIEW"
    })
    public void testPartialShipmentAndSalesOrderProgression() throws Exception {
        PickListRequest createReq = new PickListRequest(
                globalCompany.getId(),
                approvedOrder.getId(),
                warehouse1.getId(),
                null,
                UUID.randomUUID()
        );

        String createResp = mockMvc.perform(post("/api/v1/pick-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PickListResponse pickResponse = objectMapper.readValue(
                objectMapper.readTree(createResp).get("data").toString(),
                PickListResponse.class
        );

        // 1. Release, pick (10), pack
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/release")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/start-picking")).andExpect(status().isOk());
        
        CompletePickingRequest pickAll = new CompletePickingRequest(List.of(
                new CompletePickingRequest.ItemPickingUpdate(product1.getId(), BigDecimal.valueOf(10.00))
        ));
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/complete-picking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pickAll))).andExpect(status().isOk());
        
        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/pack")).andExpect(status().isOk());

        // 2. Ship partial: 4 units (instead of 10 picked)
        ShipRequest partialShip = new ShipRequest(List.of(
                new ShipRequest.ItemShipmentUpdate(product1.getId(), BigDecimal.valueOf(4.00))
        ));

        mockMvc.perform(post("/api/v1/pick-lists/" + pickResponse.id() + "/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialShip)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SHIPPED"))
                .andExpect(jsonPath("$.data.items[0].shippedQuantity").value(4.00));

        // 3. Verify stock changes: 4 deducted, reservation of 10 is completely cleared
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product1.getId(), warehouse1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(96.00).compareTo(stock.getQuantity()));
        assertEquals(0, BigDecimal.ZERO.compareTo(stock.getReservedQuantity()));

        // 4. Verify sales order status is now PARTIALLY_FULFILLED
        SalesOrder salesOrder = salesOrderRepository.findById(approvedOrder.getId()).get();
        assertEquals(SalesOrderStatus.PARTIALLY_FULFILLED, salesOrder.getStatus());
        assertEquals(0, BigDecimal.valueOf(4.00).compareTo(salesOrder.getItems().get(0).getFulfilledQuantity()));
        assertEquals(0, BigDecimal.ZERO.compareTo(salesOrder.getItems().get(0).getAllocatedQuantity()));

        // 5. Verify Inventory Allocations:
        // One consumed allocation for 4 units, and one released allocation for 6 units.
        List<InventoryAllocation> allocations = inventoryAllocationRepository.findByPickListId(pickResponse.id());
        assertTrue(allocations.stream().anyMatch(a -> a.getAllocationStatus() == AllocationStatus.CONSUMED 
                && BigDecimal.valueOf(4.00).compareTo(a.getAllocatedQuantity()) == 0));
        assertTrue(allocations.stream().anyMatch(a -> a.getAllocationStatus() == AllocationStatus.RELEASED 
                && BigDecimal.valueOf(6.00).compareTo(a.getAllocatedQuantity()) == 0));
    }
}
