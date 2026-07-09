/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : MrpRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpRunController
 * Related Service   : MrpRunService, MrpRunServiceImpl
 * Related Repository: MrpRunRepository
 * Related Entity    : MrpRun
 * Related DTO       : N/A
 * Related Mapper    : MrpRunMapper
 * Related DB Table  : mrp_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpRunService, MrpRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'mrp_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpRunRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mrp_runs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mrp_runs}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MrpRunRepository extends JpaRepository<MrpRun, Long> {

    Optional<MrpRun> findByCompanyIdAndRunNumber(Long companyId, String runNumber);

    List<MrpRun> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    @Query("""
        SELECT r FROM MrpRun r
        WHERE r.companyId = :companyId
          AND r.status = 'COMPLETED'
        ORDER BY r.completedAt DESC
    """)
    List<MrpRun> findCompletedRuns(@Param("companyId") Long companyId);
}