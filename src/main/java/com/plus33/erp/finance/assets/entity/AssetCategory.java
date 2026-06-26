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
