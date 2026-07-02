package com.plus33.erp.edge.sync;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class StoreAndForwardQueue {
    @Autowired PlatformEdgeSyncQueueRepository queueRepo;

    @Transactional
    public PlatformEdgeSyncQueue enqueuePayload(Long nodeId, String payload, String type, Long seq) {
        PlatformEdgeSyncQueue q = new PlatformEdgeSyncQueue();
        q.setNodeId(nodeId);
        q.setPayload(payload);
        q.setPayloadHash("HASH-" + seq);
        q.setPayloadType(type);
        q.setSequenceNumber(seq);
        q.setStatus("PENDING");
        q.setCompressed(true);
        q.setEncrypted(true);
        q.setChecksum("SHA256-CHECKSUM-" + seq);
        q.setCreatedAt(LocalDateTime.now());
        return queueRepo.save(q);
    }
}