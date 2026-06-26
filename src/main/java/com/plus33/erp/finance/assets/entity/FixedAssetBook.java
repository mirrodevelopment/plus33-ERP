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
