/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : GoodsReceiptRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptController
 * Related Service   : GoodsReceiptService, GoodsReceiptServiceImpl
 * Related Repository: GoodsReceiptRepository
 * Related Entity    : GoodsReceipt
 * Related DTO       : N/A
 * Related Mapper    : GoodsReceiptMapper
 * Related DB Table  : goods_receipts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GoodsReceiptService, GoodsReceiptServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'goods_receipts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'goods_receipts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code goods_receipts}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long>, JpaSpecificationExecutor<GoodsReceipt> {

    Optional<GoodsReceipt> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('goods_receipt_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
