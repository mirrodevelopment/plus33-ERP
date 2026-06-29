package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.ComplianceQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplianceQueueRepository extends JpaRepository<ComplianceQueueItem, Long> {
    List<ComplianceQueueItem> findByStatusIn(List<String> statuses);
}
