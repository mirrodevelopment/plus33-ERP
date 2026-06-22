package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryLotStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record InventoryLotRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotBlank(message = "Lot number is required")
        String lotNumber,

        @NotNull(message = "Expiry date is required")
        LocalDate expiryDate,

        LocalDate manufacturedDate,

        InventoryLotStatus status
) {}
