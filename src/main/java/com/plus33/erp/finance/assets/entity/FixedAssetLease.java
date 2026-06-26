package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_leases")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetLease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Enumerated(EnumType.STRING)
    @Column(name = "lease_type", nullable = false, length = 30)
    private LeaseType leaseType;

    @Column(name = "lease_start_date", nullable = false)
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date", nullable = false)
    private LocalDate leaseEndDate;

    @Column(name = "monthly_lease_payment", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyLeasePayment;

    @Column(name = "lessor_name", nullable = false, length = 100)
    private String lessorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_liability_account_id", nullable = false)
    private Account leaseLiabilityAccount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
