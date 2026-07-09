/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.repository
 * File              : RegionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Organization Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionService, RegionServiceImpl
 * Related Repository: RegionRepository
 * Related Entity    : Region
 * Related DTO       : N/A
 * Related Mapper    : RegionMapper
 * Related DB Table  : regions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RegionService, RegionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Organization Module against the 'regions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code RegionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'regions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code regions}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RegionRepository extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region> {
    Optional<Region> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
    long countByParentIsNull();
    long countByParentIsNotNull();
    boolean existsByParentId(Long parentId);

    @org.springframework.data.jpa.repository.Query("SELECT r.id, r.parent.id FROM Region r")
    java.util.List<Object[]> findAllParentMappings();
}
