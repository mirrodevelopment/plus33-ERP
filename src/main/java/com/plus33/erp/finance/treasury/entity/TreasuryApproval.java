package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
