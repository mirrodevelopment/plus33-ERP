/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunRequest.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunService, PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository
 * Related Entity    : PaymentRun
 * Related DTO       : PaymentRunRequest
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunController, PaymentRunService, PaymentRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record PaymentRunRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Payment date is required")
        LocalDate paymentDate,

        @NotBlank(message = "Payment method is required")
        String paymentMethod,

        @NotBlank(message = "Currency code is required")
        String currencyCode,

        String bankAccountCode,

        LocalDate filterDueDate,

        Long filterSupplierId,

        String exportFormat,

        UUID clientReferenceId
) {}

