/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : Budget.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetController
 * Related Service   : BudgetService, BudgetServiceImpl
 * Related Repository: BudgetRepository
 * Related Entity    : Budget
 * Related DTO       : N/A
 * Related Mapper    : BudgetMapper
 * Related DB Table  : budgets
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : BudgetRepository, BudgetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budgets'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.reporting.entity.FiscalYear;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budgets", uniqueConstraints = {
    @UniqueConstraint(name = "uk_budgets_company_fy_code", columnNames = {"company_id", "fiscal_year_id", "code", "is_forecast"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code Budget}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budgets'.</p>
 *
 * <p><b>Database Table   :</b> {@code budgets}</p>
 * <p><b>Module Deps      :</b> Organization, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_policy_id", nullable = false)
    private BudgetPolicy budgetPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_template_id")
    private BudgetWorkflowTemplate workflowTemplate;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_type", nullable = false, length = 30)
    private BudgetType budgetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 30)
    private BudgetPeriodType periodType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 30)
    private BudgetScenario scenario = BudgetScenario.EXPECTED;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 30)
    private BudgetStatus status = BudgetStatus.DRAFT;

    @Builder.Default
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber = 1;

    @Builder.Default
    @Column(name = "is_forecast", nullable = false)
    private Boolean isForecast = false;

    @Column(name = "forecast_type", length = 30)
    private String forecastType;

    @Column(name = "forecast_cycle_code", length = 50)
    private String forecastCycleCode;

    @Builder.Default
    @Column(name = "is_frozen", nullable = false)
    private Boolean isFrozen = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "rate_lock_type", nullable = false, length = 30)
    private String rateLockType = "SPOT"; // HISTORICAL, BUDGET_RATE, MONTHLY_AVERAGE, SPOT

    @Builder.Default
    @Column(name = "budget_exchange_rate", precision = 18, scale = 6)
    private BigDecimal budgetExchangeRate = BigDecimal.ONE;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

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