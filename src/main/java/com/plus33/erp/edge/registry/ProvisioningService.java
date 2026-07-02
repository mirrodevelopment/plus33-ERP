package com.plus33.erp.edge.registry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ProvisioningService {
    @Autowired PlatformEdgeCertificateLogRepository certRepo;

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