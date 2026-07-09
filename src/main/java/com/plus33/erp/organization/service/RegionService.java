/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : RegionService.java
 * Purpose           : Service interface contract defining the API for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionService, RegionServiceImpl
 * Related Repository: RegionRepository
 * Related Entity    : Region
 * Related DTO       : PageResponse, RegionRequest, RegionResponse, RegionSearchRequest, searchRequest
 * Related Mapper    : RegionMapper
 * Related DB Table  : regions
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
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.RegionSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code RegionService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RegionService {
    RegionResponse createRegion(RegionRequest request);
    RegionResponse getRegionById(Long id);
    PageResponse<RegionResponse> searchRegions(RegionSearchRequest searchRequest, Pageable pageable);
    RegionResponse updateRegion(Long id, RegionRequest request);
    void deleteRegion(Long id);
}
