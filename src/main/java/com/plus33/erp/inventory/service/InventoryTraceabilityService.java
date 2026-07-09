/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : InventoryTraceabilityService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceabilityController
 * Related Service   : InventoryTraceabilityService, InventoryTraceabilityServiceImpl
 * Related Repository: InventoryTraceabilityRepository
 * Related Entity    : InventoryTraceability
 * Related DTO       : InventoryLotRequest, InventoryLotResponse, InventoryRecallRequest, InventoryRecallResponse, InventorySerialResponse
 * Related Mapper    : InventoryTraceabilityMapper
 * Related DB Table  : inventory_traceabilitys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.InventorySerialStatus;
import com.plus33.erp.inventory.entity.InventoryTraceEventType;
import com.plus33.erp.inventory.entity.InventoryTraceReferenceType;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTraceabilityService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryTraceabilityService {

    // Lot Management
    InventoryLotResponse createLot(InventoryLotRequest request);
    InventoryLotResponse getLotById(Long id);
    List<InventoryLotResponse> getAllLots();

    // Serial Management
    List<InventorySerialResponse> getAllSerials();
    void transitionSerialStatus(Long serialId, InventorySerialStatus newStatus, Long warehouseId, Long storeId);

    // Trace queries
    List<InventoryTraceEventResponse> getProductTrace(Long productId);
    List<InventoryTraceEventResponse> getLotTrace(String lotNumber);
    List<InventoryTraceEventResponse> getSerialTrace(String serialNumber);

    // Recall workflows
    InventoryRecallResponse createRecall(InventoryRecallRequest request);
    List<InventoryRecallResponse> getAllRecalls();

    // FEFO allocation queries
    List<InventoryLotResponse> getLotsForFefoAllocation(Long productId);

    // Expiry processing
    void processExpiredLots();

    // Utility for recording trace events
    void recordTraceEvent(
            Long companyId,
            Long productId,
            Long lotId,
            Long serialId,
            Long warehouseId,
            Long storeId,
            InventoryTraceEventType eventType,
            BigDecimal quantity,
            InventoryTraceReferenceType referenceType,
            Long referenceId,
            String referenceNumber,
            String notes
    );
}
