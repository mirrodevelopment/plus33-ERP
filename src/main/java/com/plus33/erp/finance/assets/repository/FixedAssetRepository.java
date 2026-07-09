/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetController
 * Related Service   : FixedAssetService, FixedAssetServiceImpl
 * Related Repository: FixedAssetRepository
 * Related Entity    : FixedAsset
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetMapper
 * Related DB Table  : fixed_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetService, FixedAssetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_assets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAsset;
import com.plus33.erp.finance.assets.entity.FixedAssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_assets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_assets}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long>, JpaSpecificationExecutor<FixedAsset> {
    Optional<FixedAsset> findByCompanyIdAndAssetCode(Long companyId, String assetCode);
    Optional<FixedAsset> findByCompanyIdAndId(Long companyId, Long id);
    List<FixedAsset> findAllByCompanyIdAndStatus(Long companyId, FixedAssetStatus status);
    List<FixedAsset> findAllByCompanyId(Long companyId);
    List<FixedAsset> findAllByParentAssetId(Long parentAssetId);

    @Query(value = "SELECT nextval('fixed_asset_code_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}