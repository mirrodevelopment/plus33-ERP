package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmEventStoreRepository extends JpaRepository<CrmEventStoreItem, Long> {
    List<CrmEventStoreItem> findByCompanyIdOrderByOccurredAtAsc(Long companyId);
}
