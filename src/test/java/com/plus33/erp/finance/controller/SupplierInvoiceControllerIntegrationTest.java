package com.plus33.erp.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.PurchaseOrderItemRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SupplierInvoiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private SupplierInvoiceRepository supplierInvoiceRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseOrder createIssuedPOWithReceipt(Company company, Supplier supplier, Product product, BigDecimal orderedQty, BigDecimal receivedQty, BigDecimal price) {
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-INV-TEST-" + System.nanoTime());
        po.setOrderedBy(userRepository.findAll().get(0));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(5));
        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedAt(LocalDateTime.now());
        po.setIssuedBy(userRepository.findAll().get(0));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setProduct(product);
        item.setOrderedQuantity(orderedQty);
        item.setUnitPrice(price);
        item.setReceivedQuantity(receivedQty);
        item.setRemainingQuantity(orderedQty.subtract(receivedQty));
        item.setInvoicedQuantity(BigDecimal.ZERO);
        item.setPurchaseOrder(po);

        po.getItems().add(item);
        po.setSubtotalAmount(orderedQty.multiply(price));
        po.setTotalAmount(po.getSubtotalAmount());

        return purchaseOrderRepository.save(po);
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "SUPPLIER_INVOICE_CREATE", "SUPPLIER_INVOICE_VIEW", "SUPPLIER_INVOICE_UPDATE", "SUPPLIER_INVOICE_APPROVE", "SUPPLIER_INVOICE_CANCEL"
    })
    public void testSupplierInvoiceWorkflowAndValidations() throws Exception {
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
                    s.setCode("SUPP_INV_TEST_" + System.nanoTime());
                    s.setName("Supplier Inv Test");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // 1. Create a PO with 10 units received
        PurchaseOrder po = createIssuedPOWithReceipt(company, supplier, product, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(12.50));
        PurchaseOrderItem poItem = po.getItems().get(0);

        // 2. Validate Creation: Company mismatch checks
        Company company2 = new Company();
        company2.setCode("COMP_TEST_INV_MISMATCH_" + System.nanoTime());
        company2.setName("Company Inv Mismatch Test");
        company2 = companyRepository.save(company2);

        SupplierInvoiceRequest mismatchCompanyRequest = SupplierInvoiceRequest.builder()
                .companyId(company2.getId())
                .supplierId(supplier.getId())
                .purchaseOrderId(po.getId())
                .invoiceNumber("INV-TEST-001")
                .invoiceDate(LocalDate.now())
                .currencyCode("AED")
                .items(List.of(SupplierInvoiceItemRequest.builder()
                        .purchaseOrderItemId(poItem.getId())
                        .quantity(BigDecimal.valueOf(5))
                        .unitPrice(BigDecimal.valueOf(12.50))
                        .taxRate(BigDecimal.valueOf(5))
                        .discountAmount(BigDecimal.ZERO)
                        .build()))
                .build();

        mockMvc.perform(post("/api/v1/supplier-invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mismatchCompanyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("company")));

        // 3. Validate Creation: Invoice quantity exceeds received quantity
        SupplierInvoiceRequest overQtyRequest = SupplierInvoiceRequest.builder()
                .companyId(company.getId())
                .supplierId(supplier.getId())
                .purchaseOrderId(po.getId())
                .invoiceNumber("INV-TEST-002")
                .invoiceDate(LocalDate.now())
                .currencyCode("AED")
                .items(List.of(SupplierInvoiceItemRequest.builder()
                        .purchaseOrderItemId(poItem.getId())
                        .quantity(BigDecimal.valueOf(15)) // 15 > 10 received
                        .unitPrice(BigDecimal.valueOf(12.50))
                        .taxRate(BigDecimal.valueOf(5))
                        .discountAmount(BigDecimal.ZERO)
                        .build()))
                .build();

        mockMvc.perform(post("/api/v1/supplier-invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overQtyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("exceeds remaining received quantity")));

        // 4. Create a valid DRAFT invoice (5 units of product at 12.50 price)
        SupplierInvoiceRequest validRequest = SupplierInvoiceRequest.builder()
                .companyId(company.getId())
                .supplierId(supplier.getId())
                .purchaseOrderId(po.getId())
                .invoiceNumber("INV-TEST-SUCCESS")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .currencyCode("AED")
                .items(List.of(SupplierInvoiceItemRequest.builder()
                        .purchaseOrderItemId(poItem.getId())
                        .quantity(BigDecimal.valueOf(5))
                        .unitPrice(BigDecimal.valueOf(12.50))
                        .taxRate(BigDecimal.valueOf(5))
                        .discountAmount(BigDecimal.valueOf(2.50))
                        .build()))
                .build();

        String createResponseStr = mockMvc.perform(post("/api/v1/supplier-invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.subtotalAmount").value(62.50))
                .andExpect(jsonPath("$.data.taxAmount").value(3.13)) // 5% of 62.50 = 3.125 -> 3.13
                .andExpect(jsonPath("$.data.discountAmount").value(2.50))
                .andExpect(jsonPath("$.data.totalAmount").value(63.13)) // 62.50 + 3.13 - 2.50 = 63.13
                .andReturn().getResponse().getContentAsString();

        Long invoiceId = objectMapper.readTree(createResponseStr).get("data").get("id").asLong();

        // 5. Verify Duplicate Invoice Number checks
        mockMvc.perform(post("/api/v1/supplier-invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        // 6. Update the draft invoice (change quantity to 8 units)
        SupplierInvoiceUpdateRequest updateRequest = SupplierInvoiceUpdateRequest.builder()
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(15))
                .currencyCode("AED")
                .items(List.of(SupplierInvoiceItemRequest.builder()
                        .purchaseOrderItemId(poItem.getId())
                        .quantity(BigDecimal.valueOf(8))
                        .unitPrice(BigDecimal.valueOf(12.50))
                        .taxRate(BigDecimal.valueOf(5))
                        .discountAmount(BigDecimal.valueOf(4.00))
                        .build()))
                .build();

        mockMvc.perform(put("/api/v1/supplier-invoices/" + invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subtotalAmount").value(100.00)) // 8 * 12.50
                .andExpect(jsonPath("$.data.taxAmount").value(5.00)) // 5% of 100
                .andExpect(jsonPath("$.data.discountAmount").value(4.00))
                .andExpect(jsonPath("$.data.totalAmount").value(101.00)); // 100 + 5 - 4

        // 7. Approve the Supplier Invoice
        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.journalEntryId").isNotEmpty())
                .andExpect(jsonPath("$.data.journalEntryNumber").isNotEmpty());

        // Verify quantity was updated on the PO item
        PurchaseOrderItem poItemAfterApprove = purchaseOrderItemRepository.findById(poItem.getId()).orElseThrow();
        assertEquals(0, poItemAfterApprove.getInvoicedQuantity().compareTo(BigDecimal.valueOf(8)));

        // Verify Journal Entry was created and posted
        SupplierInvoice approvedInvoice = supplierInvoiceRepository.findById(invoiceId).orElseThrow();
        assertNotNull(approvedInvoice.getJournalEntry());
        JournalEntry je = approvedInvoice.getJournalEntry();
        assertEquals("POSTED", je.getStatus());
        assertEquals(0, je.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("2100")).findFirst().orElseThrow().getCreditAmount().compareTo(BigDecimal.valueOf(101.00)));

        // 8. Try updating approved invoice (should fail)
        mockMvc.perform(put("/api/v1/supplier-invoices/" + invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // 9. Cancel the approved Supplier Invoice
        mockMvc.perform(post("/api/v1/supplier-invoices/" + invoiceId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));

        // Verify PO item quantity was reverted
        PurchaseOrderItem poItemAfterCancel = purchaseOrderItemRepository.findById(poItem.getId()).orElseThrow();
        assertEquals(0, poItemAfterCancel.getInvoicedQuantity().compareTo(BigDecimal.ZERO));

        // Verify reversal Journal Entry was created
        JournalEntry originalJEUpdated = journalEntryRepository.findById(je.getId()).orElseThrow();
        assertNotNull(originalJEUpdated.getReversalEntry());
        JournalEntry reversalJE = originalJEUpdated.getReversalEntry();
        assertEquals("POSTED", reversalJE.getStatus());
        assertEquals(0, reversalJE.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("2100")).findFirst().orElseThrow().getDebitAmount().compareTo(BigDecimal.valueOf(101.00)));
    }
}
