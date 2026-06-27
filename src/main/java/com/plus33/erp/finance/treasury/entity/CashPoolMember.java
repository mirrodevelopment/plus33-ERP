package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "cash_pool_members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPoolMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id", nullable = false)
    private CashPool pool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false, unique = true)
    private BankAccount bankAccount;

    @Column(name = "sweep_priority", nullable = false)
    @Builder.Default
    private Integer sweepPriority = 1;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
