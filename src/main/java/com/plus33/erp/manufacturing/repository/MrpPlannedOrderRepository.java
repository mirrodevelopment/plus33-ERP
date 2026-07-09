/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : MrpPlannedOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpPlannedOrderController
 * Related Service   : MrpPlannedOrderService, MrpPlannedOrderServiceImpl
 * Related Repository: MrpPlannedOrderRepository
 * Related Entity    : MrpPlannedOrder
 * Related DTO       : N/A
 * Related Mapper    : MrpPlannedOrderMapper
 * Related DB Table  : mrp_planned_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpPlannedOrderService, MrpPlannedOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'mrp_planned_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpPlannedOrder;
import com.plus33.erp.manufacturing.entity.PlannedOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpPlannedOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mrp_planned_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mrp_planned_orders}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MrpPlannedOrderRepository extends JpaRepository<MrpPlannedOrder, Long> {

    List<MrpPlannedOrder> findByMrpRunId(Long mrpRunId);

    List<MrpPlannedOrder> findByCompanyIdAndStatus(Long companyId, PlannedOrderStatus status);

    List<MrpPlannedOrder> findByCompanyIdAndProductId(Long companyId, Long productId);

    @Query("""
        SELECT po FROM MrpPlannedOrder po
        WHERE po.companyId = :companyId
          AND po.plannedEndDate BETWEEN :from AND :to
          AND po.status IN (:statuses)
        ORDER BY po.plannedEndDate ASC
    """)
    List<MrpPlannedOrder> findActionableByHorizonAndStatuses(
            @Param("companyId") Long companyId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("statuses") List<PlannedOrderStatus> statuses);
    default List<MrpPlannedOrder> findActionableByHorizon(Long companyId, LocalDate from, LocalDate to) {
        return findActionableByHorizonAndStatuses(companyId, from, to,
                List.of(PlannedOrderStatus.OPEN, PlannedOrderStatus.FIRMED));
    }

    void deleteByMrpRunId(Long mrpRunId);
}