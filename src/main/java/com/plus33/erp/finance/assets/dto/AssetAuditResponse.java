package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;
import java.util.List;

public record AssetAuditResponse(
    Long id,
    LocalDate auditDate,
    String auditorName,
    Long warehouseId,
    String warehouseName,
    Long storeId,
    String storeName,
    List<AssetAuditItemResponse> items
) {}
