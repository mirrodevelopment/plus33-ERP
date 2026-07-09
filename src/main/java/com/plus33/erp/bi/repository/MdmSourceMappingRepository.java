/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : MdmSourceMappingRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmSourceMappingController
 * Related Service   : MdmSourceMappingService, MdmSourceMappingServiceImpl
 * Related Repository: MdmSourceMappingRepository
 * Related Entity    : MdmSourceMapping
 * Related DTO       : N/A
 * Related Mapper    : MdmSourceMappingMapper
 * Related DB Table  : mdm_source_mappings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmSourceMappingService, MdmSourceMappingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'mdm_source_mappings' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.MdmSourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmSourceMappingRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mdm_source_mappings' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mdm_source_mappings}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MdmSourceMappingRepository extends JpaRepository<MdmSourceMapping, Long> {
}