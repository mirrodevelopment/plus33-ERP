/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetConsumption.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetConsumptionController
 * Related Service   : BudgetConsumptionService, BudgetConsumptionServiceImpl
 * Related Repository: BudgetConsumptionRepository
 * Related Entity    : BudgetConsumption
 * Related DTO       : N/A
 * Related Mapper    : BudgetConsumptionMapper
 * Related DB Table  : budget_consumptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetConsumptionRepository, BudgetConsumptionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_consumptions'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetConsumption}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_consumptions'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_consumptions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_consumptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_line_id", nullable = false)
    private BudgetLine budgetLine;

    @Column(name = "source_module", nullable = false, length = 50)
    private String sourceModule; // ACCOUNTS_PAYABLE, GENERAL_LEDGER, FIXED_ASSETS

    @Column(name = "source_reference_id", nullable = false)
    private Long sourceReferenceId;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(name = "consumed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal consumedAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}