package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformConformanceDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformConformanceDeviationRepository extends JpaRepository<PlatformConformanceDeviation, Long> {
}