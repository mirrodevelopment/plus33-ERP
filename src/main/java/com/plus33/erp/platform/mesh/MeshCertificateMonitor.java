package com.plus33.erp.platform.mesh;

import com.plus33.erp.platform.entity.PlatformMeshCertificate;
import com.plus33.erp.platform.repository.PlatformMeshCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class MeshCertificateMonitor {
    @Autowired PlatformMeshCertificateRepository certRepo;

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