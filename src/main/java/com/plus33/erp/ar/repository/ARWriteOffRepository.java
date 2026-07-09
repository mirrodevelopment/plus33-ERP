/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.repository
 * File              : ARWriteOffRepository.java
 * Purpose           : JPA Repository providing database CRUD for Ar Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffService, ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : N/A
 * Related Mapper    : ARWriteOffMapper
 * Related DB Table  : a_r_write_offs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ARWriteOffService, ARWriteOffServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Ar Module against the 'a_r_write_offs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.ar.repository;

import com.plus33.erp.ar.entity.ARWriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Ar Module</b>
 *
 * <p><b>Class  :</b> {@code ARWriteOffRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ar.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'a_r_write_offs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code a_r_write_offs}</p>
 * <p><b>Module Deps      :</b> Ar</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ARWriteOffRepository extends JpaRepository<ARWriteOff, Long>, JpaSpecificationExecutor<ARWriteOff> {

    Optional<ARWriteOff> findByCompanyIdAndWriteOffNumber(Long companyId, String writeOffNumber);

    List<ARWriteOff> findByCustomerInvoiceId(Long customerInvoiceId);

    List<ARWriteOff> findByCustomerIdAndCompanyId(Long customerId, Long companyId);

    @Query(value = "SELECT nextval('ar_write_off_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}