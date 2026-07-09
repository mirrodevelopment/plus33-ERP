/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : VanStockRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: VanStockController
 * Related Service   : VanStockService, VanStockServiceImpl
 * Related Repository: VanStockRepository
 * Related Entity    : VanStock
 * Related DTO       : N/A
 * Related Mapper    : VanStockMapper
 * Related DB Table  : van_stocks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : VanStockService, VanStockServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'van_stocks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.VanStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code VanStockRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'van_stocks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code van_stocks}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface VanStockRepository extends JpaRepository<VanStock, Long> {
    Optional<VanStock> findByCompanyIdAndVanIdAndProductId(Long companyId, Long vanId, Long productId);
}
