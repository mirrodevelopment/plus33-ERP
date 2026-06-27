package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreasuryApprovalStepRepository extends JpaRepository<TreasuryApprovalStep, Long> {
    List<TreasuryApprovalStep> findByActiveTrueOrderByStepSequenceAsc();
}
