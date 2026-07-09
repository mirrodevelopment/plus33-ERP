/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformForecastModelRegistryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformForecastModelRegistryController
 * Related Service   : PlatformForecastModelRegistryService, PlatformForecastModelRegistryServiceImpl
 * Related Repository: PlatformForecastModelRegistryRepository
 * Related Entity    : PlatformForecastModelRegistry
 * Related DTO       : N/A
 * Related Mapper    : PlatformForecastModelRegistryMapper
 * Related DB Table  : platform_forecast_model_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformForecastModelRegistryService, PlatformForecastModelRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_forecast_model_registrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformForecastModelRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformForecastModelRegistryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_forecast_model_registrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_forecast_model_registrys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformForecastModelRegistryRepository extends JpaRepository<PlatformForecastModelRegistry, Long> {
}