/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformCostRecommendationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCostRecommendationController
 * Related Service   : PlatformCostRecommendationService, PlatformCostRecommendationServiceImpl
 * Related Repository: PlatformCostRecommendationRepository
 * Related Entity    : PlatformCostRecommendation
 * Related DTO       : N/A
 * Related Mapper    : PlatformCostRecommendationMapper
 * Related DB Table  : platform_cost_recommendations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCostRecommendationService, PlatformCostRecommendationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_cost_recommendations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformCostRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCostRecommendationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_cost_recommendations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cost_recommendations}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformCostRecommendationRepository extends JpaRepository<PlatformCostRecommendation, Long> {
}