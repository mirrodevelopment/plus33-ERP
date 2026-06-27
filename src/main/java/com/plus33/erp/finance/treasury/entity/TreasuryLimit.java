package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "treasury_limits", uniqueConstraints = {
    @UniqueConstraint(name = "uk_limits_combination", columnNames = {"company_id", "limit_type", "currency_code", "country_code", "target_bank_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "limit_type", nullable = false, length = 50)
    private String limitType; // DAILY_TRANSFER, CURRENCY_EXPOSURE, COUNTRY_EXPOSURE, MINIMUM_CASH_RESERVE

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_bank_id")
    private Bank targetBank;

    @Column(name = "limit_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal limitAmount;

    @Column(name = "warning_threshold_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal warningThresholdPercent = new BigDecimal("80.00");

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
