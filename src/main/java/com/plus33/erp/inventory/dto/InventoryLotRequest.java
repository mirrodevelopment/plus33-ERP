/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryLotRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryLotController
 * Related Service   : InventoryLotService, InventoryLotServiceImpl
 * Related Repository: InventoryLotRepository
 * Related Entity    : InventoryLot
 * Related DTO       : InventoryLotRequest
 * Related Mapper    : InventoryLotMapper
 * Related DB Table  : inventory_lots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryLotController, InventoryLotService, InventoryLotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryLotStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryLotRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryLotRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotBlank(message = "Lot number is required")
        String lotNumber,

        @NotNull(message = "Expiry date is required")
        LocalDate expiryDate,

        LocalDate manufacturedDate,

        InventoryLotStatus status
) {}
