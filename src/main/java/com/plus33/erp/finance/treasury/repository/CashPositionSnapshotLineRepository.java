/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : CashPositionSnapshotLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPositionSnapshotLineController
 * Related Service   : CashPositionSnapshotLineService, CashPositionSnapshotLineServiceImpl
 * Related Repository: CashPositionSnapshotLineRepository
 * Related Entity    : CashPositionSnapshotLine
 * Related DTO       : N/A
 * Related Mapper    : CashPositionSnapshotLineMapper
 * Related DB Table  : cash_position_snapshot_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashPositionSnapshotLineService, CashPositionSnapshotLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'cash_position_snapshot_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPositionSnapshotLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPositionSnapshotLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cash_position_snapshot_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_position_snapshot_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CashPositionSnapshotLineRepository extends JpaRepository<CashPositionSnapshotLine, Long> {
    List<CashPositionSnapshotLine> findBySnapshotId(Long snapshotId);
}