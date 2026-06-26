package com.plus33.erp.finance.assets.dto;

public record AssetAuditItemRequest(
    Long fixedAssetId,
    String result, // FOUND_OK, DAMAGED, MISSING
    String remarks,
    String photoEvidenceUrl
) {}
