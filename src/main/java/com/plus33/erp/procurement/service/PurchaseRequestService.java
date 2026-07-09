/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : PurchaseRequestService.java
 * Purpose           : Service interface contract defining the API for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestController
 * Related Service   : PurchaseRequestService, PurchaseRequestServiceImpl
 * Related Repository: PurchaseRequestRepository
 * Related Entity    : PurchaseRequest
 * Related DTO       : approvePurchaseRequest, cancelPurchaseRequest, createPurchaseRequest, PageResponse, PurchaseRequestRequest
 * Related Mapper    : PurchaseRequestMapper
 * Related DB Table  : purchase_requests
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Procurement Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.PurchaseRequestRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestResponse;
import com.plus33.erp.procurement.dto.PurchaseRequestSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
