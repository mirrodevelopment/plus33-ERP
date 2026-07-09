/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : ServiceSurveyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceSurveyController
 * Related Service   : ServiceSurveyService, ServiceSurveyServiceImpl
 * Related Repository: ServiceSurveyRepository
 * Related Entity    : ServiceSurvey
 * Related DTO       : N/A
 * Related Mapper    : ServiceSurveyMapper
 * Related DB Table  : service_surveys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceSurveyService, ServiceSurveyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'service_surveys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.ServiceSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceSurveyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'service_surveys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code service_surveys}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ServiceSurveyRepository extends JpaRepository<ServiceSurvey, Long> {
    Optional<ServiceSurvey> findByWorkOrderId(Long workOrderId);
}
