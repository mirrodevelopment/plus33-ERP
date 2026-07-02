package com.plus33.erp.fleet.ota;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtaPackageManager {
    @Autowired PlatformOtaPackageRepository packageRepo;

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