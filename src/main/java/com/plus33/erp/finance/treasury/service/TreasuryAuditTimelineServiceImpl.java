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

    @Override
    @Transactional(readOnly = true)
    public List<TreasuryAuditEvent> getTimeline(String aggregateType, Long aggregateId) {
        return auditEventRepository
                .findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(aggregateType, aggregateId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TreasuryAuditEvent> getCompanyTimeline(Long companyId,
                                                        LocalDateTime from,
                                                        LocalDateTime to,
                                                        Pageable pageable) {
        return auditEventRepository.findByCompanyAndTimeRange(companyId, from, to, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TreasuryAuditEvent> getActorTimeline(Long companyId, String actor,
                                                      LocalDateTime from, LocalDateTime to,
                                                      Pageable pageable) {
        return auditEventRepository.findByCompanyAndActor(companyId, actor, from, to, pageable);
    }
}
