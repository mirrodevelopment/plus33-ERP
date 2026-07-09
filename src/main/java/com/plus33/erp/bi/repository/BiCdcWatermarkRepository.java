/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiCdcWatermarkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCdcWatermarkController
 * Related Service   : BiCdcWatermarkService, BiCdcWatermarkServiceImpl
 * Related Repository: BiCdcWatermarkRepository
 * Related Entity    : BiCdcWatermark
 * Related DTO       : N/A
 * Related Mapper    : BiCdcWatermarkMapper
 * Related DB Table  : bi_cdc_watermarks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCdcWatermarkService, BiCdcWatermarkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_cdc_watermarks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiCdcWatermark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiCdcWatermarkRepository extends JpaRepository<BiCdcWatermark, Long> {
    java.util.Optional<BiCdcWatermark> findBySourceModuleAndSourceTable(String module, String table);
}
