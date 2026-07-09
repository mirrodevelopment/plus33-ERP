/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : UserRegionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserRegionController
 * Related Service   : UserRegionService, UserRegionServiceImpl
 * Related Repository: UserRegionRepository
 * Related Entity    : UserRegion
 * Related DTO       : N/A
 * Related Mapper    : UserRegionMapper
 * Related DB Table  : user_regions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UserRegionService, UserRegionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'user_regions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code UserRegionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'user_regions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code user_regions}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface UserRegionRepository extends JpaRepository<UserRegion, UserRegion.UserRegionId> {
    boolean existsByIdRegionId(Long regionId);
    List<UserRegion> findByIdUserId(Long userId);
    void deleteByIdUserId(Long userId);
}
