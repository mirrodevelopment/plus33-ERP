/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : FixedAssetRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetController
 * Related Service   : FixedAssetService, FixedAssetServiceImpl
 * Related Repository: FixedAssetRepository
 * Related Entity    : FixedAsset
 * Related DTO       : FixedAssetRequest
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

public record FixedAssetRequest(
    Long categoryId,
    Long parentAssetId,
    String name,
    String description,
    LocalDate acquisitionDate,
    BigDecimal acquisitionCost,
    BigDecimal salvageValue,
    Integer usefulLifeYears,
    BigDecimal depreciationRate,
    String depreciationMethod,
    Long warehouseId,
    Long storeId,
    Long supplierInvoiceId,
    
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
    
    // CWIP & Budgeting
    Boolean isCwip,
    Long cwipAccountId,
    BigDecimal budgetedCost,
    
    // Barcode/NFC
    String barcodeOrNfcTag,
    
    // Advanced Warranty
    String warrantyType,
    String warrantyCoveredComponents,
    String warrantyServiceContact,
    
    // GIS Location
    BigDecimal latitude,
    BigDecimal longitude,
    String site,
    String building,
    String floor,
    String room,
    String region,
    
    // IoT Readiness
    String sensorId,
    String deviceId,
    
    // Multi-Currency
    String acquisitionCurrency,
    String functionalCurrency,
    BigDecimal historicalExchangeRate,
    String reportingCurrency
) {
    public FixedAssetRequest(
        Long categoryId,
        Long parentAssetId,
        String name,
        String description,
        LocalDate acquisitionDate,
        BigDecimal acquisitionCost,
        BigDecimal salvageValue,
        Integer usefulLifeYears,
        BigDecimal depreciationRate,
        String depreciationMethod,
        Long warehouseId,
        Long storeId,
        Long supplierInvoiceId,
        String purchaseInvoiceUrl,
        String warrantyDocUrl,
        String insuranceDocUrl,
        String photoUrl,
        String manualUrl,
        LocalDate warrantyStartDate,
        LocalDate warrantyEndDate,
        String warrantyVendor,
        LocalDate amcExpiryDate,
        LocalDate amcRenewalDate,
        String insurancePolicyNumber,
        String insuranceCompany,
        BigDecimal insurancePremium,
        LocalDate insuranceExpiryDate,
        BigDecimal insuredValue
    ) {
        this(
            categoryId, parentAssetId, name, description, acquisitionDate, acquisitionCost, salvageValue,
            usefulLifeYears, depreciationRate, depreciationMethod, warehouseId, storeId, supplierInvoiceId,
            purchaseInvoiceUrl, warrantyDocUrl, insuranceDocUrl, photoUrl, manualUrl,
            warrantyStartDate, warrantyEndDate, warrantyVendor, amcExpiryDate, amcRenewalDate,
            insurancePolicyNumber, insuranceCompany, insurancePremium, insuranceExpiryDate, insuredValue,
            false, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null
        );
    }

    public FixedAssetRequest(
        Long categoryId,
        Long parentAssetId,
        String name,
        String description,
        LocalDate acquisitionDate,
        BigDecimal acquisitionCost,
        BigDecimal salvageValue,
        Integer usefulLifeYears,
        BigDecimal depreciationRate,
        String depreciationMethod,
        Long warehouseId,
        Long storeId,
        Long supplierInvoiceId
    ) {
        this(
            categoryId, parentAssetId, name, description, acquisitionDate, acquisitionCost, salvageValue,
            usefulLifeYears, depreciationRate, depreciationMethod, warehouseId, storeId, supplierInvoiceId,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        );
    }
}
