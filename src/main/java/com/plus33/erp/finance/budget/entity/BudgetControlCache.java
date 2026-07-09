/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetControlCache.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetControlCacheController
 * Related Service   : BudgetControlCacheService, BudgetControlCacheServiceImpl
 * Related Repository: BudgetControlCacheRepository
 * Related Entity    : BudgetControlCache
 * Related DTO       : N/A
 * Related Mapper    : BudgetControlCacheMapper
 * Related DB Table  : budget_control_caches
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetControlCacheRepository, BudgetControlCacheMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_control_caches'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetControlCache}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_control_caches'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_control_caches}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_control_caches")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetControlCache {

    @Id
    @Column(name = "budget_line_id")
    private Long budgetLineId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "budget_line_id")
    private BudgetLine budgetLine;

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
    @Column(name = "available_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal availableAmount = BigDecimal.ZERO;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}