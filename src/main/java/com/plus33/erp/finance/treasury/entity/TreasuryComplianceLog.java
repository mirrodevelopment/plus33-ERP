package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
