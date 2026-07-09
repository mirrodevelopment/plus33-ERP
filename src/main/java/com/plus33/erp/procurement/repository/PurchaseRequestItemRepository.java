/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : PurchaseRequestItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestItemController
 * Related Service   : PurchaseRequestItemService, PurchaseRequestItemServiceImpl
 * Related Repository: PurchaseRequestItemRepository
 * Related Entity    : PurchaseRequestItem
 * Related DTO       : N/A
 * Related Mapper    : PurchaseRequestItemMapper
 * Related DB Table  : purchase_request_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseRequestItemService, PurchaseRequestItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'purchase_request_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'purchase_request_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_request_items}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PurchaseRequestItemRepository extends JpaRepository<PurchaseRequestItem, Long> {
    List<PurchaseRequestItem> findByPurchaseRequestId(Long purchaseRequestId);
}
