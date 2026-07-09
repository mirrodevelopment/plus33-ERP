/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmPositionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmPositionController
 * Related Service   : HcmPositionService, HcmPositionServiceImpl
 * Related Repository: HcmPositionRepository
 * Related Entity    : HcmPosition
 * Related DTO       : N/A
 * Related Mapper    : HcmPositionMapper
 * Related DB Table  : hcm_positions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmPositionService, HcmPositionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_positions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmPositionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_positions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_positions}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmPositionRepository extends JpaRepository<HcmPosition, Long> {
    Optional<HcmPosition> findByCode(String code);
}
