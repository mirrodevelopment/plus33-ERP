/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxRate.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxRateController
 * Related Service   : TaxRateService, TaxRateServiceImpl
 * Related Repository: TaxRateRepository
 * Related Entity    : TaxRate
 * Related DTO       : N/A
 * Related Mapper    : TaxRateMapper
 * Related DB Table  : tax_rates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxRateRepository, TaxRateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_rates'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxRate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_rates'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_rates}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "tax_rates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TaxCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_version_id")
    private TaxConfigurationVersion configVersion;

    @Column(name = "rate_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratePercent;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}