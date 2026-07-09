/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryAuditTimelineServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAuditTimelineController
 * Related Service   : TreasuryAuditTimelineServiceImpl
 * Related Repository: TreasuryAuditEventRepository
 * Related Entity    : TreasuryAuditTimeline
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAuditTimelineMapper
 * Related DB Table  : treasury_audit_timelines
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryAuditTimelineController, TreasuryAuditTimelineServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TreasuryAuditTimelineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryAuditEvent;
import com.plus33.erp.finance.treasury.repository.TreasuryAuditEventRepository;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Default implementation of {@link TreasuryAuditTimelineService}.
 *
 * Audit recording uses REQUIRES_NEW propagation to ensure that even if the
 * outer transaction rolls back, the audit record is still persisted.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryAuditTimelineServiceImpl implements TreasuryAuditTimelineService {

    private final TreasuryAuditEventRepository auditEventRepository;
    private final EntityManager entityManager;

    /**
     * Performs the record operation in this module.
     *
     * @return the TreasuryAuditEvent result
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TreasuryAuditEvent record(Long companyId, String aggregateType, Long aggregateId,
                                     String eventAction, String previousState, String newState,
                                     String description, String actor) {
        TreasuryAuditEvent event = TreasuryAuditEvent.builder()
                .company(entityManager.getReference(Company.class, companyId))
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventAction(eventAction)
                .previousState(previousState)
                .newState(newState)
                .description(description)
                .actor(actor)
                .build();

        TreasuryAuditEvent saved = auditEventRepository.save(event);
        log.debug("Audit event recorded: aggregate={}/{} action={} by={}",
                aggregateType, aggregateId, eventAction, actor);
        return saved;
    }

    /**
     * Retrieves timeline data from the database.
     *
     * @param aggregateType the aggregateType input value
     * @param aggregateId the aggregateId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<TreasuryAuditEvent> getTimeline(String aggregateType, Long aggregateId) {
        return auditEventRepository
                .findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(aggregateType, aggregateId);
    }

    /**
     * Retrieves company timeline data from the database.
     *
     * @return Spring Page of matching records with pagination metadata
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TreasuryAuditEvent> getCompanyTimeline(Long companyId,
                                                        LocalDateTime from,
                                                        LocalDateTime to,
                                                        Pageable pageable) {
        return auditEventRepository.findByCompanyAndTimeRange(companyId, from, to, pageable);
    }

    /**
     * Retrieves actor timeline data from the database.
     *
     * @return Spring Page of matching records with pagination metadata
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TreasuryAuditEvent> getActorTimeline(Long companyId, String actor,
                                                      LocalDateTime from, LocalDateTime to,
                                                      Pageable pageable) {
        return auditEventRepository.findByCompanyAndActor(companyId, actor, from, to, pageable);
    }
}