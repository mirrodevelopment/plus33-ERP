/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetDowntimeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetDowntimeController
 * Related Service   : FixedAssetDowntimeService, FixedAssetDowntimeServiceImpl
 * Related Repository: FixedAssetDowntimeRepository
 * Related Entity    : FixedAssetDowntime
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetDowntimeMapper
 * Related DB Table  : fixed_asset_downtimes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetDowntimeService, FixedAssetDowntimeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_downtimes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetDowntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetDowntimeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_downtimes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_downtimes}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetDowntimeRepository extends JpaRepository<FixedAssetDowntime, Long> {
    List<FixedAssetDowntime> findAllByFixedAssetIdOrderByStartTimeDesc(Long fixedAssetId);
}