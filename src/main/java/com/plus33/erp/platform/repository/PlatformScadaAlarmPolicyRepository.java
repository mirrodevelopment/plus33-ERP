package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformScadaAlarmPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformScadaAlarmPolicyRepository extends JpaRepository<PlatformScadaAlarmPolicy, Long> {
}