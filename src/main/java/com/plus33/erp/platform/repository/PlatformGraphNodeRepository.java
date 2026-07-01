package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformGraphNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformGraphNodeRepository extends JpaRepository<PlatformGraphNode, Long> {
}