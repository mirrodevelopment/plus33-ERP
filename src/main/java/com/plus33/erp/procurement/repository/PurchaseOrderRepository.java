/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : PurchaseOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderController
 * Related Service   : PurchaseOrderService, PurchaseOrderServiceImpl
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : PurchaseOrder
 * Related DTO       : N/A
 * Related Mapper    : PurchaseOrderMapper
 * Related DB Table  : purchase_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseOrderService, PurchaseOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'purchase_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'purchase_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_orders}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);

    @Query(value = "SELECT nextval('purchase_order_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    boolean existsByPurchaseRequestId(Long purchaseRequestId);
    boolean existsByPurchaseRequestIdAndIdNot(Long purchaseRequestId, Long id);
}
