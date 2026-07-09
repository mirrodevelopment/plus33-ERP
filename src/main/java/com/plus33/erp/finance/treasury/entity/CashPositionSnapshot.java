/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashPositionSnapshot.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPositionSnapshotController
 * Related Service   : CashPositionSnapshotService, CashPositionSnapshotServiceImpl
 * Related Repository: CashPositionSnapshotRepository
 * Related Entity    : CashPositionSnapshot
 * Related DTO       : N/A
 * Related Mapper    : CashPositionSnapshotMapper
 * Related DB Table  : cash_position_snapshots
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : CashPositionSnapshotRepository, CashPositionSnapshotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cash_position_snapshots'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPositionSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cash_position_snapshots'.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_position_snapshots}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "cash_position_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPositionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "snapshot_date", nullable = false)
    @Builder.Default
    private LocalDateTime snapshotDate = LocalDateTime.now();

    @Column(name = "snapshot_type", nullable = false, length = 30)
    @Builder.Default
    private String snapshotType = "END_OF_DAY"; // INTRADAY, END_OF_DAY

    @Column(name = "total_cash_usd", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCashUsd;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CashPositionSnapshotLine> lines = new ArrayList<>();
}