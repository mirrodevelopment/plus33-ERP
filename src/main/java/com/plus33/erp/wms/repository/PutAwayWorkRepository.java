/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : PutAwayWorkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PutAwayWorkController
 * Related Service   : PutAwayWorkService, PutAwayWorkServiceImpl
 * Related Repository: PutAwayWorkRepository
 * Related Entity    : PutAwayWork
 * Related DTO       : N/A
 * Related Mapper    : PutAwayWorkMapper
 * Related DB Table  : put_away_works
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PutAwayWorkService, PutAwayWorkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'put_away_works' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.PutAwayWork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code PutAwayWorkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'put_away_works' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code put_away_works}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PutAwayWorkRepository extends JpaRepository<PutAwayWork, Long> {
    List<PutAwayWork> findByAsnId(Long asnId);
    List<PutAwayWork> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<PutAwayWork> findByAssignedToAndStatus(Long userId, String status);
}
