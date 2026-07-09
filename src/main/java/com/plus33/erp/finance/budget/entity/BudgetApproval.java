/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetApproval.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetApprovalController
 * Related Service   : BudgetApprovalService, BudgetApprovalServiceImpl
 * Related Repository: BudgetApprovalRepository
 * Related Entity    : BudgetApproval
 * Related DTO       : N/A
 * Related Mapper    : BudgetApprovalMapper
 * Related DB Table  : budget_approvals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetApprovalRepository, BudgetApprovalMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_approvals'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_approvals", uniqueConstraints = {
    @UniqueConstraint(name = "uk_approvals_budget_step", columnNames = {"budget_id", "approval_step"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetApproval}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_approvals'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_approvals}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "approval_step", nullable = false)
    private Integer approvalStep;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(name = "approver_username", length = 100)
    private String approverUsername;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(length = 255)
    private String remarks;
}