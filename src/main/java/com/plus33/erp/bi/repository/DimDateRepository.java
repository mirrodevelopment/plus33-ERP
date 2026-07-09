/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : DimDateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DimDateController
 * Related Service   : DimDateService, DimDateServiceImpl
 * Related Repository: DimDateRepository
 * Related Entity    : DimDate
 * Related DTO       : N/A
 * Related Mapper    : DimDateMapper
 * Related DB Table  : dim_dates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DimDateService, DimDateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'dim_dates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimDateRepository extends JpaRepository<DimDate, Long> {
    java.util.Optional<DimDate> findByDateKey(Integer dateKey);
    java.util.Optional<DimDate> findByFullDate(java.time.LocalDate date);
}
