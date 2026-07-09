/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryApproval.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryApprovalController
 * Related Service   : TreasuryApprovalService, TreasuryApprovalServiceImpl
 * Related Repository: TreasuryApprovalRepository
 * Related Entity    : TreasuryApproval
 * Related DTO       : N/A
 * Related Mapper    : TreasuryApprovalMapper
 * Related DB Table  : treasury_approvals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryApprovalRepository, TreasuryApprovalMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_approvals'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryApproval}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_approvals'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_approvals}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_approvals")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approval_step", nullable = false)
    private Integer approvalStep;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private CashTransfer transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_batch_id")
    private PaymentBatch paymentBatch;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(name = "approver_username", length = 100)
    private String approverUsername;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(length = 255)
    private String remarks;
}