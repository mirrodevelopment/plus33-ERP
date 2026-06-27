package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.InternalSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalSettlementRepository extends JpaRepository<InternalSettlement, Long> {
    List<InternalSettlement> findByCompanyId(Long companyId);
}
