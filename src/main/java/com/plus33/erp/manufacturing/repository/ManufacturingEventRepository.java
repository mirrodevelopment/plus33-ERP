/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ManufacturingEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingEventController
 * Related Service   : ManufacturingEventService, ManufacturingEventServiceImpl
 * Related Repository: ManufacturingEventRepository
 * Related Entity    : ManufacturingEvent
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingEventMapper
 * Related DB Table  : manufacturing_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingEventService, ManufacturingEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'manufacturing_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'manufacturing_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_events}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingEventRepository extends JpaRepository<ManufacturingEvent, Long> {
    List<ManufacturingEvent> findByCompanyId(Long companyId);
    List<ManufacturingEvent> findByCompanyIdAndEventType(Long companyId, String eventType);
    List<ManufacturingEvent> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
}
