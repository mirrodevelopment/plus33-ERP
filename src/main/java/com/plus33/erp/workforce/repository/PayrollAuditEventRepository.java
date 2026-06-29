package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollAuditEventRepository extends JpaRepository<PayrollAuditEvent, Long> {
    List<PayrollAuditEvent> findByCompanyId(Long companyId);
    List<PayrollAuditEvent> findByPayrollRunId(Long payrollRunId);
}
