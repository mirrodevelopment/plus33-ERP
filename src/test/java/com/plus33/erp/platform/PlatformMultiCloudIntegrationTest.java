package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.platform.multicloud.CrossCloudReplicator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PlatformMultiCloudIntegrationTest {

    @Autowired CrossCloudReplicator replicator;

    @Autowired PlatformMulticloudSyncProfileRepository profileRepo;
    @Autowired PlatformMulticloudSyncHistoryRepository historyRepo;

    @Test
    void testMulticloudScenarios() {
        // Register 40 profiles
        for (int i = 1; i <= 40; i++) {
            replicator.registerProfile("AWS-REGION-" + i, "https://aws.sync.endpoint/" + i);
        }
        List<PlatformMulticloudSyncProfile> profiles = profileRepo.findAll();
        assertTrue(profiles.size() >= 40);

        // Record 40 sync metrics histories
        for (int i = 1; i <= 40; i++) {
            replicator.syncWorkloads("AWS-REGION-" + i, 1000 * i, 10 + i);
        }
        List<PlatformMulticloudSyncHistory> history = historyRepo.findAll();
        assertTrue(history.size() >= 40);
    }
}
