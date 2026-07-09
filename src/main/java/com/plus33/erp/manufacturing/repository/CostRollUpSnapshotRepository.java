/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : CostRollUpSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostRollUpSnapshotController
 * Related Service   : CostRollUpSnapshotService, CostRollUpSnapshotServiceImpl
 * Related Repository: CostRollUpSnapshotRepository
 * Related Entity    : CostRollUpSnapshot
 * Related DTO       : N/A
 * Related Mapper    : CostRollUpSnapshotMapper
 * Related DB Table  : cost_roll_up_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CostRollUpSnapshotService, CostRollUpSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'cost_roll_up_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.CostRollUpSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CostRollUpSnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cost_roll_up_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cost_roll_up_snapshots}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CostRollUpSnapshotRepository extends JpaRepository<CostRollUpSnapshot, Long> {
    List<CostRollUpSnapshot> findByCompanyId(Long companyId);
    List<CostRollUpSnapshot> findByProductId(Long productId);
}
