package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierScorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupplierScorecardRepository extends JpaRepository<SupplierScorecard, Long> {
    Optional<SupplierScorecard> findBySupplierId(Long supplierId);
}
