/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : LaborGroupRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LaborGroupController
 * Related Service   : LaborGroupService, LaborGroupServiceImpl
 * Related Repository: LaborGroupRepository
 * Related Entity    : LaborGroup
 * Related DTO       : N/A
 * Related Mapper    : LaborGroupMapper
 * Related DB Table  : labor_groups
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LaborGroupService, LaborGroupServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'labor_groups' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.LaborGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code LaborGroupRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'labor_groups' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code labor_groups}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface LaborGroupRepository extends JpaRepository<LaborGroup, Long> {
    List<LaborGroup> findByCompanyId(Long companyId);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
