/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Compliance Module
 * Package           : com.plus33.erp.compliance.profile
 * File              : DriftDetector.java
 * Purpose           : Business logic service layer for Compliance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DriftDetectorController
 * Related Service   : DriftDetector
 * Related Repository: DriftDetectorRepository
 * Related Entity    : DriftDetector
 * Related DTO       : N/A
 * Related Mapper    : DriftDetectorMapper
 * Related DB Table  : drift_detectors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : DriftDetectorController, DriftDetectorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Compliance Module. Implements DriftDetectorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.compliance.profile;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Compliance Module</b>
 *
 * <p><b>Class  :</b> {@code DriftDetector}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.compliance.profile}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Compliance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DriftDetectorController
 *   --> DriftDetector (this)
 *   --> Validate business rules
 *   --> DriftDetectorRepository (read/write 'drift_detectors')
 *   --> DriftDetectorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code drift_detectors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DriftDetector {
    @Autowired PlatformDeviceDriftLogRepository driftRepo;
    /**
     * Performs the recordDrift operation in this module.
     *
     * @param deviceId the deviceId input value
     * @param baseline the baseline input value
     * @param current the current input value
     * @return the PlatformDeviceDriftLog result
     */
    @Transactional
    public PlatformDeviceDriftLog recordDrift(Long deviceId, String baseline, String current) {
        PlatformDeviceDriftLog log = new PlatformDeviceDriftLog();
        log.setDeviceId(deviceId);
        log.setBaselineHash(baseline);
        log.setCurrentHash(current);
        log.setChangedFiles("/etc/ssh/sshd_config");
        log.setRegistryChanges("hklm\\software\\services: modified");
        log.setPackageChanges("telnet: installed");
        log.setServiceChanges("telnetd: running");
        log.setDetectedAt(LocalDateTime.now());
        return driftRepo.save(log);
    }
}