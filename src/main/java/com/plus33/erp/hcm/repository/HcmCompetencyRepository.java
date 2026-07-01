package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HcmCompetencyRepository extends JpaRepository<HcmCompetency, Long> {
    Optional<HcmCompetency> findByName(String name);
}
