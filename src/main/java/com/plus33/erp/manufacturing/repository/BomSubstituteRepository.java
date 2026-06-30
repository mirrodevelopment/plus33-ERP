package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomSubstitute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BomSubstituteRepository extends JpaRepository<BomSubstitute, Long> {
    List<BomSubstitute> findByBomLineId(Long bomLineId);
}
