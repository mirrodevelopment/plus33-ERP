/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxGroupLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxGroupLineController
 * Related Service   : TaxGroupLineService, TaxGroupLineServiceImpl
 * Related Repository: TaxGroupLineRepository
 * Related Entity    : TaxGroupLine
 * Related DTO       : N/A
 * Related Mapper    : TaxGroupLineMapper
 * Related DB Table  : tax_group_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxGroupLineService, TaxGroupLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_group_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxGroupLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxGroupLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_group_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_group_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxGroupLineRepository extends JpaRepository<TaxGroupLine, Long> {
    List<TaxGroupLine> findByGroupId(Long groupId);
}