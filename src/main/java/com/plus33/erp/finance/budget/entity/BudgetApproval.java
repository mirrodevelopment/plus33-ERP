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
