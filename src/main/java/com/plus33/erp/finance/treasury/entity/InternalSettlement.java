/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : InternalSettlement.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InternalSettlementController
 * Related Service   : InternalSettlementService, InternalSettlementServiceImpl
 * Related Repository: InternalSettlementRepository
 * Related Entity    : InternalSettlement
 * Related DTO       : N/A
 * Related Mapper    : InternalSettlementMapper
 * Related DB Table  : internal_settlements
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : InternalSettlementRepository, InternalSettlementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'internal_settlements'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InternalSettlement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'internal_settlements'.</p>
 *
 * <p><b>Database Table   :</b> {@code internal_settlements}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "internal_settlements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_ihb_account_id", nullable = false)
    private InHouseBankAccount sourceIhbAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_ihb_account_id", nullable = false)
    private InHouseBankAccount targetIhbAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(length = 255)
    private String notes;
}