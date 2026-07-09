/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : QualityInspectionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionController
 * Related Service   : QualityInspectionService, QualityInspectionServiceImpl
 * Related Repository: QualityInspectionRepository
 * Related Entity    : QualityInspection
 * Related DTO       : N/A
 * Related Mapper    : QualityInspectionMapper
 * Related DB Table  : quality_inspections
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : QualityInspectionService, QualityInspectionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'quality_inspections' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code QualityInspectionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'quality_inspections' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code quality_inspections}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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