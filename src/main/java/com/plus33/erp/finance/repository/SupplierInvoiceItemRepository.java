/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : SupplierInvoiceItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceItemController
 * Related Service   : SupplierInvoiceItemService, SupplierInvoiceItemServiceImpl
 * Related Repository: SupplierInvoiceItemRepository
 * Related Entity    : SupplierInvoiceItem
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceItemMapper
 * Related DB Table  : supplier_invoice_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceItemService, SupplierInvoiceItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'supplier_invoice_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.SupplierInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'supplier_invoice_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code supplier_invoice_items}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface SupplierInvoiceItemRepository extends JpaRepository<SupplierInvoiceItem, Long> {
}