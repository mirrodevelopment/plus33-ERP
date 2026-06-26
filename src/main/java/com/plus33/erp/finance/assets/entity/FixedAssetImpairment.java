package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_impairments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetImpairment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "impairment_date", nullable = false)
    private LocalDate impairmentDate;

    @Column(name = "impairment_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal impairmentAmount;

    @Column(name = "recoverable_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal recoverableAmount;

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
