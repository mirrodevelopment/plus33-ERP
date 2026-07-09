/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : SalesOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderService, SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository
 * Related Entity    : SalesOrder
 * Related DTO       : N/A
 * Related Mapper    : SalesOrderMapper
 * Related DB Table  : sales_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderService, SalesOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'sales_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'sales_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_orders}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {

    Optional<SalesOrder> findByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    boolean existsByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    boolean existsByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    Optional<SalesOrder> findByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    @Query(value = "SELECT nextval('sales_order_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}