/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : EngineeringChangeLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeLineController
 * Related Service   : EngineeringChangeLineService, EngineeringChangeLineServiceImpl
 * Related Repository: EngineeringChangeLineRepository
 * Related Entity    : EngineeringChangeLine
 * Related DTO       : N/A
 * Related Mapper    : EngineeringChangeLineMapper
 * Related DB Table  : engineering_change_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeLineService, EngineeringChangeLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'engineering_change_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.EngineeringChangeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code EngineeringChangeLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'engineering_change_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code engineering_change_lines}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EngineeringChangeLineRepository extends JpaRepository<EngineeringChangeLine, Long> {
    List<EngineeringChangeLine> findByEngineeringChangeOrderId(Long ecoId);
}
