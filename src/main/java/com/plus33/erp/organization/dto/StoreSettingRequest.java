package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Data Transfer Record containing update parameters for store settings.
 */
@Schema(description = "Store settings edit request payload")
public record StoreSettingRequest(
        @NotBlank(message = "Operating hours are required")
        @Schema(description = "Operating Hours", example = "08:00 - 22:00")
        String operatingHours,

        @NotBlank(message = "WiFi SSID is required")
        @Schema(description = "WiFi SSID", example = "PLUS33-Guest")
        String wifiSsid,

        @NotBlank(message = "WiFi password is required")
        @Schema(description = "WiFi Password", example = "CoffeeBreak")
        String wifiPassword,

        @NotNull(message = "Low stock threshold is required")
        @Min(value = 0, message = "Low stock threshold must be at least 0")
        @Schema(description = "Low stock alert threshold qty", example = "50")
        Integer lowStockThreshold,

        @NotNull(message = "Daily sales target is required")
        @Min(value = 0, message = "Daily sales target must be at least 0")
        @Schema(description = "Daily sales targets", example = "10000.00")
        BigDecimal salesTarget,

        @NotBlank(message = "Receipt footer is required")
        @Schema(description = "Receipt Footer Text", example = "Thank you for visiting PLUS33 Coffee!")
        String receiptFooter
) {}
