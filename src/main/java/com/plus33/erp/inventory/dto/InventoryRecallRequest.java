package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRecallRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        Long lotId,

        Long serialId,

        @NotBlank(message = "Recall reason is required")
        String recallReason,

        String recallReferenceNumber
) {}
