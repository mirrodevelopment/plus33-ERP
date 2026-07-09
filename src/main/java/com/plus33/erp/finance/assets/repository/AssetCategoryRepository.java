/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : AssetCategoryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetCategoryController
 * Related Service   : AssetCategoryService, AssetCategoryServiceImpl
 * Related Repository: AssetCategoryRepository
 * Related Entity    : AssetCategory
 * Related DTO       : N/A
 * Related Mapper    : AssetCategoryMapper
 * Related DB Table  : asset_categorys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetCategoryService, AssetCategoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'asset_categorys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code AssetCategoryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'asset_categorys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code asset_categorys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {
    Optional<AssetCategory> findByCompanyIdAndCode(Long companyId, String code);
    List<AssetCategory> findAllByCompanyId(Long companyId);
}