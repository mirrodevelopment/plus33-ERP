/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : CycleCountResultRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountResultController
 * Related Service   : CycleCountResultService, CycleCountResultServiceImpl
 * Related Repository: CycleCountResultRepository
 * Related Entity    : CycleCountResult
 * Related DTO       : N/A
 * Related Mapper    : CycleCountResultMapper
 * Related DB Table  : cycle_count_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountResultService, CycleCountResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'cycle_count_results' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountResultRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cycle_count_results' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_results}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CycleCountResultRepository extends JpaRepository<CycleCountResult, Long> {
    List<CycleCountResult> findByPlanId(Long planId);
    List<CycleCountResult> findByTask_Id(Long taskId);
    List<CycleCountResult> findByPlanIdAndStatus(Long planId, String status);
}
