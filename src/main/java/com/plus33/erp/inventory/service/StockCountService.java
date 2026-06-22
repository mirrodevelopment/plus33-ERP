package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import org.springframework.data.domain.Pageable;

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
