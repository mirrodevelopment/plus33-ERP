package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollItemBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollItemBreakdownRepository extends JpaRepository<PayrollItemBreakdown, Long> {
    List<PayrollItemBreakdown> findByPayrollRunItemId(Long payrollRunItemId);
}
