package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionCostRepository extends JpaRepository<ProductionCost, Long> {

    Optional<ProductionCost> findByProductionOrderId(Long productionOrderId);

    @Query("""
        SELECT pc FROM ProductionCost pc
        WHERE pc.productionOrder.companyId = :companyId
          AND pc.status = 'IN_PROGRESS'
    """)
    java.util.List<ProductionCost> findOpenCostsByCompany(@Param("companyId") Long companyId);
}
