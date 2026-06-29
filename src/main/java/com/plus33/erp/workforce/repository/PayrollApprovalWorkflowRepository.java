package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollApprovalWorkflowRepository extends JpaRepository<PayrollApprovalWorkflow, Long> {
    List<PayrollApprovalWorkflow> findByPayrollRunIdOrderByStepNumberAsc(Long payrollRunId);
}
