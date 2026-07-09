/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : ControlLibraryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlLibraryController
 * Related Service   : ControlLibraryService, ControlLibraryServiceImpl
 * Related Repository: ControlLibraryRepository
 * Related Entity    : ControlLibrary
 * Related DTO       : N/A
 * Related Mapper    : ControlLibraryMapper
 * Related DB Table  : control_librarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlLibraryService, ControlLibraryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'control_librarys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ControlLibraryRepository extends JpaRepository<ControlLibrary, Long> {
    Optional<ControlLibrary> findByControlCode(String code);
    List<ControlLibrary> findByCompanyIdAndStatus(Long companyId, String status);
}
