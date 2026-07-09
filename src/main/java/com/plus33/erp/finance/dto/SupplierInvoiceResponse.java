/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : SupplierInvoiceResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceService, SupplierInvoiceServiceImpl
 * Related Repository: SupplierInvoiceRepository
 * Related Entity    : SupplierInvoice
 * Related DTO       : SupplierInvoiceItemResponse, SupplierInvoiceResponse
 * Related Mapper    : SupplierInvoiceMapper
 * Related DB Table  : supplier_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceController, SupplierInvoiceService, SupplierInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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