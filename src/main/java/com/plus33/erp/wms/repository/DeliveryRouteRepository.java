/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : DeliveryRouteRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DeliveryRouteController
 * Related Service   : DeliveryRouteService, DeliveryRouteServiceImpl
 * Related Repository: DeliveryRouteRepository
 * Related Entity    : DeliveryRoute
 * Related DTO       : N/A
 * Related Mapper    : DeliveryRouteMapper
 * Related DB Table  : delivery_routes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DeliveryRouteService, DeliveryRouteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'delivery_routes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code DeliveryRouteRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'delivery_routes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code delivery_routes}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    Optional<DeliveryRoute> findByRouteNumber(String routeNumber);
    List<DeliveryRoute> findByCompanyIdAndStatus(Long companyId, String status);
}
