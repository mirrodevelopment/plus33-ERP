package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import org.springframework.data.domain.Pageable;

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
