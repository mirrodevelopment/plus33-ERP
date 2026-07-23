/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : StoreService.java
 * Purpose           : Service interface contract defining the API for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreService, StoreServiceImpl
 * Related Repository: StoreRepository
 * Related Entity    : Store
 * Related DTO       : PageResponse, searchRequest, StoreRequest, StoreResponse, StoreSearchRequest
 * Related Mapper    : StoreMapper
 * Related DB Table  : stores
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Organization Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.StoreSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code StoreService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StoreService {
    StoreResponse createStore(StoreRequest request);
    StoreResponse getStoreById(Long id);
    PageResponse<StoreResponse> searchStores(StoreSearchRequest searchRequest, Pageable pageable);
    StoreResponse updateStore(Long id, StoreRequest request);
    void deleteStore(Long id);
    StoreResponse activateStore(Long id);
    StoreResponse deactivateStore(Long id);
    
    com.plus33.erp.organization.dto.StoreSettingResponse getStoreSettings(Long storeId);
    com.plus33.erp.organization.dto.StoreSettingResponse updateStoreSettings(Long storeId, com.plus33.erp.organization.dto.StoreSettingRequest request);

    com.plus33.erp.organization.dto.StoreDocumentResponse uploadDocument(Long storeId, String documentType, String documentName, String filePath);
    void deleteDocument(Long storeId, Long documentId);
    java.util.List<com.plus33.erp.organization.dto.StoreDocumentResponse> getDocuments(Long storeId);
}
