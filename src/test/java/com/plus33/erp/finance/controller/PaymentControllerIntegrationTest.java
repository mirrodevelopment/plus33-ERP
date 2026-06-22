package com.plus33.erp.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import com.plus33.erp.procurement.entity.Supplier;
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
public class PaymentControllerIntegrationTest {

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
    private PaymentRepository paymentRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplierInvoice createApprovedInvoice(Company company, Supplier supplier, Product product, BigDecimal amount, String invoiceNumber) {
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-PAY-TEST-" + System.nanoTime());
        po.setOrderedBy(userRepository.findAll().get(0));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(5));
        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedAt(LocalDateTime.now());
        po.setIssuedBy(userRepository.findAll().get(0));

        PurchaseOrderItem poItem = new PurchaseOrderItem();
        poItem.setProduct(product);
        poItem.setOrderedQuantity(BigDecimal.TEN);
        poItem.setUnitPrice(amount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
        poItem.setReceivedQuantity(BigDecimal.TEN);
        poItem.setRemainingQuantity(BigDecimal.ZERO);
        poItem.setInvoicedQuantity(BigDecimal.TEN);
        poItem.setPurchaseOrder(po);

        po.getItems().add(poItem);
        po.setSubtotalAmount(amount);
        po.setTotalAmount(amount);
        purchaseOrderRepository.save(po);

        SupplierInvoice invoice = SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .purchaseOrder(po)
                .invoiceNumber(invoiceNumber)
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .subtotalAmount(amount)
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(amount)
                .paidAmount(BigDecimal.ZERO)
                .outstandingBalance(amount)
                .status(SupplierInvoiceStatus.APPROVED)
                .currencyCode("AED")
                .build();

        SupplierInvoiceItem item = SupplierInvoiceItem.builder()
                .supplierInvoice(invoice)
                .purchaseOrderItem(poItem)
                .product(product)
                .quantity(BigDecimal.TEN)
                .unitPrice(poItem.getUnitPrice())
                .netAmount(amount)
                .taxRate(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(amount)
                .build();
        invoice.getItems().add(item);

        return supplierInvoiceRepository.save(invoice);
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "PAYMENT_CREATE", "PAYMENT_VIEW", "PAYMENT_CANCEL"
    })
    public void testPaymentWorkflowAndValidations() throws Exception {
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
                    s.setCode("SUPP_PAY_TEST_" + System.nanoTime());
                    s.setName("Supplier Pay Test");
                    s.setCompany(company);
                    s.setActive(true);
                    return supplierRepository.save(s);
                });

        // 1. Create an approved Supplier Invoice of amount 500
        SupplierInvoice invoice = createApprovedInvoice(company, supplier, product, BigDecimal.valueOf(500.00), "INV-PAY-001");

        // 2. Validate Creation: Company mismatch checks
        Company company2 = new Company();
        company2.setCode("COMP_TEST_PAY_MISMATCH_" + System.nanoTime());
        company2.setName("Company Pay Mismatch Test");
        company2 = companyRepository.save(company2);

        PaymentRequest mismatchCompanyRequest = PaymentRequest.builder()
                .companyId(company2.getId())
                .supplierId(supplier.getId())
                .paymentDate(LocalDate.now())
                .paymentMethod("BANK_TRANSFER")
                .amount(BigDecimal.valueOf(200.00))
                .referenceNumber("REF-001")
                .currencyCode("AED")
                .allocations(List.of(PaymentAllocationRequest.builder()
                        .supplierInvoiceId(invoice.getId())
                        .allocatedAmount(BigDecimal.valueOf(200.00))
                        .build()))
                .build();

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mismatchCompanyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("company")));

        // 3. Validate Creation: Allocation exceeds outstanding balance
        PaymentRequest overAllocRequest = PaymentRequest.builder()
                .companyId(company.getId())
                .supplierId(supplier.getId())
                .paymentDate(LocalDate.now())
                .paymentMethod("BANK_TRANSFER")
                .amount(BigDecimal.valueOf(600.00))
                .referenceNumber("REF-002")
                .currencyCode("AED")
                .allocations(List.of(PaymentAllocationRequest.builder()
                        .supplierInvoiceId(invoice.getId())
                        .allocatedAmount(BigDecimal.valueOf(600.00)) // 600 > 500 outstanding
                        .build()))
                .build();

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overAllocRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cannot exceed outstanding balance")));

        // 4. Create a valid partial payment of amount 200
        PaymentRequest partialRequest = PaymentRequest.builder()
                .companyId(company.getId())
                .supplierId(supplier.getId())
                .paymentDate(LocalDate.now())
                .paymentMethod("CASH")
                .amount(BigDecimal.valueOf(200.00))
                .referenceNumber("REF-SUCCESS-1")
                .currencyCode("AED")
                .allocations(List.of(PaymentAllocationRequest.builder()
                        .supplierInvoiceId(invoice.getId())
                        .allocatedAmount(BigDecimal.valueOf(200.00))
                        .build()))
                .build();

        String createResponseStr = mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.amount").value(200.00))
                .andExpect(jsonPath("$.data.journalEntryId").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Long paymentId = objectMapper.readTree(createResponseStr).get("data").get("id").asLong();

        // Verify invoice balance and status
        SupplierInvoice invoiceAfterPartial = supplierInvoiceRepository.findById(invoice.getId()).orElseThrow();
        assertEquals("PARTIALLY_PAID", invoiceAfterPartial.getStatus().name());
        assertEquals(0, invoiceAfterPartial.getPaidAmount().compareTo(BigDecimal.valueOf(200.00)));
        assertEquals(0, invoiceAfterPartial.getOutstandingBalance().compareTo(BigDecimal.valueOf(300.00)));

        // Verify Journal Entry created (Debit 2100, Credit 1100 since it is CASH)
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        assertNotNull(payment.getJournalEntry());
        JournalEntry je = payment.getJournalEntry();
        assertEquals("POSTED", je.getStatus());
        assertEquals(0, je.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("2100")).findFirst().orElseThrow().getDebitAmount().compareTo(BigDecimal.valueOf(200.00)));
        assertEquals(0, je.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("1100")).findFirst().orElseThrow().getCreditAmount().compareTo(BigDecimal.valueOf(200.00)));

        // 5. Verify Duplicate Reference Number constraint
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        // 6. Cancel the payment
        PaymentCancelRequest cancelRequest = PaymentCancelRequest.builder()
                .reason("Test Payment Reversal")
                .build();

        mockMvc.perform(post("/api/v1/payments/" + paymentId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"))
                .andExpect(jsonPath("$.data.cancellationReason").value("Test Payment Reversal"));

        // Verify invoice outstanding balance was restored and status reverted back to APPROVED
        SupplierInvoice invoiceAfterCancel = supplierInvoiceRepository.findById(invoice.getId()).orElseThrow();
        assertEquals("APPROVED", invoiceAfterCancel.getStatus().name());
        assertEquals(0, invoiceAfterCancel.getPaidAmount().compareTo(BigDecimal.ZERO));
        assertEquals(0, invoiceAfterCancel.getOutstandingBalance().compareTo(BigDecimal.valueOf(500.00)));

        // Verify reversing journal entry was posted
        JournalEntry originalJEUpdated = journalEntryRepository.findById(je.getId()).orElseThrow();
        assertNotNull(originalJEUpdated.getReversalEntry());
        JournalEntry reversalJE = originalJEUpdated.getReversalEntry();
        assertEquals("POSTED", reversalJE.getStatus());
        assertEquals(0, reversalJE.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("2100")).findFirst().orElseThrow().getCreditAmount().compareTo(BigDecimal.valueOf(200.00)));
        assertEquals(0, reversalJE.getLines().stream().filter(l -> l.getAccount().getAccountCode().equals("1100")).findFirst().orElseThrow().getDebitAmount().compareTo(BigDecimal.valueOf(200.00)));
    }
}
