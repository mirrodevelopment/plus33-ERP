/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : BomLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomLineController
 * Related Service   : BomLineService, BomLineServiceImpl
 * Related Repository: BomLineRepository
 * Related Entity    : BomLine
 * Related DTO       : N/A
 * Related Mapper    : BomLineMapper
 * Related DB Table  : bom_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomLineService, BomLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'bom_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bom_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_lines}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BomLineRepository extends JpaRepository<BomLine, Long> {

    List<BomLine> findByBomHeaderIdOrderBySortSequenceAscLineNumberAsc(Long bomHeaderId);

    @Query("""
        SELECT bl FROM BomLine bl
        WHERE bl.bomHeader.id = :bomHeaderId
          AND bl.componentProduct.id = :productId
    """)
    List<BomLine> findByBomHeaderIdAndComponentProductId(
            @Param("bomHeaderId") Long bomHeaderId,
            @Param("productId") Long productId);
    void deleteByBomHeaderId(Long bomHeaderId);
}