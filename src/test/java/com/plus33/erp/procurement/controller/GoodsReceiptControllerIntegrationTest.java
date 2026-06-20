package com.plus33.erp.procurement.controller;

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
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.GoodsReceiptRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GoodsReceiptControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private InventoryStockRepository inventoryStockRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseOrder createIssuedPurchaseOrder(Company company, Supplier supplier, Product product, BigDecimal qty, BigDecimal price) {
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-TEST-GR-" + System.nanoTime());
        po.setOrderedBy(userRepository.findAll().get(0));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(10));
        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedAt(LocalDateTime.now());
        po.setIssuedBy(userRepository.findAll().get(0));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setProduct(product);
        item.setOrderedQuantity(qty);
        item.setUnitPrice(price);
        item.setReceivedQuantity(BigDecimal.ZERO);
        item.setRemainingQuantity(qty);
        item.setPurchaseOrder(po);

        po.getItems().add(item);
        po.setSubtotalAmount(qty.multiply(price));
        po.setTotalAmount(po.getSubtotalAmount());

        return purchaseOrderRepository.save(po);
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "GOODS_RECEIPT_CREATE", "GOODS_RECEIPT_VIEW", "GOODS_RECEIPT_UPDATE", "GOODS_RECEIPT_CANCEL"
    })
    public void testGoodsReceiptWorkflowValidationsAndIdempotency() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        Product product = productRepository.findAll().stream()
                .filter(Product::getActive)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active product found"));

        Supplier supplier = supplierRepository.findAll().stream()
                .filter(s -> s.getCompany().getId().equals(company.getId()) && s.getActive())
                .findFirst()
                .orElseGet(() -> {
                    Supplier s = new Supplier();
                    s.setCode("TEST_SUPP_GR_" + System.nanoTime());
                    s.setName("Test Supplier GR");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // 1. Create a valid ISSUED PO
        PurchaseOrder po = createIssuedPurchaseOrder(company, supplier, product, BigDecimal.valueOf(100), BigDecimal.valueOf(5.00));

        // Test Validation: PO status is Draft (should fail)
        PurchaseOrder draftPo = new PurchaseOrder();
        draftPo.setCompany(company);
        draftPo.setSupplier(supplier);
        draftPo.setOrderNumber("PO-DRAFT-GR-" + System.nanoTime());
        draftPo.setOrderedBy(userRepository.findAll().get(0));
        draftPo.setExpectedDeliveryDate(LocalDate.now().plusDays(5));
        draftPo.setStatus(PurchaseOrderStatus.DRAFT);
        draftPo = purchaseOrderRepository.save(draftPo);

        GoodsReceiptRequest draftPoRequest = new GoodsReceiptRequest(
                draftPo.getId(), company.getId(), warehouse.getId(), null,
                "Draft test", "DN-1", "INV-1", UUID.randomUUID(),
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.TEN, "Remarks"))
        );

        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draftPoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Goods receipts can only be recorded against ISSUED or PARTIALLY_RECEIVED")));

        // Test Validation: Company mismatch
        Company company2 = new Company();
        company2.setCode("COMP_TEST_GR_MISMATCH_" + System.nanoTime());
        company2.setName("Company Mismatch Test");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        GoodsReceiptRequest companyMismatchReq = new GoodsReceiptRequest(
                po.getId(), company2.getId(), warehouse.getId(), null,
                "Company mismatch", "DN-1", "INV-1", UUID.randomUUID(),
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.TEN, "Remarks"))
        );

        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyMismatchReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Goods receipt company does not match purchase order company"));

        // Test Validation: Product not present on PO
        Product product2 = new Product();
        product2.setCode("SKU_NOT_ON_PO_" + System.nanoTime());
        product2.setName("Unlinked Product");
        product2.setCategory(product.getCategory());
        product2.setUnit(product.getUnit());
        product2.setProductType(product.getProductType());
        product2.setActive(true);
        product2 = productRepository.save(product2);

        GoodsReceiptRequest wrongProductReq = new GoodsReceiptRequest(
                po.getId(), company.getId(), warehouse.getId(), null,
                "Wrong product", "DN-1", "INV-1", UUID.randomUUID(),
                List.of(new GoodsReceiptItemRequest(product2.getId(), BigDecimal.TEN, "Remarks"))
        );

        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongProductReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("is not present on the linked Purchase Order")));

        // Test Validation: Quantity exceeds remaining limit
        GoodsReceiptRequest overQtyReq = new GoodsReceiptRequest(
                po.getId(), company.getId(), warehouse.getId(), null,
                "Over Qty", "DN-1", "INV-1", UUID.randomUUID(),
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.valueOf(101), "Remarks"))
        );

        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overQtyReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("exceeds remaining quantity")));

        // 2. Perform a successful partial Goods Receipt
        UUID idempotencyKey = UUID.randomUUID();
        GoodsReceiptRequest partialReq = new GoodsReceiptRequest(
                po.getId(), company.getId(), warehouse.getId(), null,
                "Partial Receipt Remarks", "DN-DN-1", "INV-INV-1", idempotencyKey,
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.valueOf(40), "Line notes"))
        );

        String partialResponse = mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.totalQuantity").value(40.0))
                .andExpect(jsonPath("$.data.totalAmount").value(200.00)) // 40 * 5.00
                .andReturn().getResponse().getContentAsString();

        Long grId = objectMapper.readTree(partialResponse).path("data").path("id").asLong();

        // Verify PO was transitioned to PARTIALLY_RECEIVED (40 / 100 = 40%)
        PurchaseOrder updatedPo = purchaseOrderRepository.findById(po.getId()).orElseThrow();
        assertEquals(PurchaseOrderStatus.PARTIALLY_RECEIVED, updatedPo.getStatus());
        assertEquals(0, BigDecimal.valueOf(40).compareTo(updatedPo.getReceivedPercentage()));
        assertNull(updatedPo.getReceivedAt());

        // Verify Stock Movement was created correctly
        List<StockMovement> movements = stockMovementRepository.findAll().stream()
                .filter(m -> m.getReferenceNumber() != null && m.getReferenceNumber().equals(objectMapper.readTree(partialResponse).path("data").path("receiptNumber").asText()))
                .toList();
        assertFalse(movements.isEmpty());
        assertEquals(StockMovementReferenceType.GOODS_RECEIPT, movements.get(0).getReferenceType());
        assertEquals(0, BigDecimal.valueOf(40).compareTo(movements.get(0).getQuantity()));

        // 3. Test Idempotency (Repeat submission returns 200 OK and same data)
        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(grId));

        // Test Idempotency: Same UUID but different payload returns 409
        GoodsReceiptRequest partialReqChanged = new GoodsReceiptRequest(
                po.getId(), company.getId(), warehouse.getId(), null,
                "Partial Receipt Remarks Changed", "DN-DN-1", "INV-INV-1", idempotencyKey,
                List.of(new GoodsReceiptItemRequest(product.getId(), BigDecimal.valueOf(50), "Line notes"))
        );

        mockMvc.perform(post("/api/v1/goods-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialReqChanged)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Client reference ID already exists with different request data."));

        // 4. Test optimistic locking conflict endpoint mapping (returns 409)
        // We can verify this via GlobalExceptionHandler by hitting a mocked endpoint or triggering it.
        // Let's assert the handler mapping directly in mockMvc if needed, or by calling a bad update version check.

        // 5. Test Cancellation underflow prevention
        // Check current inventory stock level
        InventoryStock stock = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId()).orElseThrow();
        // Artificially reduce stock to trigger underflow detection
        stock.setQuantity(BigDecimal.TEN); // we received 40, but now we only have 10 in stock
        inventoryStockRepository.save(stock);

        mockMvc.perform(post("/api/v1/goods-receipts/" + grId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Reverting receipt"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Inventory underflow detected. Cannot cancel goods receipt."));

        // Restore stock to allow successful cancellation
        stock = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId()).orElseThrow();
        stock.setQuantity(BigDecimal.valueOf(100));
        inventoryStockRepository.save(stock);

        // Cancel Goods Receipt successfully
        mockMvc.perform(post("/api/v1/goods-receipts/" + grId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasonRequest("Valid cancel reason"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancelledAt").isNotEmpty());

        // Verify PO reverted back to ISSUED
        updatedPo = purchaseOrderRepository.findById(po.getId()).orElseThrow();
        assertEquals(PurchaseOrderStatus.ISSUED, updatedPo.getStatus());
        assertEquals(0, BigDecimal.ZERO.compareTo(updatedPo.getReceivedPercentage()));

        // Verify reverse Stock Movement (negative quantity)
        List<StockMovement> revMovements = stockMovementRepository.findAll().stream()
                .filter(m -> m.getReferenceType() == StockMovementReferenceType.GOODS_RECEIPT_CANCEL)
                .toList();
        assertFalse(revMovements.isEmpty());
        assertEquals(0, BigDecimal.valueOf(-40).compareTo(revMovements.get(0).getQuantity()));
    }
}
