/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : ProductRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : N/A
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductService, ProductServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'products' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'products' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code products}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByNameIgnoreCase(String name);
    Page<Product> findByActive(Boolean active, Pageable pageable);
}
