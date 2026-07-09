/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunInvoiceRequest.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunInvoiceController
 * Related Service   : PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 * Related Repository: PaymentRunInvoiceRepository
 * Related Entity    : PaymentRunInvoice
 * Related DTO       : PaymentRunInvoiceRequest
 * Related Mapper    : PaymentRunInvoiceMapper
 * Related DB Table  : payment_run_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunInvoiceController, PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunInvoiceRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record PaymentRunInvoiceRequest(
        @NotNull(message = "Supplier Invoice ID is required")
        Long supplierInvoiceId,

        @NotNull(message = "Payment amount is required")
        @DecimalMin(value = "0.00", message = "Payment amount must be greater than or equal to zero")
        BigDecimal paymentAmount,

        String paymentReference
) {}
