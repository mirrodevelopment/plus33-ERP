/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : FixedAssetResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetController
 * Related Service   : FixedAssetService, FixedAssetServiceImpl
 * Related Repository: FixedAssetRepository
 * Related Entity    : FixedAsset
 * Related DTO       : FixedAssetResponse
 * Related Mapper    : FixedAssetMapper
 * Related DB Table  : fixed_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetController, FixedAssetService, FixedAssetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
