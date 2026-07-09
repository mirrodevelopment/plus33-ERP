/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetDriver.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDriverController
 * Related Service   : BudgetDriverService, BudgetDriverServiceImpl
 * Related Repository: BudgetDriverRepository
 * Related Entity    : BudgetDriver
 * Related DTO       : N/A
 * Related Mapper    : BudgetDriverMapper
 * Related DB Table  : budget_drivers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : BudgetDriverRepository, BudgetDriverMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_drivers'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
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
@Table(name = "budget_drivers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_drivers_company_fy_code", columnNames = {"company_id", "fiscal_year_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetDriver}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_drivers'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_drivers}</p>
 * <p><b>Module Deps      :</b> Organization, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Builder.Default
    @Column(name = "driver_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal driverValue = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String unit;

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