/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderController
 * Related Service   : ProductionOrderService, ProductionOrderServiceImpl
 * Related Repository: ProductionOrderRepository
 * Related Entity    : ProductionOrder
 * Related DTO       : N/A
 * Related Mapper    : ProductionOrderMapper
 * Related DB Table  : production_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOrderService, ProductionOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionOrder;
import com.plus33.erp.manufacturing.entity.ProductionOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_orders}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    Optional<ProductionOrder> findByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    boolean existsByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    List<ProductionOrder> findByCompanyId(Long companyId);

    List<ProductionOrder> findByCompanyIdAndStatus(Long companyId, ProductionOrderStatus status);

    List<ProductionOrder> findByCompanyIdAndProductId(Long companyId, Long productId);

    @Query("""
        SELECT po FROM ProductionOrder po
        WHERE po.companyId = :companyId
          AND po.plannedEndDate < :date
          AND po.status NOT IN (:excludedStatuses)
        ORDER BY po.plannedEndDate ASC
    """)
    List<ProductionOrder> findOverdueOrdersExcluding(
            @Param("companyId") Long companyId,
            @Param("date") LocalDate date,
            @Param("excludedStatuses") List<ProductionOrderStatus> excludedStatuses);
    default List<ProductionOrder> findOverdueOrders(Long companyId, LocalDate date) {
        return findOverdueOrdersExcluding(companyId, date,
                List.of(ProductionOrderStatus.CLOSED, ProductionOrderStatus.CANCELLED));
    }

    @Query("""
        SELECT po FROM ProductionOrder po
        WHERE po.companyId = :companyId
          AND po.plannedStartDate BETWEEN :from AND :to
        ORDER BY po.plannedStartDate ASC, po.priority ASC
    """)
    List<ProductionOrder> findByProductionSchedule(
            @Param("companyId") Long companyId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
    @Query("""
        SELECT po FROM ProductionOrder po
        WHERE po.companyId = :companyId
          AND po.status IN (:activeStatuses)
        ORDER BY po.priority ASC, po.plannedEndDate ASC
    """)
    List<ProductionOrder> findActiveOrdersByStatuses(
            @Param("companyId") Long companyId,
            @Param("activeStatuses") List<ProductionOrderStatus> activeStatuses);
    default List<ProductionOrder> findActiveOrders(Long companyId) {
        return findActiveOrdersByStatuses(companyId,
                List.of(ProductionOrderStatus.RELEASED, ProductionOrderStatus.IN_PROGRESS));
    }
}