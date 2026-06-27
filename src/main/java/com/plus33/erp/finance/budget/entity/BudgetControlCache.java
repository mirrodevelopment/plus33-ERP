package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_control_caches")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetControlCache {

    @Id
    @Column(name = "budget_line_id")
    private Long budgetLineId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "budget_line_id")
    private BudgetLine budgetLine;

    @Builder.Default
    @Column(name = "allocated_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "consumed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal consumedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "available_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal availableAmount = BigDecimal.ZERO;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
