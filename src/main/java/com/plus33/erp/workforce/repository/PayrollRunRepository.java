package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PayrollRunRepository extends JpaRepository<PayrollRun, Long> {
    Optional<PayrollRun> findByCompanyIdAndRunNumber(Long companyId, String runNumber);
    List<PayrollRun> findByCompanyId(Long companyId);
}
