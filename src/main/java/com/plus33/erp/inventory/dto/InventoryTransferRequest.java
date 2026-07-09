/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryTransferRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferController
 * Related Service   : InventoryTransferService, InventoryTransferServiceImpl
 * Related Repository: InventoryTransferRepository
 * Related Entity    : InventoryTransfer
 * Related DTO       : InventoryTransferItemRequest, InventoryTransferRequest
 * Related Mapper    : InventoryTransferMapper
 * Related DB Table  : inventory_transfers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTransferController, InventoryTransferService, InventoryTransferServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTransferRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryTransferRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        Long sourceWarehouseId,
        Long sourceStoreId,
        Long destWarehouseId,
        Long destStoreId,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        String remarks,

        @NotEmpty(message = "Transfer must contain at least one item")
        @Valid
        List<InventoryTransferItemRequest> items
) {}
