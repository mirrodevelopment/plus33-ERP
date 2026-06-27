package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPoolRepository extends JpaRepository<CashPool, Long> {
    List<CashPool> findByCompanyId(Long companyId);
    List<CashPool> findByActiveTrue();
}
