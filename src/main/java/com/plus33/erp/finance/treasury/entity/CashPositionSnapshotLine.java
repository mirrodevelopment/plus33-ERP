package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cash_position_snapshot_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPositionSnapshotLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private CashPositionSnapshot snapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "current_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "reconciled_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal reconciledBalance;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}
