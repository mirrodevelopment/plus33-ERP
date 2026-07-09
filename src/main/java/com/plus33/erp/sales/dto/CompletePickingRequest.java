/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CompletePickingRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompletePickingController
 * Related Service   : CompletePickingService, CompletePickingServiceImpl
 * Related Repository: CompletePickingRepository
 * Related Entity    : CompletePicking
 * Related DTO       : CompletePickingRequest
 * Related Mapper    : CompletePickingMapper
 * Related DB Table  : complete_pickings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompletePickingController, CompletePickingService, CompletePickingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CompletePickingRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record CompletePickingRequest(
    @NotEmpty(message = "Picking items list cannot be empty")
    List<@Valid ItemPickingUpdate> items
) {
    public record ItemPickingUpdate(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Picked quantity is required")
        @PositiveOrZero(message = "Picked quantity must be greater than or equal to zero")
        BigDecimal pickedQuantity
    ) {}
}
