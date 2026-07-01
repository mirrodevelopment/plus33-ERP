package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.TalentPool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TalentPoolRepository extends JpaRepository<TalentPool, Long> {
    Optional<TalentPool> findByName(String name);
}
