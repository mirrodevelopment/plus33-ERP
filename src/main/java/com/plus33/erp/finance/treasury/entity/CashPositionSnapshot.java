package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cash_position_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPositionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "snapshot_date", nullable = false)
    @Builder.Default
    private LocalDateTime snapshotDate = LocalDateTime.now();

    @Column(name = "snapshot_type", nullable = false, length = 30)
    @Builder.Default
    private String snapshotType = "END_OF_DAY"; // INTRADAY, END_OF_DAY

    @Column(name = "total_cash_usd", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCashUsd;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CashPositionSnapshotLine> lines = new ArrayList<>();
}
