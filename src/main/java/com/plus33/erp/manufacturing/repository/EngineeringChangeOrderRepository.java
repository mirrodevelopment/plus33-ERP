package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.EngineeringChangeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EngineeringChangeOrderRepository extends JpaRepository<EngineeringChangeOrder, Long> {
    List<EngineeringChangeOrder> findByCompanyId(Long companyId);
    Optional<EngineeringChangeOrder> findByCompanyIdAndEcoNumber(Long companyId, String ecoNumber);
    boolean existsByCompanyIdAndEcoNumber(Long companyId, String ecoNumber);
}
