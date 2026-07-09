/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : ControlMappingRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlMappingController
 * Related Service   : ControlMappingService, ControlMappingServiceImpl
 * Related Repository: ControlMappingRepository
 * Related Entity    : ControlMapping
 * Related DTO       : N/A
 * Related Mapper    : ControlMappingMapper
 * Related DB Table  : control_mappings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlMappingService, ControlMappingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'control_mappings' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ControlMappingRepository extends JpaRepository<ControlMapping, Long> {
    List<ControlMapping> findByControlLibraryId(Long controlLibraryId);
    List<ControlMapping> findByFrameworkId(Long frameworkId);
    boolean existsByControlLibraryIdAndFrameworkId(Long controlLibraryId, Long frameworkId);
}
