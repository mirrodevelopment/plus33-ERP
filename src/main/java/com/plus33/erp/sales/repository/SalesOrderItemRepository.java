/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : SalesOrderItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderItemController
 * Related Service   : SalesOrderItemService, SalesOrderItemServiceImpl
 * Related Repository: SalesOrderItemRepository
 * Related Entity    : SalesOrderItem
 * Related DTO       : N/A
 * Related Mapper    : SalesOrderItemMapper
 * Related DB Table  : sales_order_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderItemService, SalesOrderItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'sales_order_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'sales_order_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_order_items}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {
}