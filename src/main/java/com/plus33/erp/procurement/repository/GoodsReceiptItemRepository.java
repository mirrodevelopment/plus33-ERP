/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : GoodsReceiptItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptItemController
 * Related Service   : GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 * Related Repository: GoodsReceiptItemRepository
 * Related Entity    : GoodsReceiptItem
 * Related DTO       : N/A
 * Related Mapper    : GoodsReceiptItemMapper
 * Related DB Table  : goods_receipt_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'goods_receipt_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.GoodsReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'goods_receipt_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code goods_receipt_items}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface GoodsReceiptItemRepository extends JpaRepository<GoodsReceiptItem, Long> {
    List<GoodsReceiptItem> findByGoodsReceiptId(Long goodsReceiptId);
}