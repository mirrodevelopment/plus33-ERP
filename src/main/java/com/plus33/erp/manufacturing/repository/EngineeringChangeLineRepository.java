package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.EngineeringChangeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EngineeringChangeLineRepository extends JpaRepository<EngineeringChangeLine, Long> {
    List<EngineeringChangeLine> findByEngineeringChangeOrderId(Long ecoId);
}
