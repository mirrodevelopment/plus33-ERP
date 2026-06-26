package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FixedAssetResponse(
    Long id,
    String assetCode,
    String name,
    String description,
    Long categoryId,
    String categoryCode,
    String categoryName,
    Long parentAssetId,
    String parentAssetCode,
    LocalDate acquisitionDate,
    BigDecimal acquisitionCost,
    BigDecimal salvageValue,
    Integer usefulLifeYears,
    BigDecimal depreciationRate,
    String depreciationMethod,
    String status,
    Long warehouseId,
    String warehouseName,
    Long storeId,
    String storeName,
    Long supplierInvoiceId,
    BigDecimal originalCost,
    BigDecimal currentBookValue,
    BigDecimal accumulatedDepreciation,
    LocalDate lastDepreciationDate,
    String qrCodeString,
    
    // Documents
    String purchaseInvoiceUrl,
    String warrantyDocUrl,
    String insuranceDocUrl,
    String photoUrl,
    String manualUrl,
    
    // Warranty
    LocalDate warrantyStartDate,
    LocalDate warrantyEndDate,
    String warrantyVendor,
    LocalDate amcExpiryDate,
    LocalDate amcRenewalDate,
    
    // Insurance
    String insurancePolicyNumber,
    String insuranceCompany,
    BigDecimal insurancePremium,
    LocalDate insuranceExpiryDate,
    BigDecimal insuredValue,
    
    // Assignment
    Long assignedEmployeeId,
    String assignedEmployeeName,
    String assignedDepartment,
    
    // CWIP & Budgeting
    Boolean isCwip,
    Long cwipAccountId,
    BigDecimal budgetedCost,
    BigDecimal actualCost,
    
    // Barcode/NFC
    String barcodeOrNfcTag,
    
    // Advanced Warranty
    String warrantyType,
    String warrantyCoveredComponents,
    String warrantyServiceContact,
    
    // Health Score
    Integer healthScore,
    
    // GIS Location
    BigDecimal latitude,
    BigDecimal longitude,
    String site,
    String building,
    String floor,
    String room,
    String region,
    
    // IoT
    String sensorId,
    String deviceId,
    LocalDateTime lastHeartbeat,
    BigDecimal telemetryTemp,
    BigDecimal telemetryVibration,
    BigDecimal telemetryRuntimeHours,
    
    // Multi-Currency
    String acquisitionCurrency,
    String functionalCurrency,
    BigDecimal historicalExchangeRate,
    String reportingCurrency,
    
    // Compliance
    Boolean isDeleted,
    Boolean isLegalHold,
    LocalDate retentionExpiryDate,
    
    // Child components
    List<FixedAssetResponse> components
) {}
