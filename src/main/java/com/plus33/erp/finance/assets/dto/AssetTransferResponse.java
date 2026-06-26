package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record AssetTransferResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    LocalDate transferDate,
    Long fromWarehouseId,
    String fromWarehouseName,
    Long fromStoreId,
    String fromStoreName,
    Long toWarehouseId,
    String toWarehouseName,
    Long toStoreId,
    String toStoreName,
    Long toCompanyId,
    String toCompanyName,
    String status,
    String reason,
    String performedBy
) {}
