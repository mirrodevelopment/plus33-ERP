/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : UnitOfMeasureRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UnitOfMeasureController
 * Related Service   : UnitOfMeasureService, UnitOfMeasureServiceImpl
 * Related Repository: UnitOfMeasureRepository
 * Related Entity    : UnitOfMeasure
 * Related DTO       : N/A
 * Related Mapper    : UnitOfMeasureMapper
 * Related DB Table  : unit_of_measures
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UnitOfMeasureService, UnitOfMeasureServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'unit_of_measures' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code UnitOfMeasureRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'unit_of_measures' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code unit_of_measures}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {
    Optional<UnitOfMeasure> findByCode(String code);
}
