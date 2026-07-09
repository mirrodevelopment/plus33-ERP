/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : WorkCenterRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterController
 * Related Service   : WorkCenterService, WorkCenterServiceImpl
 * Related Repository: WorkCenterRepository
 * Related Entity    : WorkCenter
 * Related DTO       : N/A
 * Related Mapper    : WorkCenterMapper
 * Related DB Table  : work_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkCenterService, WorkCenterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'work_centers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code WorkCenterRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'work_centers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code work_centers}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {

    List<WorkCenter> findByCompanyIdAndActiveTrue(Long companyId);

    Optional<WorkCenter> findByCompanyIdAndCode(Long companyId, String code);

    boolean existsByCompanyIdAndCode(Long companyId, String code);

    default List<WorkCenter> findByCompanyId(Long companyId) {
        return findByCompanyIdAndActiveTrue(companyId);
    }

    List<WorkCenter> findByCompanyIdAndWorkCenterType(Long companyId, String workCenterType);
}