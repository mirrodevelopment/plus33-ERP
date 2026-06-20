package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.PurchaseRequestRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestResponse;
import com.plus33.erp.procurement.dto.PurchaseRequestSearchRequest;
import org.springframework.data.domain.Pageable;

public interface PurchaseRequestService {
    PurchaseRequestResponse createPurchaseRequest(PurchaseRequestRequest request);
    PurchaseRequestResponse getPurchaseRequestById(Long id);
    PageResponse<PurchaseRequestResponse> searchPurchaseRequests(PurchaseRequestSearchRequest searchRequest, Pageable pageable);
    PurchaseRequestResponse updatePurchaseRequest(Long id, PurchaseRequestRequest request);
    PurchaseRequestResponse submitPurchaseRequest(Long id);
    PurchaseRequestResponse approvePurchaseRequest(Long id);
    PurchaseRequestResponse rejectPurchaseRequest(Long id, String rejectionReason);
    PurchaseRequestResponse cancelPurchaseRequest(Long id, String cancellationReason);
    PurchaseRequestResponse convertPurchaseRequestToPo(Long id);
}
