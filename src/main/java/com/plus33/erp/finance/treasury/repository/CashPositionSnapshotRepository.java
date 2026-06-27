package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPositionSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPositionSnapshotRepository extends JpaRepository<CashPositionSnapshot, Long> {
    List<CashPositionSnapshot> findByCompanyId(Long companyId);
    List<CashPositionSnapshot> findByCompanyIdAndSnapshotType(Long companyId, String snapshotType);
}
