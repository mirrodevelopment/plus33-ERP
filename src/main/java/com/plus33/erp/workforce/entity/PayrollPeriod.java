/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollPeriod.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPeriodController
 * Related Service   : PayrollPeriodService, PayrollPeriodServiceImpl
 * Related Repository: PayrollPeriodRepository
 * Related Entity    : PayrollPeriod
 * Related DTO       : N/A
 * Related Mapper    : PayrollPeriodMapper
 * Related DB Table  : payroll_periods
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : PayrollPeriodRepository, PayrollPeriodMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_periods'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollPeriod}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_periods'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_periods}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "payroll_periods")
@NoArgsConstructor
@AllArgsConstructor
public class PayrollPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "period_name", nullable = false, length = 100)
    private String periodName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 30)
    private String status = "OPEN";

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

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