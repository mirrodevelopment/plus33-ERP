package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Data Transfer Record representing store settings details.
 */
@Schema(description = "Store settings details response payload")
public record StoreSettingResponse(
        @Schema(description = "Database ID of the setting", example = "1")
        Long id,

        @Schema(description = "Store ID mapping", example = "1")
        Long storeId,

        @Schema(description = "Operating Hours", example = "08:00 - 22:00")
        String operatingHours,

        @Schema(description = "WiFi SSID", example = "PLUS33-Guest")
        String wifiSsid,

        @Schema(description = "WiFi Password", example = "CoffeeBreak")
        String wifiPassword,

        @Schema(description = "Low stock alert threshold qty", example = "50")
        Integer lowStockThreshold,

        @Schema(description = "Daily sales targets", example = "10000.00")
        BigDecimal salesTarget,

        @Schema(description = "Receipt Footer Text", example = "Thank you for visiting PLUS33 Coffee!")
        String receiptFooter
) {}
