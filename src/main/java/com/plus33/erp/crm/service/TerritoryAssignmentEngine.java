/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : TerritoryAssignmentEngine.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TerritoryAssignmentEngineController
 * Related Service   : TerritoryAssignmentEngine
 * Related Repository: CrmTerritoryRepository
 * Related Entity    : TerritoryAssignmentEngine
 * Related DTO       : N/A
 * Related Mapper    : TerritoryAssignmentEngineMapper
 * Related DB Table  : territory_assignment_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TerritoryAssignmentEngineController, TerritoryAssignmentEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements TerritoryAssignmentEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmTerritory;
import com.plus33.erp.crm.repository.CrmTerritoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code TerritoryAssignmentEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TerritoryAssignmentEngineController
 *   --> TerritoryAssignmentEngine (this)
 *   --> Validate business rules
 *   --> TerritoryAssignmentEngineRepository (read/write 'territory_assignment_engines')
 *   --> TerritoryAssignmentEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code territory_assignment_engines}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TerritoryAssignmentEngine {

    private final CrmTerritoryRepository territoryRepo;

    public TerritoryAssignmentEngine(CrmTerritoryRepository territoryRepo) {
        this.territoryRepo = territoryRepo;
    }

    /**
     * Performs the assignTerritory operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param region the region input value
     * @param postalCode the postalCode input value
     * @return the CrmTerritory result
     */
    public CrmTerritory assignTerritory(Long companyId, String region, String postalCode) {
        List<CrmTerritory> territories = territoryRepo.findByCompanyId(companyId);
        return territories.stream()
                .filter(t -> t.getRegionName().equalsIgnoreCase(region) || 
                             (t.getPostalCodeRange() != null && postalCode.startsWith(t.getPostalCodeRange())))
                .findFirst()
                .orElse(null);
    }
}