/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryComplianceLog.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryComplianceLogController
 * Related Service   : TreasuryComplianceLogService, TreasuryComplianceLogServiceImpl
 * Related Repository: TreasuryComplianceLogRepository
 * Related Entity    : TreasuryComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : TreasuryComplianceLogMapper
 * Related DB Table  : treasury_compliance_logs
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryComplianceLogRepository, TreasuryComplianceLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_compliance_logs'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryComplianceLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_compliance_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_compliance_logs}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_compliance_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryComplianceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType; // IMPORT, RECONCILE, TRANSFER_APPROVAL, REVALUATION, ACCRUAL, POOL_SWEEP

    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}