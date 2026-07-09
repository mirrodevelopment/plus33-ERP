/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiForecastModelRegistryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiForecastModelRegistryController
 * Related Service   : BiForecastModelRegistryService, BiForecastModelRegistryServiceImpl
 * Related Repository: BiForecastModelRegistryRepository
 * Related Entity    : BiForecastModelRegistry
 * Related DTO       : N/A
 * Related Mapper    : BiForecastModelRegistryMapper
 * Related DB Table  : bi_forecast_model_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiForecastModelRegistryService, BiForecastModelRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_forecast_model_registrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiForecastModelRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiForecastModelRegistryRepository extends JpaRepository<BiForecastModelRegistry, Long> {
    java.util.List<BiForecastModelRegistry> findByIsActiveTrueAndForecastDomain(String domain);
    java.util.Optional<BiForecastModelRegistry> findByModelCode(String modelCode);
}
