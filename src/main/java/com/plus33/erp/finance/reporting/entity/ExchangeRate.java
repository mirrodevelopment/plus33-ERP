/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.entity
 * File              : ExchangeRate.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ExchangeRateController
 * Related Service   : ExchangeRateService, ExchangeRateServiceImpl
 * Related Repository: ExchangeRateRepository
 * Related Entity    : ExchangeRate
 * Related DTO       : N/A
 * Related Mapper    : ExchangeRateMapper
 * Related DB Table  : exchange_rates
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : ExchangeRateRepository, ExchangeRateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'exchange_rates'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "exchange_rates", uniqueConstraints = {
    @UniqueConstraint(name = "uk_exchange_rate_lookup", columnNames = {"company_id", "from_currency", "to_currency", "rate_type", "effective_date"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ExchangeRate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'exchange_rates'.</p>
 *
 * <p><b>Database Table   :</b> {@code exchange_rates}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "from_currency", nullable = false, length = 3)
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false, length = 3)
    private String toCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type", nullable = false, length = 30)
    @Builder.Default
    private ExchangeRateType rateType = ExchangeRateType.SPOT;

    @Column(nullable = false, precision = 12, scale = 6)
    private BigDecimal rate;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
}