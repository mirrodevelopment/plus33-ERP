/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmDepartmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmDepartmentController
 * Related Service   : HcmDepartmentService, HcmDepartmentServiceImpl
 * Related Repository: HcmDepartmentRepository
 * Related Entity    : HcmDepartment
 * Related DTO       : N/A
 * Related Mapper    : HcmDepartmentMapper
 * Related DB Table  : hcm_departments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmDepartmentService, HcmDepartmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_departments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmDepartmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_departments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_departments}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmDepartmentRepository extends JpaRepository<HcmDepartment, Long> {
    List<HcmDepartment> findByOrganizationIdAndIsCurrent(Long organizationId, Boolean isCurrent);
}
