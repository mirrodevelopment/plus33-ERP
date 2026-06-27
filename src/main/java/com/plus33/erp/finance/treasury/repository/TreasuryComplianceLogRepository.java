package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryComplianceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreasuryComplianceLogRepository extends JpaRepository<TreasuryComplianceLog, Long> {
    List<TreasuryComplianceLog> findByCompanyId(Long companyId);
}
