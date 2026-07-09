/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : ProductCategoryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductCategoryController
 * Related Service   : ProductCategoryService, ProductCategoryServiceImpl
 * Related Repository: ProductCategoryRepository
 * Related Entity    : ProductCategory
 * Related DTO       : N/A
 * Related Mapper    : ProductCategoryMapper
 * Related DB Table  : product_categorys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductCategoryService, ProductCategoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'product_categorys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductCategoryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'product_categorys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code product_categorys}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByCode(String code);
}
