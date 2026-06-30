package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.QualityInspection;
import com.plus33.erp.manufacturing.entity.InspectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long> {

    Optional<QualityInspection> findByCompanyIdAndInspectionNumber(Long companyId, String inspectionNumber);

    List<QualityInspection> findByCompanyIdAndStatus(Long companyId, InspectionStatus status);

    List<QualityInspection> findByProductionOrderId(Long productionOrderId);

    @Query("""
        SELECT qi FROM QualityInspection qi
        WHERE qi.companyId = :companyId
          AND qi.createdAt BETWEEN :from AND :to
        ORDER BY qi.createdAt ASC
    """)
    List<QualityInspection> findByDateRange(
            @Param("companyId") Long companyId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
        SELECT qi FROM QualityInspection qi
        WHERE qi.companyId = :companyId
          AND qi.status = :status
        ORDER BY qi.createdAt DESC
    """)
    List<QualityInspection> findByCompanyIdAndStatusOrderByCreatedAtDesc(
            @Param("companyId") Long companyId,
            @Param("status") InspectionStatus status);

    default List<QualityInspection> findRejectedInspections(Long companyId) {
        return findByCompanyIdAndStatusOrderByCreatedAtDesc(companyId, InspectionStatus.FAILED);
    }
}
