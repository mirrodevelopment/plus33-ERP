/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.maintenance
 * File              : PlatformMaintenanceManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMaintenanceManagerController
 * Related Service   : PlatformMaintenanceManager
 * Related Repository: PlatformMaintenanceManagerRepository
 * Related Entity    : PlatformMaintenanceManager
 * Related DTO       : N/A
 * Related Mapper    : PlatformMaintenanceManagerMapper
 * Related DB Table  : platform_maintenance_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMaintenanceManagerController, PlatformMaintenanceManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PlatformMaintenanceManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.maintenance;

import com.plus33.erp.platform.entity.PlatformMaintenanceWindow;
import com.plus33.erp.platform.repository.PlatformMaintenanceWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMaintenanceManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.maintenance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PlatformMaintenanceManagerController
 *   --> PlatformMaintenanceManager (this)
 *   --> Validate business rules
 *   --> PlatformMaintenanceManagerRepository (read/write 'platform_maintenance_managers')
 *   --> PlatformMaintenanceManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code platform_maintenance_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PlatformMaintenanceManager {
    @Autowired PlatformMaintenanceWindowRepository windowRepo;
    /**
     * Creates a new scheduled window and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param start the start input value
     * @param end the end input value
     * @param services the services input value
     * @param msg the msg input value
     * @param allowedUsers the allowedUsers input value
     * @return the numeric result value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformMaintenanceWindow createScheduledWindow(LocalDateTime start, LocalDateTime end, String services, String msg, String allowedUsers) {
        PlatformMaintenanceWindow window = new PlatformMaintenanceWindow();
        window.setStartTime(start);
        window.setEndTime(end);
        window.setAffectedServices(services);
        window.setNotificationMsg(msg);
        window.setAllowedUsers(allowedUsers);
        window.setActive(true);
        return windowRepo.save(window);
    }

    /**
     * Performs the isServiceUnderMaintenance operation in this module.
     *
     * @param serviceName the serviceName input value
     * @param username the username input value
     * @return true if operation succeeded, false otherwise
     */
    public boolean isServiceUnderMaintenance(String serviceName, String username) {
        LocalDateTime now = LocalDateTime.now();
        return windowRepo.findAll().stream()
                .filter(PlatformMaintenanceWindow::getActive)
                .filter(w -> now.isAfter(w.getStartTime()) && now.isBefore(w.getEndTime()))
                .filter(w -> w.getAffectedServices().contains("*") || w.getAffectedServices().contains(serviceName))
                .anyMatch(w -> w.getAllowedUsers() == null || !w.getAllowedUsers().contains(username));
    }
}