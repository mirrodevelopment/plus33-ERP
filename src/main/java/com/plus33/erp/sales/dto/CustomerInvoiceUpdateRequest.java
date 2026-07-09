/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerInvoiceUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceUpdateController
 * Related Service   : CustomerInvoiceUpdateService, CustomerInvoiceUpdateServiceImpl
 * Related Repository: CustomerInvoiceUpdateRepository
 * Related Entity    : CustomerInvoiceUpdate
 * Related DTO       : CustomerInvoiceItemRequest, CustomerInvoiceUpdateRequest
 * Related Mapper    : CustomerInvoiceUpdateMapper
 * Related DB Table  : customer_invoice_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceUpdateController, CustomerInvoiceUpdateService, CustomerInvoiceUpdateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceUpdateRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record CustomerInvoiceUpdateRequest(
        @NotNull(message = "Invoice date is required")
        LocalDate invoiceDate,

        LocalDate dueDate,

        @Size(max = 3, message = "Currency code must be 3 characters")
        String currencyCode,

        @NotEmpty(message = "Invoice must contain at least one item")
        @Valid
        List<CustomerInvoiceItemRequest> items
) {}
