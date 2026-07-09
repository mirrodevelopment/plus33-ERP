/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : ProcurementRequisitionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementRequisitionController
 * Related Service   : ProcurementRequisitionService, ProcurementRequisitionServiceImpl
 * Related Repository: ProcurementRequisitionRepository
 * Related Entity    : ProcurementRequisition
 * Related DTO       : N/A
 * Related Mapper    : ProcurementRequisitionMapper
 * Related DB Table  : procurement_requisitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementRequisitionService, ProcurementRequisitionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'procurement_requisitions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementRequisitionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'procurement_requisitions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_requisitions}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProcurementRequisitionRepository extends JpaRepository<ProcurementRequisition, Long> {
    Optional<ProcurementRequisition> findByRequisitionNumber(String requisitionNumber);
}
