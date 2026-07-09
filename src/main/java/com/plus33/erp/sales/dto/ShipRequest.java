/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : ShipRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShipController
 * Related Service   : ShipService, ShipServiceImpl
 * Related Repository: ShipRepository
 * Related Entity    : Ship
 * Related DTO       : ShipRequest
 * Related Mapper    : ShipMapper
 * Related DB Table  : ships
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShipController, ShipService, ShipServiceImpl
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
 * <p><b>Class  :</b> {@code ShipRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ShipRequest(
    @NotEmpty(message = "Shipment items list cannot be empty")
    List<@Valid ItemShipmentUpdate> items
) {
    public record ItemShipmentUpdate(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Shipped quantity is required")
        @PositiveOrZero(message = "Shipped quantity must be greater than or equal to zero")
        BigDecimal shippedQuantity
    ) {}
}
