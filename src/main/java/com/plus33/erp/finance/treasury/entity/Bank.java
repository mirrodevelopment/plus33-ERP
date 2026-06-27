package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "banks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "routing_code", length = 30)
    private String routingCode;

    @Column(name = "credit_rating", nullable = false, length = 10)
    @Builder.Default
    private String creditRating = "A";

    @Column(name = "country_risk_score", nullable = false)
    @Builder.Default
    private Integer countryRiskScore = 1;

    @Column(name = "exposure_limit", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal exposureLimit = new BigDecimal("10000000.00");

    @Column(name = "max_deposit_limit", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal maxDepositLimit = new BigDecimal("10000000.00");

    @Column(name = "current_exposure", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentExposure = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
