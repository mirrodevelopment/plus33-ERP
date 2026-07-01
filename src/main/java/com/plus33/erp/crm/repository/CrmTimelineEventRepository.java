package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmTimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmTimelineEventRepository extends JpaRepository<CrmTimelineEvent, Long> {
    List<CrmTimelineEvent> findByCompanyIdAndCustomerIdOrderByOccurredAtDesc(Long companyId, Long customerId);
}
