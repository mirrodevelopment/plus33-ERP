/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryAuditTimelineService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAuditTimelineController
 * Related Service   : TreasuryAuditTimelineService, TreasuryAuditTimelineServiceImpl
 * Related Repository: TreasuryAuditTimelineRepository
 * Related Entity    : TreasuryAuditTimeline
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAuditTimelineMapper
 * Related DB Table  : treasury_audit_timelines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit timeline service providing a chronological view of all actions
 * performed on any treasury aggregate.
 */
public interface TreasuryAuditTimelineService {

    /**
     * Records an audit event for any treasury aggregate mutation.
     *
     * @param companyId     owning company
     * @param aggregateType e.g. "CashTransfer", "PaymentBatch", "TreasuryInvestment"
     * @param aggregateId   primary key of the aggregate
     * @param eventAction   e.g. "CREATED", "STATUS_CHANGED", "APPROVED", "JOURNAL_POSTED"
     * @param previousState optional JSON / string representation of prior state
     * @param newState      optional JSON / string representation of new state
     * @param description   human-readable description
     * @param actor         user or system process performing the action
     */
    TreasuryAuditEvent record(
            Long companyId, String aggregateType, Long aggregateId,
            String eventAction, String previousState, String newState,
            String description, String actor);

    /**
     * Returns the full audit timeline for a specific aggregate, ordered chronologically.
     */
    List<TreasuryAuditEvent> getTimeline(String aggregateType, Long aggregateId);

    /**
     * Returns paginated audit events for a company within a time window.
     */
    Page<TreasuryAuditEvent> getCompanyTimeline(
            Long companyId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    /**
     * Returns paginated audit events filtered by actor.
     */
    Page<TreasuryAuditEvent> getActorTimeline(
            Long companyId, String actor, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
