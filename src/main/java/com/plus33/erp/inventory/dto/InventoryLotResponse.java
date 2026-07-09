/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryLotResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryLotController
 * Related Service   : InventoryLotService, InventoryLotServiceImpl
 * Related Repository: InventoryLotRepository
 * Related Entity    : InventoryLot
 * Related DTO       : InventoryLotResponse
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryLotResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryLotResponse(
        Long id,
        Long companyId,
        Long productId,
        String lotNumber,
        LocalDate expiryDate,
        LocalDate manufacturedDate,
        InventoryLotStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
