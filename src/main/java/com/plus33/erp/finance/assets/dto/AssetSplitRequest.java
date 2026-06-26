package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.util.List;

public record AssetSplitRequest(
    List<SplitTarget> targets
) {
    public record SplitTarget(
        String name,
        String description,
        BigDecimal allocationRatio  // e.g. 0.80 for 80%
    ) {}
}
