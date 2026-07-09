/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.entity
 * File              : PeriodLock.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockController
 * Related Service   : PeriodLockService, PeriodLockServiceImpl
 * Related Repository: PeriodLockRepository
 * Related Entity    : PeriodLock
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockMapper
 * Related DB Table  : period_locks
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : PeriodLockRepository, PeriodLockMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'period_locks'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PeriodLock}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'period_locks'.</p>
 *
 * <p><b>Database Table   :</b> {@code period_locks}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "period_locks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @Column(name = "lock_date", nullable = false)
    private LocalDate lockDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_type", nullable = false, length = 30)
    @Builder.Default
    private PeriodLockType lockType = PeriodLockType.HARD;

    @Column(name = "locked_by", nullable = false, length = 100)
    private String lockedBy;

    @Column(name = "locked_at", nullable = false)
    @Builder.Default
    private LocalDateTime lockedAt = LocalDateTime.now();

    @Column(length = 255)
    private String reason;
}