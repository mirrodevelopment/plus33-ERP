/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : InventoryTransferService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferController
 * Related Service   : InventoryTransferService, InventoryTransferServiceImpl
 * Related Repository: InventoryTransferRepository
 * Related Entity    : InventoryTransfer
 * Related DTO       : InventoryTransferRequest, InventoryTransferResponse, InventoryTransferSearchRequest, InventoryTransferUpdateRequest, PageResponse
 * Related Mapper    : InventoryTransferMapper
 * Related DB Table  : inventory_transfers
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTransferService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryTransferService {
    IdempotentCreateResult<InventoryTransferResponse> createTransfer(InventoryTransferRequest request);
    InventoryTransferResponse updateTransfer(Long id, InventoryTransferUpdateRequest request);
    InventoryTransferResponse getTransferById(Long id);
    PageResponse<InventoryTransferResponse> searchTransfers(InventoryTransferSearchRequest searchRequest, Pageable pageable);
    InventoryTransferResponse submitTransfer(Long id);
    InventoryTransferResponse approveTransfer(Long id);
    InventoryTransferResponse dispatchTransfer(Long id);
    InventoryTransferResponse receiveTransfer(Long id);
    InventoryTransferResponse cancelTransfer(Long id, String reason);
}
