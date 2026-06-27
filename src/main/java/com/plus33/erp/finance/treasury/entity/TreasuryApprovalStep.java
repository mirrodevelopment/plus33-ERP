package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "treasury_approval_steps")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_sequence", nullable = false)
    private Integer stepSequence;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "min_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal minAmount = BigDecimal.ZERO;

    @Column(name = "max_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
