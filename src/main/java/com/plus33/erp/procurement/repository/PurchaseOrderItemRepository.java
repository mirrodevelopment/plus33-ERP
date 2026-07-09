/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : PurchaseOrderItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderItemController
 * Related Service   : PurchaseOrderItemService, PurchaseOrderItemServiceImpl
 * Related Repository: PurchaseOrderItemRepository
 * Related Entity    : PurchaseOrderItem
 * Related DTO       : N/A
 * Related Mapper    : PurchaseOrderItemMapper
 * Related DB Table  : purchase_order_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseOrderItemService, PurchaseOrderItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'purchase_order_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'purchase_order_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_order_items}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
    List<PurchaseOrderItem> findByPurchaseOrderId(Long purchaseOrderId);
}
