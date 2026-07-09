/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : BomHeaderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomHeaderController
 * Related Service   : BomHeaderService, BomHeaderServiceImpl
 * Related Repository: BomHeaderRepository
 * Related Entity    : BomHeader
 * Related DTO       : N/A
 * Related Mapper    : BomHeaderMapper
 * Related DB Table  : bom_headers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomHeaderService, BomHeaderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'bom_headers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomHeader;
import com.plus33.erp.manufacturing.entity.BomStatus;
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
 * <p><b>Class  :</b> {@code BomHeaderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bom_headers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_headers}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BomHeaderRepository extends JpaRepository<BomHeader, Long> {

    List<BomHeader> findByCompanyIdAndStatus(Long companyId, BomStatus status);

    List<BomHeader> findByCompanyIdAndProductId(Long companyId, Long productId);

    Optional<BomHeader> findByCompanyIdAndBomNumber(Long companyId, String bomNumber);

    boolean existsByCompanyIdAndBomNumber(Long companyId, String bomNumber);

    @Query("""
        SELECT b FROM BomHeader b
        WHERE b.companyId = :companyId
          AND b.product.id = :productId
          AND b.status = com.plus33.erp.manufacturing.entity.BomStatus.ACTIVE
          AND b.effectiveFrom <= :date
          AND (b.effectiveTo IS NULL OR b.effectiveTo >= :date)
        ORDER BY b.effectiveFrom DESC
    """)
    List<BomHeader> findActiveByProductAndDate(
            @Param("companyId") Long companyId,
            @Param("productId") Long productId,
            @Param("date") LocalDate date);
    default List<BomHeader> findActiveBomForProduct(Long companyId, Long productId, LocalDate date) {
        return findActiveByProductAndDate(companyId, productId, date);
    }

    @Query("SELECT b FROM BomHeader b WHERE b.companyId = :companyId ORDER BY b.createdAt DESC")
    List<BomHeader> findAllByCompany(@Param("companyId") Long companyId);
}