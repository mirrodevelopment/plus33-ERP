package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HcmPositionRepository extends JpaRepository<HcmPosition, Long> {
    Optional<HcmPosition> findByCode(String code);
}
