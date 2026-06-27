package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreasuryApprovalRepository extends JpaRepository<TreasuryApproval, Long> {
    List<TreasuryApproval> findByTransferId(Long transferId);
    List<TreasuryApproval> findByPaymentBatchId(Long paymentBatchId);
}
