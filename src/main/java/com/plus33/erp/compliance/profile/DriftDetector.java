package com.plus33.erp.compliance.profile;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DriftDetector {
    @Autowired PlatformDeviceDriftLogRepository driftRepo;

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