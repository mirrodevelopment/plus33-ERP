package com.plus33.erp.finance.dto;

import com.plus33.erp.finance.entity.SupplierInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInvoiceResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private Long supplierId;
    private String supplierName;
    private Long purchaseOrderId;
    private String purchaseOrderNumber;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal outstandingBalance;
    private SupplierInvoiceStatus status;
    private String currencyCode;
    private Long journalEntryId;
    private String journalEntryNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SupplierInvoiceItemResponse> items;
}
