package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTwinRelationRepository extends JpaRepository<PlatformTwinRelation, Long> {
}