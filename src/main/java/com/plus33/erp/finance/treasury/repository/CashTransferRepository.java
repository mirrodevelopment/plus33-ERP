package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashTransferRepository extends JpaRepository<CashTransfer, Long> {
    List<CashTransfer> findByCompanyId(Long companyId);
    List<CashTransfer> findByStatus(String status);
}
