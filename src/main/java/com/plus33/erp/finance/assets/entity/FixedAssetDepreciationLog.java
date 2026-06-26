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
@Table(name = "fixed_asset_depreciation_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetDepreciationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "depreciation_date", nullable = false)
    private LocalDate depreciationDate;

    @Column(name = "depreciation_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "book_value_before", nullable = false, precision = 15, scale = 2)
    private BigDecimal bookValueBefore;

    @Column(name = "book_value_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal bookValueAfter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
