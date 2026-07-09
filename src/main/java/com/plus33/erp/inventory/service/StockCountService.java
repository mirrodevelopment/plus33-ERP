/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : StockCountService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : PageResponse, searchRequest, StockCountRequest, StockCountResponse, StockCountSearchRequest
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StockCountService {
    IdempotentCreateResult<StockCountResponse> createCount(StockCountRequest request);
    StockCountResponse updateCount(Long id, StockCountUpdateRequest request);
    StockCountResponse getCountById(Long id);
    PageResponse<StockCountResponse> searchCounts(StockCountSearchRequest searchRequest, Pageable pageable);
    StockCountResponse assignCount(Long id, Long userId);
    StockCountResponse startCount(Long id);
    StockCountResponse submitCount(Long id, StockCountSubmitRequest request);
    StockCountResponse rejectCount(Long id, String reason);
    StockCountResponse reopenCount(Long id);
    StockCountResponse approveCount(Long id);
    StockCountResponse postCount(Long id);
    StockCountResponse closeCount(Long id);
}
