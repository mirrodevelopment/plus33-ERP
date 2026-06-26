package com.plus33.erp.finance.assets.dto;

import java.util.List;

public record AssetMergeRequest(
    List<Long> sourceAssetIds,
    String targetName,
    String targetDescription
) {}
