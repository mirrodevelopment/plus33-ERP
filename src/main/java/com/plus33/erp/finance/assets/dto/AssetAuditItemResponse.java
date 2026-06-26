package com.plus33.erp.finance.assets.dto;

public record AssetAuditItemResponse(
    Long id,
    Long fixedAssetId,
    String fixedAssetCode,
    String fixedAssetName,
    String result,
    String remarks,
    String photoEvidenceUrl
) {}
