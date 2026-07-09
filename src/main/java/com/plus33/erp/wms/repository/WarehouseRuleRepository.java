/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseRuleController
 * Related Service   : WarehouseRuleService, WarehouseRuleServiceImpl
 * Related Repository: WarehouseRuleRepository
 * Related Entity    : WarehouseRule
 * Related DTO       : N/A
 * Related Mapper    : WarehouseRuleMapper
 * Related DB Table  : warehouse_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseRuleService, WarehouseRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRuleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_rules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_rules}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseRuleRepository extends JpaRepository<WarehouseRule, Long> {
    List<WarehouseRule> findByCompanyIdAndActiveTrueOrderByPriorityAsc(Long companyId);
}
