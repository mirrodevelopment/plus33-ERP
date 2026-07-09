/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetAssignment.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetAssignmentController
 * Related Service   : FixedAssetAssignmentService, FixedAssetAssignmentServiceImpl
 * Related Repository: FixedAssetAssignmentRepository
 * Related Entity    : FixedAssetAssignment
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetAssignmentMapper
 * Related DB Table  : fixed_asset_assignments
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Workforce Module
 * Used By           : FixedAssetAssignmentRepository, FixedAssetAssignmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_assignments'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.workforce.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetAssignment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_assignments'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_assignments}</p>
 * <p><b>Module Deps      :</b> Organization, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_assignments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;

    @Column(name = "assigned_department", length = 100)
    private String assignedDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_warehouse_id")
    private Warehouse assignedWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_store_id")
    private Store assignedStore;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "assigned_by", nullable = false, length = 100)
    private String assignedBy;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}