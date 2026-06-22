package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import org.springframework.data.domain.Pageable;

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
