package com.plus33.erp.analytics.controller;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.*;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProcurementAnalyticsControllerIntegrationTest {

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
    private SupplierInvoiceRepository supplierInvoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalyticsRefreshService analyticsRefreshService;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"ANALYTICS_VIEW"})
    public void testProcurementAnalyticsEndpoints() throws Exception {
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
                    s.setCode("SUPP_ANALYTICS_TEST_" + System.nanoTime());
                    s.setName("Supplier Analytics Test");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // Create a Purchase Order
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-ANA-TEST-" + System.nanoTime());
        po.setOrderedBy(userRepository.findAll().get(0));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(5));
        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedAt(LocalDateTime.now());
        po.setIssuedBy(userRepository.findAll().get(0));

        PurchaseOrderItem poItem = new PurchaseOrderItem();
        poItem.setProduct(product);
        poItem.setOrderedQuantity(BigDecimal.TEN);
        poItem.setUnitPrice(BigDecimal.valueOf(50.00));
        poItem.setReceivedQuantity(BigDecimal.TEN);
        poItem.setRemainingQuantity(BigDecimal.ZERO);
        poItem.setInvoicedQuantity(BigDecimal.TEN);
        poItem.setPurchaseOrder(po);

        po.getItems().add(poItem);
        po.setSubtotalAmount(BigDecimal.valueOf(500.00));
        po.setTotalAmount(BigDecimal.valueOf(500.00));
        purchaseOrderRepository.save(po);

        // Create an approved Supplier Invoice of amount 500
        SupplierInvoice invoice = SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .purchaseOrder(po)
                .invoiceNumber("INV-ANA-001")
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .subtotalAmount(BigDecimal.valueOf(500.00))
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(500.00))
                .paidAmount(BigDecimal.ZERO)
                .outstandingBalance(BigDecimal.valueOf(500.00))
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build();

        SupplierInvoiceItem item = SupplierInvoiceItem.builder()
                .supplierInvoice(invoice)
                .purchaseOrderItem(poItem)
                .product(product)
                .quantity(BigDecimal.TEN)
                .unitPrice(poItem.getUnitPrice())
                .netAmount(BigDecimal.valueOf(500.00))
                .taxRate(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.valueOf(500.00))
                .build();
        invoice.getItems().add(item);
        supplierInvoiceRepository.save(invoice);

        // Refresh views to capture test data
        analyticsRefreshService.refreshAll();

        // 1. Verify Summary endpoint
        mockMvc.perform(get("/api/v1/analytics/procurement/summary")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.totalPurchaseOrders").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));

        // 2. Verify Suppliers endpoint
        mockMvc.perform(get("/api/v1/analytics/procurement/suppliers")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].supplierId").value(org.hamcrest.Matchers.hasItem(supplier.getId().intValue())));

        // 3. Verify Payables Aging endpoint
        mockMvc.perform(get("/api/v1/analytics/procurement/payables-aging")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].totalOutstanding").value(org.hamcrest.Matchers.hasItem(500.00)));

        // 4. Verify Purchase Orders endpoint
        mockMvc.perform(get("/api/v1/analytics/procurement/purchase-orders")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].purchaseOrderId").value(org.hamcrest.Matchers.hasItem(po.getId().intValue())));

        // 5. Verify Invoice Matching endpoint
        mockMvc.perform(get("/api/v1/analytics/procurement/invoice-matching")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].supplierInvoiceId").value(org.hamcrest.Matchers.hasItem(invoice.getId().intValue())));
    }
}
