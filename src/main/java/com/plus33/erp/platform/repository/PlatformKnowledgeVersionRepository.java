package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformKnowledgeVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformKnowledgeVersionRepository extends JpaRepository<PlatformKnowledgeVersion, Long> {
}