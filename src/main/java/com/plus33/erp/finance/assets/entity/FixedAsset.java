/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAsset.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetController
 * Related Service   : FixedAssetService, FixedAssetServiceImpl
 * Related Repository: FixedAssetRepository
 * Related Entity    : FixedAsset
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetMapper
 * Related DB Table  : fixed_assets
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Workforce Module
 * Used By           : FixedAssetRepository, FixedAssetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_assets'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.workforce.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_assets", uniqueConstraints = {
    @UniqueConstraint(name = "uk_fixed_asset_company_code", columnNames = {"company_id", "asset_code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAsset}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_assets'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_assets}</p>
 * <p><b>Module Deps      :</b> Finance, Organization, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_asset_id")
    private FixedAsset parentAsset;

    @Column(name = "asset_code", nullable = false, length = 50)
    private String assetCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "acquisition_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal acquisitionCost;

    @Builder.Default
    @Column(name = "salvage_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal salvageValue = BigDecimal.ZERO;

    @Column(name = "useful_life_years", nullable = false)
    private Integer usefulLifeYears;

    @Column(name = "depreciation_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal depreciationRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_method", nullable = false, length = 30)
    private DepreciationMethod depreciationMethod;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FixedAssetStatus status = FixedAssetStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_invoice_id")
    private SupplierInvoice supplierInvoice;

    @Column(name = "original_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalCost;

    @Column(name = "current_book_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBookValue;

    @Builder.Default
    @Column(name = "accumulated_depreciation", nullable = false, precision = 15, scale = 2)
    private BigDecimal accumulatedDepreciation = BigDecimal.ZERO;

    @Column(name = "last_depreciation_date")
    private LocalDate lastDepreciationDate;

    // Document References
    @Column(name = "purchase_invoice_url")
    private String purchaseInvoiceUrl;

    @Column(name = "warranty_doc_url")
    private String warrantyDocUrl;

    @Column(name = "insurance_doc_url")
    private String insuranceDocUrl;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "manual_url")
    private String manualUrl;

    // Warranty Tracking
    @Column(name = "warranty_start_date")
    private LocalDate warrantyStartDate;

    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;

    @Column(name = "warranty_vendor", length = 100)
    private String warrantyVendor;

    @Column(name = "amc_expiry_date")
    private LocalDate amcExpiryDate;

    @Column(name = "amc_renewal_date")
    private LocalDate amcRenewalDate;

    // Insurance Tracking
    @Column(name = "insurance_policy_number", length = 50)
    private String insurancePolicyNumber;

    @Column(name = "insurance_company", length = 100)
    private String insuranceCompany;

    @Column(name = "insurance_premium", precision = 15, scale = 2)
    private BigDecimal insurancePremium;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @Column(name = "insured_value", precision = 15, scale = 2)
    private BigDecimal insuredValue;

    // Assignments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;

    @Column(name = "assigned_department", length = 100)
    private String assignedDepartment;

    // CWIP & Budgeting
    @Builder.Default
    @Column(name = "is_cwip", nullable = false)
    private Boolean isCwip = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cwip_account_id")
    private Account cwipAccount;

    @Builder.Default
    @Column(name = "budgeted_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal budgetedCost = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "actual_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal actualCost = BigDecimal.ZERO;

    @Column(name = "barcode_or_nfc_tag", length = 100)
    private String barcodeOrNfcTag;

    // Advanced Warranty
    @Column(name = "warranty_type", length = 50)
    private String warrantyType;

    @Column(name = "warranty_covered_components", columnDefinition = "TEXT")
    private String warrantyCoveredComponents;

    @Column(name = "warranty_service_contact", length = 100)
    private String warrantyServiceContact;

    // Health Score
    @Builder.Default
    @Column(name = "health_score", nullable = false)
    private Integer healthScore = 100;

    // GIS Location Mapping
    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "site", length = 100)
    private String site;

    @Column(name = "building", length = 100)
    private String building;

    @Column(name = "floor", length = 50)
    private String floor;

    @Column(name = "room", length = 50)
    private String room;

    @Column(name = "region", length = 100)
    private String region;

    // IoT Telemetry (Readiness)
    @Column(name = "sensor_id", length = 100)
    private String sensorId;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "telemetry_temp", precision = 5, scale = 2)
    private BigDecimal telemetryTemp;

    @Column(name = "telemetry_vibration", precision = 5, scale = 2)
    private BigDecimal telemetryVibration;

    @Builder.Default
    @Column(name = "telemetry_runtime_hours", precision = 12, scale = 2)
    private BigDecimal telemetryRuntimeHours = BigDecimal.ZERO;

    // Multi-Currency
    @Builder.Default
    @Column(name = "acquisition_currency", length = 3)
    private String acquisitionCurrency = "AED";

    @Builder.Default
    @Column(name = "functional_currency", length = 3)
    private String functionalCurrency = "AED";

    @Builder.Default
    @Column(name = "historical_exchange_rate", precision = 18, scale = 6)
    private BigDecimal historicalExchangeRate = BigDecimal.ONE;

    @Builder.Default
    @Column(name = "reporting_currency", length = 3)
    private String reportingCurrency = "AED";

    // Disaster Recovery & Compliance
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Builder.Default
    @Column(name = "is_legal_hold", nullable = false)
    private Boolean isLegalHold = false;

    @Column(name = "retention_expiry_date")
    private LocalDate retentionExpiryDate;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}