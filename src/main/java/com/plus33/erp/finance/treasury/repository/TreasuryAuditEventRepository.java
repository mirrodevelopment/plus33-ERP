/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryAuditEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAuditEventController
 * Related Service   : TreasuryAuditEventService, TreasuryAuditEventServiceImpl
 * Related Repository: TreasuryAuditEventRepository
 * Related Entity    : TreasuryAuditEvent
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAuditEventMapper
 * Related DB Table  : treasury_audit_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryAuditEventService, TreasuryAuditEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_audit_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryAuditEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_audit_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_audit_events}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryAuditEventRepository extends JpaRepository<TreasuryAuditEvent, Long> {

    List<TreasuryAuditEvent> findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(
            String aggregateType, Long aggregateId);

    @Query("""
        SELECT e FROM TreasuryAuditEvent e
        WHERE e.company.id = :companyId
          AND e.occurredAt BETWEEN :from AND :to
        ORDER BY e.occurredAt DESC
        """)
    Page<TreasuryAuditEvent> findByCompanyAndTimeRange(
            @Param("companyId") Long companyId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    @Query("""
        SELECT e FROM TreasuryAuditEvent e
        WHERE e.company.id = :companyId
          AND e.actor = :actor
          AND e.occurredAt BETWEEN :from AND :to
        ORDER BY e.occurredAt DESC
        """)
    Page<TreasuryAuditEvent> findByCompanyAndActor(
            @Param("companyId") Long companyId,
            @Param("actor") String actor,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);
}