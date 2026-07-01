package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEventStoreItemRepository extends JpaRepository<ProjectEventStoreItem, Long> {
}
