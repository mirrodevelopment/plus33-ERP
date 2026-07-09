/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetFxLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetFxLogController
 * Related Service   : FixedAssetFxLogService, FixedAssetFxLogServiceImpl
 * Related Repository: FixedAssetFxLogRepository
 * Related Entity    : FixedAssetFxLog
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetFxLogMapper
 * Related DB Table  : fixed_asset_fx_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetFxLogService, FixedAssetFxLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_fx_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetFxLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetFxLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_fx_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_fx_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetFxLogRepository extends JpaRepository<FixedAssetFxLog, Long> {
    List<FixedAssetFxLog> findAllByFixedAssetIdOrderByValuationDateDesc(Long fixedAssetId);
}