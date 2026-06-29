package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollRunItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRunItemRepository extends JpaRepository<PayrollRunItem, Long> {
    List<PayrollRunItem> findByPayrollRunId(Long payrollRunId);
}
