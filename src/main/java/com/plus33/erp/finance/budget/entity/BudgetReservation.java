/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetReservation.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetReservationController
 * Related Service   : BudgetReservationService, BudgetReservationServiceImpl
 * Related Repository: BudgetReservationRepository
 * Related Entity    : BudgetReservation
 * Related DTO       : N/A
 * Related Mapper    : BudgetReservationMapper
 * Related DB Table  : budget_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetReservationRepository, BudgetReservationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_reservations'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetReservation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_reservations'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_reservations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_reservations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_line_id", nullable = false)
    private BudgetLine budgetLine;

    @Column(name = "source_module", nullable = false, length = 50)
    private String sourceModule; // PROCUREMENT_PR, PROCUREMENT_PO

    @Column(name = "source_reference_id", nullable = false)
    private Long sourceReferenceId;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservedAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 30)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

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