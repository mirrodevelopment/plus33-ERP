package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.StoreSearchRequest;
import org.springframework.data.domain.Pageable;

public interface StoreService {
    StoreResponse createStore(StoreRequest request);
    StoreResponse getStoreById(Long id);
    PageResponse<StoreResponse> searchStores(StoreSearchRequest searchRequest, Pageable pageable);
    StoreResponse updateStore(Long id, StoreRequest request);
    void deleteStore(Long id);
    StoreResponse activateStore(Long id);
    StoreResponse deactivateStore(Long id);
}
