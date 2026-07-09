/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : Bank.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankController
 * Related Service   : BankService, BankServiceImpl
 * Related Repository: BankRepository
 * Related Entity    : Bank
 * Related DTO       : N/A
 * Related Mapper    : BankMapper
 * Related DB Table  : banks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankRepository, BankMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'banks'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code Bank}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'banks'.</p>
 *
 * <p><b>Database Table   :</b> {@code banks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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