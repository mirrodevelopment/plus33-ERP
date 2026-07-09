/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : SerialGenealogyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SerialGenealogyController
 * Related Service   : SerialGenealogyService, SerialGenealogyServiceImpl
 * Related Repository: SerialGenealogyRepository
 * Related Entity    : SerialGenealogy
 * Related DTO       : N/A
 * Related Mapper    : SerialGenealogyMapper
 * Related DB Table  : serial_genealogys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SerialGenealogyService, SerialGenealogyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'serial_genealogys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.SerialGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code SerialGenealogyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'serial_genealogys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code serial_genealogys}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SerialGenealogyRepository extends JpaRepository<SerialGenealogy, Long> {
    List<SerialGenealogy> findByCompanyIdAndChildSerialNumber(Long companyId, String childSerialNumber);
}
