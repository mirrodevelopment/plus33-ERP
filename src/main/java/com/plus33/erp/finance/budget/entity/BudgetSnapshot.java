/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetSnapshotController
 * Related Service   : BudgetSnapshotService, BudgetSnapshotServiceImpl
 * Related Repository: BudgetSnapshotRepository
 * Related Entity    : BudgetSnapshot
 * Related DTO       : N/A
 * Related Mapper    : BudgetSnapshotMapper
 * Related DB Table  : budget_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetSnapshotRepository, BudgetSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_snapshots'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_snapshots}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "snapshot_date", nullable = false, updatable = false)
    private LocalDateTime snapshotDate;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "trigger_event", nullable = false, length = 50)
    private String triggerEvent;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BudgetSnapshotLine> lines = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        snapshotDate = LocalDateTime.now();
    }
}