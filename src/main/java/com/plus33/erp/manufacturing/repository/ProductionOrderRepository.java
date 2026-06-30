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
