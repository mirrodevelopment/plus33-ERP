package com.plus33.erp.platform.logging;

import com.plus33.erp.platform.entity.PlatformLogEntry;
import com.plus33.erp.platform.repository.PlatformLogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PlatformLogService {
    @Autowired PlatformLogEntryRepository logRepo;

    @Transactional
    public void recordLog(String traceId, String spanId, String service, String level, String logger, String message) {
        PlatformLogEntry entry = new PlatformLogEntry();
        entry.setTraceId(traceId);
        entry.setSpanId(spanId);
        entry.setServiceName(service);
        entry.setNodeId("node-host-1");
        entry.setLogLevel(level);
        entry.setLogger(logger);
        entry.setMessage(message);
        entry.setTimestamp(LocalDateTime.now());
        logRepo.save(entry);
    }
}