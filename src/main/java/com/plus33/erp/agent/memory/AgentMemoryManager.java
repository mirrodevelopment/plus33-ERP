package com.plus33.erp.agent.memory;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AgentMemoryManager {
    @Autowired PlatformAgentMemoryRepository memoryRepo;

    @Transactional
    public void storeMemory(Long sessionId, String scope, String key, String val) {
        PlatformAgentMemory memory = memoryRepo.findAll().stream()
                .filter(m -> m.getSessionId().equals(sessionId) && m.getMemoryKey().equals(key))
                .findFirst()
                .orElseGet(() -> {
                    PlatformAgentMemory m = new PlatformAgentMemory();
                    m.setSessionId(sessionId);
                    m.setMemoryKey(key);
                    return m;
                });

        memory.setMemoryScope(scope);
        memory.setMemoryValue(val);
        memory.setUpdatedAt(LocalDateTime.now());
        memoryRepo.save(memory);
    }
}