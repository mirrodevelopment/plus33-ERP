/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : ProcurementRfqVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementRfqVersionController
 * Related Service   : ProcurementRfqVersionService, ProcurementRfqVersionServiceImpl
 * Related Repository: ProcurementRfqVersionRepository
 * Related Entity    : ProcurementRfqVersion
 * Related DTO       : N/A
 * Related Mapper    : ProcurementRfqVersionMapper
 * Related DB Table  : procurement_rfq_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementRfqVersionService, ProcurementRfqVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'procurement_rfq_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementRfqVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementRfqVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'procurement_rfq_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_rfq_versions}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProcurementRfqVersionRepository extends JpaRepository<ProcurementRfqVersion, Long> {
    List<ProcurementRfqVersion> findByRfqId(Long rfqId);
}
