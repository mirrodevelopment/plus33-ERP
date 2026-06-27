package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPositionSnapshotLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPositionSnapshotLineRepository extends JpaRepository<CashPositionSnapshotLine, Long> {
    List<CashPositionSnapshotLine> findBySnapshotId(Long snapshotId);
}
