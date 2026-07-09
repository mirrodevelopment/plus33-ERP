/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetAssignmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetAssignmentController
 * Related Service   : FixedAssetAssignmentService, FixedAssetAssignmentServiceImpl
 * Related Repository: FixedAssetAssignmentRepository
 * Related Entity    : FixedAssetAssignment
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetAssignmentMapper
 * Related DB Table  : fixed_asset_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetAssignmentService, FixedAssetAssignmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_assignments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetAssignmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_assignments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_assignments}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetAssignmentRepository extends JpaRepository<FixedAssetAssignment, Long> {
    List<FixedAssetAssignment> findAllByFixedAssetId(Long fixedAssetId);
    Optional<FixedAssetAssignment> findTopByFixedAssetIdAndReleasedAtIsNull(Long fixedAssetId);
}