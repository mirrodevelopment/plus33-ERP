/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : EngineeringChangeOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeOrderController
 * Related Service   : EngineeringChangeOrderService, EngineeringChangeOrderServiceImpl
 * Related Repository: EngineeringChangeOrderRepository
 * Related Entity    : EngineeringChangeOrder
 * Related DTO       : N/A
 * Related Mapper    : EngineeringChangeOrderMapper
 * Related DB Table  : engineering_change_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeOrderService, EngineeringChangeOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'engineering_change_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.EngineeringChangeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code EngineeringChangeOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'engineering_change_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code engineering_change_orders}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EngineeringChangeOrderRepository extends JpaRepository<EngineeringChangeOrder, Long> {
    List<EngineeringChangeOrder> findByCompanyId(Long companyId);
    Optional<EngineeringChangeOrder> findByCompanyIdAndEcoNumber(Long companyId, String ecoNumber);
    boolean existsByCompanyIdAndEcoNumber(Long companyId, String ecoNumber);
}
