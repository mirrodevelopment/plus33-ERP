/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : CarrierRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarrierController
 * Related Service   : CarrierService, CarrierServiceImpl
 * Related Repository: CarrierRepository
 * Related Entity    : Carrier
 * Related DTO       : N/A
 * Related Mapper    : CarrierMapper
 * Related DB Table  : carriers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CarrierService, CarrierServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'carriers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CarrierRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'carriers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code carriers}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Optional<Carrier> findByCompanyIdAndCode(Long companyId, String code);
    List<Carrier> findByCompanyIdAndActiveTrue(Long companyId);
    Optional<Carrier> findByCompanyIdAndProviderKey(Long companyId, String providerKey);
}
