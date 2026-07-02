package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.edge.sync.StoreAndForwardQueue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StoreAndForwardSyncTest {

    @Autowired StoreAndForwardQueue syncQueue;

    @Autowired PlatformEdgeSyncQueueRepository queueRepo;

    @Test
    void testStoreAndForwardSyncScenarios() {
        // Store-and-forward telemetry queue logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            syncQueue.enqueuePayload(1L, "{\"telemetry\": \"value-" + i + "\"}", "TELEMETRY", (long) i);
        }

        List<PlatformEdgeSyncQueue> items = queueRepo.findAll();
        assertTrue(items.size() >= 40);
        assertTrue(items.get(0).getCompressed());
        assertTrue(items.get(0).getEncrypted());
        assertEquals("PENDING", items.get(0).getStatus());
    }
}
