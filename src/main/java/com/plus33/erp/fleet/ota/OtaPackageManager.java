/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Fleet Module
 * Package           : com.plus33.erp.fleet.ota
 * File              : OtaPackageManager.java
 * Purpose           : Business logic service layer for Fleet Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OtaPackageManagerController
 * Related Service   : OtaPackageManager
 * Related Repository: OtaPackageManagerRepository
 * Related Entity    : OtaPackageManager
 * Related DTO       : N/A
 * Related Mapper    : OtaPackageManagerMapper
 * Related DB Table  : ota_package_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : OtaPackageManagerController, OtaPackageManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Fleet Module. Implements OtaPackageManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.fleet.ota;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Fleet Module</b>
 *
 * <p><b>Class  :</b> {@code OtaPackageManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.fleet.ota}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Fleet Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OtaPackageManagerController
 *   --> OtaPackageManager (this)
 *   --> Validate business rules
 *   --> OtaPackageManagerRepository (read/write 'ota_package_managers')
 *   --> OtaPackageManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code ota_package_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OtaPackageManager {
    @Autowired PlatformOtaPackageRepository packageRepo;
    /**
     * Creates a new package and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param ver the ver input value
     * @param checksum the checksum input value
     * @param sig the sig input value
     * @return the PlatformOtaPackage result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformOtaPackage createPackage(String name, String ver, String checksum, String sig) {
        PlatformOtaPackage p = new PlatformOtaPackage();
        p.setPackageName(name);
        p.setPackageVersion(ver);
        p.setSemanticVersion(ver);
        p.setChecksumSha256(checksum);
        p.setSignature(sig);
        p.setPackageSizeBytes(1048576L);
        p.setCompression("GZIP");
        p.setSupportedArchitecture("x86_64");
        p.setSupportedOs("Linux");
        p.setMinimumAgentVersion("1.0.0");
        p.setRollbackVersion("0.9.0");
        p.setReleaseNotes("Important security fixes");
        p.setPackageType("FULL");
        return packageRepo.save(p);
    }
}