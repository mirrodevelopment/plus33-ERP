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
