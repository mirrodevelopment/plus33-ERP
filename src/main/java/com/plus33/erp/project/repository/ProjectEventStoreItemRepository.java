/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectEventStoreItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectEventStoreItemController
 * Related Service   : ProjectEventStoreItemService, ProjectEventStoreItemServiceImpl
 * Related Repository: ProjectEventStoreItemRepository
 * Related Entity    : ProjectEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : ProjectEventStoreItemMapper
 * Related DB Table  : project_event_store_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectEventStoreItemService, ProjectEventStoreItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_event_store_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEventStoreItemRepository extends JpaRepository<ProjectEventStoreItem, Long> {
}
