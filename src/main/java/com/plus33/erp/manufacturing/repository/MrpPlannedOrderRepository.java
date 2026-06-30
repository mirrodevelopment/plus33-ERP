package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpPlannedOrder;
import com.plus33.erp.manufacturing.entity.PlannedOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
