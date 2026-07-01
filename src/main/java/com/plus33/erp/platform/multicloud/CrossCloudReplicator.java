package com.plus33.erp.platform.multicloud;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CrossCloudReplicator {
    @Autowired PlatformMulticloudSyncProfileRepository profileRepo;
    @Autowired PlatformMulticloudSyncHistoryRepository historyRepo;

    @Transactional
    public void registerProfile(String provider, String endpoint) {
        PlatformMulticloudSyncProfile p = new PlatformMulticloudSyncProfile();
        p.setProviderName(provider);
        p.setTargetEndpoint(endpoint);
        p.setActive(true);
        profileRepo.save(p);
    }

    @Transactional
    public void syncWorkloads(String provider, int records, long latency) {
        PlatformMulticloudSyncHistory h = new PlatformMulticloudSyncHistory();
        h.setProviderName(provider);
        h.setRecordsSynced(records);
        h.setLatencyMs(latency);
        h.setSyncStatus("SUCCESS");
        h.setTimestamp(LocalDateTime.now());
        historyRepo.save(h);
    }
}