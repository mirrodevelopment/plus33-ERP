/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : PickListItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListItemController
 * Related Service   : PickListItemService, PickListItemServiceImpl
 * Related Repository: PickListItemRepository
 * Related Entity    : PickListItem
 * Related DTO       : N/A
 * Related Mapper    : PickListItemMapper
 * Related DB Table  : pick_list_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickListItemService, PickListItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'pick_list_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.PickListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'pick_list_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code pick_list_items}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PickListItemRepository extends JpaRepository<PickListItem, Long> {
}