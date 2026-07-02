package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFuelOptimizationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformFuelOptimizationPolicyRepository extends JpaRepository<PlatformFuelOptimizationPolicy, Long> {
}