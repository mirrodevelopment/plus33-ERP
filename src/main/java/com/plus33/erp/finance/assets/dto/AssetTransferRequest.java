package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record AssetTransferRequest(
    LocalDate transferDate,
    Long toWarehouseId,
    Long toStoreId,
    Long toCompanyId,
    String reason
) {}
