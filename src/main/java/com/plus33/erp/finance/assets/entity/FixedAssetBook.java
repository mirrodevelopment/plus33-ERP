/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetBook.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetBookController
 * Related Service   : FixedAssetBookService, FixedAssetBookServiceImpl
 * Related Repository: FixedAssetBookRepository
 * Related Entity    : FixedAssetBook
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetBookMapper
 * Related DB Table  : fixed_asset_books
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetBookRepository, FixedAssetBookMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_books'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_books", uniqueConstraints = {
    @UniqueConstraint(name = "uk_fa_books_asset_book", columnNames = {"fixed_asset_id", "depreciation_book_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetBook}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_books'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_books}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depreciation_book_id", nullable = false)
    private DepreciationBook depreciationBook;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_method", nullable = false, length = 30)
    private DepreciationMethod depreciationMethod;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_frequency", nullable = false, length = 30)
    private DepreciationFrequency depreciationFrequency = DepreciationFrequency.MONTHLY;

    @Column(name = "depreciation_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "useful_life_years", nullable = false)
    private Integer usefulLifeYears;

    @Builder.Default
    @Column(name = "salvage_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal salvageValue = BigDecimal.ZERO;

    @Column(name = "original_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalCost;

    @Column(name = "current_book_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBookValue;

    @Builder.Default
    @Column(name = "accumulated_depreciation", nullable = false, precision = 15, scale = 2)
    private BigDecimal accumulatedDepreciation = BigDecimal.ZERO;

    @Column(name = "last_depreciation_date")
    private LocalDate lastDepreciationDate;
}