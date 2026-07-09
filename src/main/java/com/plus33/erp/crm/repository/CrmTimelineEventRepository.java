/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmTimelineEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmTimelineEventController
 * Related Service   : CrmTimelineEventService, CrmTimelineEventServiceImpl
 * Related Repository: CrmTimelineEventRepository
 * Related Entity    : CrmTimelineEvent
 * Related DTO       : N/A
 * Related Mapper    : CrmTimelineEventMapper
 * Related DB Table  : crm_timeline_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmTimelineEventService, CrmTimelineEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_timeline_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmTimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmTimelineEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_timeline_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_timeline_events}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmTimelineEventRepository extends JpaRepository<CrmTimelineEvent, Long> {
    List<CrmTimelineEvent> findByCompanyIdAndCustomerIdOrderByOccurredAtDesc(Long companyId, Long customerId);
}
