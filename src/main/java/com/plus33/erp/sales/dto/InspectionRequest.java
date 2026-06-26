package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.InspectionResult;
import java.util.List;

public record InspectionRequest(
    List<ItemInspection> items,
    String remarks
) {
    public record ItemInspection(
        Long productId,
        InspectionResult result,
        String notes
    ) {}
}
