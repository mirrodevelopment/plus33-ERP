package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.PeriodLock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PeriodLockRepository extends JpaRepository<PeriodLock, Long> {
    Optional<PeriodLock> findByCompanyId(Long companyId);
}
