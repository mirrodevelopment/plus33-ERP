/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : PaymentAllocationRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentAllocationController
 * Related Service   : PaymentAllocationService, PaymentAllocationServiceImpl
 * Related Repository: PaymentAllocationRepository
 * Related Entity    : PaymentAllocation
 * Related DTO       : PaymentAllocationRequest
 * Related Mapper    : PaymentAllocationMapper
 * Related DB Table  : payment_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentAllocationController, PaymentAllocationService, PaymentAllocationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentAllocationRequest}</p>
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
public class PaymentAllocationRequest {

    private Long supplierInvoiceId;

    private Long customerInvoiceId;

    @NotNull(message = "Allocated amount is required")
    @Positive(message = "Allocated amount must be greater than zero")
    private BigDecimal allocatedAmount;
}