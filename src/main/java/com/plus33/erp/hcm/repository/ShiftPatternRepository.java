package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.ShiftPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShiftPatternRepository extends JpaRepository<ShiftPattern, Long> {
    Optional<ShiftPattern> findByName(String name);
}
