/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : SlottingRecommendationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SlottingRecommendationController
 * Related Service   : SlottingRecommendationService, SlottingRecommendationServiceImpl
 * Related Repository: SlottingRecommendationRepository
 * Related Entity    : SlottingRecommendation
 * Related DTO       : N/A
 * Related Mapper    : SlottingRecommendationMapper
 * Related DB Table  : slotting_recommendations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SlottingRecommendationService, SlottingRecommendationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'slotting_recommendations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.SlottingRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code SlottingRecommendationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'slotting_recommendations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code slotting_recommendations}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SlottingRecommendationRepository extends JpaRepository<SlottingRecommendation, Long> {
    List<SlottingRecommendation> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
