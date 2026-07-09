/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : PickingWorkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickingWorkController
 * Related Service   : PickingWorkService, PickingWorkServiceImpl
 * Related Repository: PickingWorkRepository
 * Related Entity    : PickingWork
 * Related DTO       : N/A
 * Related Mapper    : PickingWorkMapper
 * Related DB Table  : picking_works
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickingWorkService, PickingWorkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'picking_works' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.PickingWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code PickingWorkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'picking_works' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code picking_works}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PickingWorkRepository extends JpaRepository<PickingWork, Long> {
    List<PickingWork> findByWave_IdAndStatus(Long waveId, String status);
    List<PickingWork> findByCompanyIdAndAssignedToAndStatus(Long companyId, Long userId, String status);

    @Query("SELECT pw FROM PickingWork pw WHERE pw.wave.id = :waveId AND pw.status IN ('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'PARTIALLY_PICKED')")
    List<PickingWork> findOpenByWave(@Param("waveId") Long waveId);
}
