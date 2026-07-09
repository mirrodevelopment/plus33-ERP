/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryLimit.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryLimitController
 * Related Service   : TreasuryLimitService, TreasuryLimitServiceImpl
 * Related Repository: TreasuryLimitRepository
 * Related Entity    : TreasuryLimit
 * Related DTO       : N/A
 * Related Mapper    : TreasuryLimitMapper
 * Related DB Table  : treasury_limits
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryLimitRepository, TreasuryLimitMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_limits'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
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
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryLimit}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_limits'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_limits}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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