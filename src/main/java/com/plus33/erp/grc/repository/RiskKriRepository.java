package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RiskKriRepository extends JpaRepository<RiskKri, Long> {
    List<RiskKri> findByRiskId(Long riskId);
    List<RiskKri> findByBreached(Boolean breached);
}
