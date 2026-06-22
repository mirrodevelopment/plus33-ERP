package com.plus33.erp.inventory.service;

import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.InventorySerialStatus;
import com.plus33.erp.inventory.entity.InventoryTraceEventType;
import com.plus33.erp.inventory.entity.InventoryTraceReferenceType;

import java.math.BigDecimal;
import java.util.List;

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
