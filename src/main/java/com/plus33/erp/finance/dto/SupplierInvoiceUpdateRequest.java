/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : SupplierInvoiceUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceUpdateController
 * Related Service   : SupplierInvoiceUpdateService, SupplierInvoiceUpdateServiceImpl
 * Related Repository: SupplierInvoiceUpdateRepository
 * Related Entity    : SupplierInvoiceUpdate
 * Related DTO       : SupplierInvoiceItemRequest, SupplierInvoiceUpdateRequest
 * Related Mapper    : SupplierInvoiceUpdateMapper
 * Related DB Table  : supplier_invoice_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceUpdateController, SupplierInvoiceUpdateService, SupplierInvoiceUpdateServiceImpl
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
 * <p><b>Class  :</b> {@code SupplierInvoiceUpdateRequest}</p>
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
public class SupplierInvoiceUpdateRequest {

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