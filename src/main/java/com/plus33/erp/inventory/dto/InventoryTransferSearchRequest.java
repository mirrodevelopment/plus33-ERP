/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryTransferSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferSearchController
 * Related Service   : InventoryTransferSearchService, InventoryTransferSearchServiceImpl
 * Related Repository: InventoryTransferSearchRepository
 * Related Entity    : InventoryTransferSearch
 * Related DTO       : InventoryTransferSearchRequest
 * Related Mapper    : InventoryTransferSearchMapper
 * Related DB Table  : inventory_transfer_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTransferSearchController, InventoryTransferSearchService, InventoryTransferSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryTransferStatus;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTransferSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Builder
public record InventoryTransferSearchRequest(
        InventoryTransferStatus status,
        Long companyId,
        Long sourceWarehouseId,
        Long sourceStoreId,
        Long destWarehouseId,
        Long destStoreId,
        String transferNumber,
        UUID clientReferenceId,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Long createdBy,
        Long productId
) {}