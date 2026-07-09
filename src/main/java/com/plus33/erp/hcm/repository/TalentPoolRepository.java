/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : TalentPoolRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TalentPoolController
 * Related Service   : TalentPoolService, TalentPoolServiceImpl
 * Related Repository: TalentPoolRepository
 * Related Entity    : TalentPool
 * Related DTO       : N/A
 * Related Mapper    : TalentPoolMapper
 * Related DB Table  : talent_pools
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TalentPoolService, TalentPoolServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'talent_pools' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.TalentPool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code TalentPoolRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'talent_pools' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code talent_pools}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TalentPoolRepository extends JpaRepository<TalentPool, Long> {
    Optional<TalentPool> findByName(String name);
}
