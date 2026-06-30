package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.CostRollUpSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CostRollUpSnapshotRepository extends JpaRepository<CostRollUpSnapshot, Long> {
    List<CostRollUpSnapshot> findByCompanyId(Long companyId);
    List<CostRollUpSnapshot> findByProductId(Long productId);
}
