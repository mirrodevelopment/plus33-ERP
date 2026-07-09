/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : AssetCategory.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetCategoryController
 * Related Service   : AssetCategoryService, AssetCategoryServiceImpl
 * Related Repository: AssetCategoryRepository
 * Related Entity    : AssetCategory
 * Related DTO       : N/A
 * Related Mapper    : AssetCategoryMapper
 * Related DB Table  : asset_categories
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : AssetCategoryRepository, AssetCategoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'asset_categories'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "asset_categories", uniqueConstraints = {
    @UniqueConstraint(name = "uk_asset_category_company_code", columnNames = {"company_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code AssetCategory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'asset_categories'.</p>
 *
 * <p><b>Database Table   :</b> {@code asset_categories}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_method", nullable = false, length = 30)
    private DepreciationMethod depreciationMethod;

    @Column(name = "depreciation_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "useful_life_years", nullable = false)
    private Integer usefulLifeYears;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_account_id", nullable = false)
    private Account assetAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accumulated_depreciation_account_id", nullable = false)
    private Account accumulatedDepreciationAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depreciation_expense_account_id", nullable = false)
    private Account depreciationExpenseAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gain_loss_account_id", nullable = false)
    private Account gainLossAccount;

    @Builder.Default
    @Column(name = "capitalization_threshold", nullable = false, precision = 15, scale = 2)
    private BigDecimal capitalizationThreshold = BigDecimal.ZERO;
}