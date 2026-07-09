/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : InventoryAdjustmentService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentController
 * Related Service   : InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 * Related Repository: InventoryAdjustmentRepository
 * Related Entity    : InventoryAdjustment
 * Related DTO       : InventoryAdjustmentRequest, InventoryAdjustmentResponse, InventoryAdjustmentSearchRequest, InventoryAdjustmentUpdateRequest, PageResponse
 * Related Mapper    : InventoryAdjustmentMapper
 * Related DB Table  : inventory_adjustments
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
 * <p><b>Class  :</b> {@code InventoryAdjustmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryAdjustmentService {
    IdempotentCreateResult<InventoryAdjustmentResponse> createAdjustment(InventoryAdjustmentRequest request);
    InventoryAdjustmentResponse updateAdjustment(Long id, InventoryAdjustmentUpdateRequest request);
    InventoryAdjustmentResponse getAdjustmentById(Long id);
    PageResponse<InventoryAdjustmentResponse> searchAdjustments(InventoryAdjustmentSearchRequest searchRequest, Pageable pageable);
    InventoryAdjustmentResponse submitAdjustment(Long id);
    InventoryAdjustmentResponse approveAdjustment(Long id);
    InventoryAdjustmentResponse postAdjustment(Long id);
    InventoryAdjustmentResponse cancelAdjustment(Long id, String reason);
}
