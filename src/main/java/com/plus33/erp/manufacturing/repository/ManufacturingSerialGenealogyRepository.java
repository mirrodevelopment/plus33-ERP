/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ManufacturingSerialGenealogyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingSerialGenealogyController
 * Related Service   : ManufacturingSerialGenealogyService, ManufacturingSerialGenealogyServiceImpl
 * Related Repository: ManufacturingSerialGenealogyRepository
 * Related Entity    : ManufacturingSerialGenealogy
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingSerialGenealogyMapper
 * Related DB Table  : manufacturing_serial_genealogys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingSerialGenealogyService, ManufacturingSerialGenealogyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'manufacturing_serial_genealogys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingSerialGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingSerialGenealogyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'manufacturing_serial_genealogys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_serial_genealogys}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingSerialGenealogyRepository extends JpaRepository<ManufacturingSerialGenealogy, Long> {
    List<ManufacturingSerialGenealogy> findByProductionOrderId(Long productionOrderId);
    Optional<ManufacturingSerialGenealogy> findBySerialNumber(String serialNumber);
    List<ManufacturingSerialGenealogy> findByParentSerialNumber(String parentSerialNumber);
}
