package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;
import java.util.List;

public record AssetAuditRequest(
    LocalDate auditDate,
    String auditorName,
    Long warehouseId,
    Long storeId,
    List<AssetAuditItemRequest> items
) {}
