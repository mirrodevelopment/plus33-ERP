/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.mesh
 * File              : MeshCertificateMonitor.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MeshCertificateMonitorController
 * Related Service   : MeshCertificateMonitor
 * Related Repository: MeshCertificateMonitorRepository
 * Related Entity    : MeshCertificateMonitor
 * Related DTO       : N/A
 * Related Mapper    : MeshCertificateMonitorMapper
 * Related DB Table  : mesh_certificate_monitors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MeshCertificateMonitorController, MeshCertificateMonitorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements MeshCertificateMonitorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.mesh;

import com.plus33.erp.platform.entity.PlatformMeshCertificate;
import com.plus33.erp.platform.repository.PlatformMeshCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code MeshCertificateMonitor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.mesh}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MeshCertificateMonitorController
 *   --> MeshCertificateMonitor (this)
 *   --> Validate business rules
 *   --> MeshCertificateMonitorRepository (read/write 'mesh_certificate_monitors')
 *   --> MeshCertificateMonitorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code mesh_certificate_monitors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MeshCertificateMonitor {
    @Autowired PlatformMeshCertificateRepository certRepo;
    /**
     * Creates a new cert and persists it to the database.
     *
     * @param alias the alias input value
     * @param issuer the issuer input value
     * @param validityDays the validityDays input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerCert(String alias, String issuer, int validityDays) {
        PlatformMeshCertificate cert = new PlatformMeshCertificate();
        cert.setAlias(alias);
        cert.setIssuer(issuer);
        cert.setIssuedAt(LocalDateTime.now());
        cert.setExpiresAt(LocalDateTime.now().plusDays(validityDays));
        cert.setRotationDate(LocalDateTime.now().plusDays(validityDays / 2));
        cert.setStatus("VALID");
        certRepo.save(cert);
    }

    /**
     * Validates business rules and constraints for expirations.
     *
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void checkExpirations() {
        LocalDateTime threshold = LocalDateTime.now().plusDays(7);
        certRepo.findAll().forEach(cert -> {
            if (cert.getExpiresAt().isBefore(threshold)) {
                cert.setStatus("EXPIRED");
                certRepo.save(cert);
            }
        });
    }
}