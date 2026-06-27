package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_version_id", nullable = false)
    private BudgetVersion budgetVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id", nullable = false)
    private BudgetDimensionSet dimensionSet;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @Builder.Default
    @Column(name = "allocated_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "consumed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal consumedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Builder.Default
    @Column(name = "distribution_method", nullable = false, length = 30)
    private String distributionMethod = "MANUAL"; // EQUAL, MANUAL, SEASONAL, PREV_YEAR_ACTUAL, PERCENTAGE, FORMULA

    @Column(name = "formula_expression", length = 255)
    private String formulaExpression;

    @Column(name = "forecast_confidence", precision = 5, scale = 2)
    private BigDecimal forecastConfidence;

    @Column(name = "predicted_spend", precision = 15, scale = 2)
    private BigDecimal predictedSpend;

    @Column(name = "predicted_revenue", precision = 15, scale = 2)
    private BigDecimal predictedRevenue;

    @Column(name = "ai_recommendation", columnDefinition = "TEXT")
    private String aiRecommendation;

    @Column(name = "ai_generated_at")
    private LocalDateTime aiGeneratedAt;

    @Column(length = 255)
    private String notes;
}
