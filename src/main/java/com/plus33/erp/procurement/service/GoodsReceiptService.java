package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import org.springframework.data.domain.Pageable;

public interface GoodsReceiptService {
    IdempotentCreateResult<GoodsReceiptResponse> createGoodsReceipt(GoodsReceiptRequest request);
    GoodsReceiptResponse getGoodsReceiptById(Long id);
    PageResponse<GoodsReceiptResponse> searchGoodsReceipts(GoodsReceiptSearchRequest searchRequest, Pageable pageable);
    GoodsReceiptResponse updateGoodsReceipt(Long id, GoodsReceiptUpdateRequest request);
    GoodsReceiptResponse cancelGoodsReceipt(Long id, String reason);
}
