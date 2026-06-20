package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request);
    PurchaseOrderResponse getPurchaseOrderById(Long id);
    PageResponse<PurchaseOrderResponse> searchPurchaseOrders(PurchaseOrderSearchRequest searchRequest, Pageable pageable);
    PurchaseOrderResponse updatePurchaseOrder(Long id, PurchaseOrderRequest request);
    void deletePurchaseOrder(Long id);
    PurchaseOrderResponse approvePurchaseOrder(Long id);
    PurchaseOrderResponse cancelPurchaseOrder(Long id, String reason);
    PurchaseOrderResponse closePurchaseOrder(Long id);
}
