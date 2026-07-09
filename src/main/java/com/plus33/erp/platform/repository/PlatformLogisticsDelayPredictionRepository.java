/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformLogisticsDelayPredictionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformLogisticsDelayPredictionController
 * Related Service   : PlatformLogisticsDelayPredictionService, PlatformLogisticsDelayPredictionServiceImpl
 * Related Repository: PlatformLogisticsDelayPredictionRepository
 * Related Entity    : PlatformLogisticsDelayPrediction
 * Related DTO       : N/A
 * Related Mapper    : PlatformLogisticsDelayPredictionMapper
 * Related DB Table  : platform_logistics_delay_predictions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformLogisticsDelayPredictionService, PlatformLogisticsDelayPredictionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_logistics_delay_predictions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformLogisticsDelayPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformLogisticsDelayPredictionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_logistics_delay_predictions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_logistics_delay_predictions}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformLogisticsDelayPredictionRepository extends JpaRepository<PlatformLogisticsDelayPrediction, Long> {
}