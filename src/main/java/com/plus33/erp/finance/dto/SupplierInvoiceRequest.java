/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : SupplierInvoiceRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceService, SupplierInvoiceServiceImpl
 * Related Repository: SupplierInvoiceRepository
 * Related Entity    : SupplierInvoice
 * Related DTO       : SupplierInvoiceItemRequest, SupplierInvoiceRequest
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInvoiceRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    private Long purchaseOrderId;

    @NotBlank(message = "Invoice number is required")
    @Size(max = 100, message = "Invoice number cannot exceed 100 characters")
    private String invoiceNumber;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    private LocalDate dueDate;

    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode;

    @NotEmpty(message = "At least one invoice item is required")
    @Valid
    private List<SupplierInvoiceItemRequest> items;
}