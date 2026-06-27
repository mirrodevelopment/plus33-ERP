package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "budget_template_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetTemplateLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private BudgetTemplate template;

    @Column(name = "account_code", nullable = false, length = 50)
    private String accountCode;

    @Column(name = "dimension_type", length = 50)
    private String dimensionType; // DEPARTMENT, COST_CENTER, PROJECT, WAREHOUSE, ASSET_CATEGORY

    @Builder.Default
    @Column(name = "distribution_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal distributionPercent = BigDecimal.ZERO;
}
