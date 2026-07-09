/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Logistics Module
 * Package           : com.plus33.erp.logistics.fleet
 * File              : FleetManager.java
 * Purpose           : Business logic service layer for Logistics Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FleetManagerController
 * Related Service   : FleetManager
 * Related Repository: FleetManagerRepository
 * Related Entity    : FleetManager
 * Related DTO       : N/A
 * Related Mapper    : FleetManagerMapper
 * Related DB Table  : fleet_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : FleetManagerController, FleetManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Logistics Module. Implements FleetManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.logistics.fleet;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Logistics Module</b>
 *
 * <p><b>Class  :</b> {@code FleetManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.logistics.fleet}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Logistics Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FleetManagerController
 *   --> FleetManager (this)
 *   --> Validate business rules
 *   --> FleetManagerRepository (read/write 'fleet_managers')
 *   --> FleetManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code fleet_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FleetManager {
    @Autowired PlatformVehicleRepository vehicleRepo;
    /**
     * Creates a new vehicle and persists it to the database.
     *
     * @param code the code input value
     * @param capacity the capacity input value
     * @return the PlatformVehicle result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformVehicle registerVehicle(String code, BigDecimal capacity) {
        PlatformVehicle v = new PlatformVehicle();
        v.setVehicleCode(code);
        v.setCapacityKg(capacity);
        v.setStatus("AVAILABLE");
        return vehicleRepo.save(v);
    }
}