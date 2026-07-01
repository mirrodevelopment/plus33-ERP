package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformKnowledgeRefreshJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformKnowledgeRefreshJobRepository extends JpaRepository<PlatformKnowledgeRefreshJob, Long> {
}