/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetBookRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetBookController
 * Related Service   : FixedAssetBookService, FixedAssetBookServiceImpl
 * Related Repository: FixedAssetBookRepository
 * Related Entity    : FixedAssetBook
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetBookMapper
 * Related DB Table  : fixed_asset_books
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetBookService, FixedAssetBookServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_books' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetBookRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_books' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_books}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetBookRepository extends JpaRepository<FixedAssetBook, Long> {
    Optional<FixedAssetBook> findByFixedAssetIdAndDepreciationBookId(Long fixedAssetId, Long depreciationBookId);
    List<FixedAssetBook> findAllByFixedAssetId(Long fixedAssetId);
}