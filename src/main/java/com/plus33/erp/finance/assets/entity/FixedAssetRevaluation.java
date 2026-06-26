package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_revaluations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetRevaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "revaluation_date", nullable = false)
    private LocalDate revaluationDate;

    @Column(name = "previous_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal previousValue;

    @Column(name = "new_fair_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal newFairValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revaluation_reserve_account_id", nullable = false)
    private Account revaluationReserveAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @Column(length = 255)
    private String reason;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
