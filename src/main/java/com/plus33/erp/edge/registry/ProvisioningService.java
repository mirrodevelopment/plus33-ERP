/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Edge Module
 * Package           : com.plus33.erp.edge.registry
 * File              : ProvisioningService.java
 * Purpose           : Business logic service layer for Edge Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProvisioningController
 * Related Service   : ProvisioningService
 * Related Repository: ProvisioningRepository
 * Related Entity    : Provisioning
 * Related DTO       : N/A
 * Related Mapper    : ProvisioningMapper
 * Related DB Table  : provisionings
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ProvisioningController, ProvisioningServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Edge Module. Implements ProvisioningService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.edge.registry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Edge Module</b>
 *
 * <p><b>Class  :</b> {@code ProvisioningService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.edge.registry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Edge Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProvisioningController
 *   --> ProvisioningService (this)
 *   --> Validate business rules
 *   --> ProvisioningRepository (read/write 'provisionings')
 *   --> ProvisioningMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code provisionings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProvisioningService {
    @Autowired PlatformEdgeCertificateLogRepository certRepo;
    /**
     * Performs the rotateCertificate operation in this module.
     *
     * @param nodeId the nodeId input value
     * @param serial the serial input value
     * @return the PlatformEdgeCertificateLog result
     */
    @Transactional
    public PlatformEdgeCertificateLog rotateCertificate(Long nodeId, String serial) {
        PlatformEdgeCertificateLog cert = new PlatformEdgeCertificateLog();
        cert.setNodeId(nodeId);
        cert.setCertificateSerial(serial);
        cert.setIssuer("PLUS33 Edge CA");
        cert.setValidFrom(LocalDateTime.now());
        cert.setValidTo(LocalDateTime.now().plusYears(1));
        cert.setRotationReason("Scheduled Certificate Rotation");
        cert.setRotationStatus("COMPLETED");
        cert.setRotatedBy("security-system");
        return certRepo.save(cert);
    }
}