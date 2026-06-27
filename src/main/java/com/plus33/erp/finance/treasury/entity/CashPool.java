package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cash_pools")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "pool_name", nullable = false, length = 150)
    private String poolName;

    @Column(name = "pool_type", nullable = false, length = 30)
    @Builder.Default
    private String poolType = "ZERO_BALANCE"; // ZERO_BALANCE, NOTIONAL, TARGET_BALANCE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_account_id", nullable = false)
    private BankAccount headerAccount;

    @Column(name = "target_balance", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal targetBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CashPoolMember> members = new ArrayList<>();
}
