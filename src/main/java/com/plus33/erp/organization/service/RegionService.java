package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.RegionSearchRequest;
import org.springframework.data.domain.Pageable;

public interface RegionService {
    RegionResponse createRegion(RegionRequest request);
    RegionResponse getRegionById(Long id);
    PageResponse<RegionResponse> searchRegions(RegionSearchRequest searchRequest, Pageable pageable);
    RegionResponse updateRegion(Long id, RegionRequest request);
    void deleteRegion(Long id);
}
