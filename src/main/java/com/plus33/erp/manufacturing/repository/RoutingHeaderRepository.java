/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : RoutingHeaderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RoutingHeaderController
 * Related Service   : RoutingHeaderService, RoutingHeaderServiceImpl
 * Related Repository: RoutingHeaderRepository
 * Related Entity    : RoutingHeader
 * Related DTO       : N/A
 * Related Mapper    : RoutingHeaderMapper
 * Related DB Table  : routing_headers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RoutingHeaderService, RoutingHeaderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'routing_headers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.RoutingHeader;
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
 * <p><b>Class  :</b> {@code RoutingHeaderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'routing_headers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code routing_headers}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface RoutingHeaderRepository extends JpaRepository<RoutingHeader, Long> {

    List<RoutingHeader> findByCompanyIdAndProductId(Long companyId, Long productId);

    Optional<RoutingHeader> findByCompanyIdAndRoutingNumber(Long companyId, String routingNumber);

    @Query("""
        SELECT r FROM RoutingHeader r
        WHERE r.companyId = :companyId
          AND r.product.id = :productId
          AND r.status = 'ACTIVE'
          AND r.effectiveFrom <= :date
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
        ORDER BY r.effectiveFrom DESC
    """)
    List<RoutingHeader> findActiveByProductAndDate(
            @Param("companyId") Long companyId,
            @Param("productId") Long productId,
            @Param("date") LocalDate date);
}