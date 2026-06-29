package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollCostAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollCostAllocationRepository extends JpaRepository<PayrollCostAllocation, Long> {
    List<PayrollCostAllocation> findByPayrollRunItemId(Long payrollRunItemId);
}
