/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.memory
 * File              : AgentMemoryManager.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AgentMemoryManagerController
 * Related Service   : AgentMemoryManager
 * Related Repository: AgentMemoryManagerRepository
 * Related Entity    : AgentMemoryManager
 * Related DTO       : N/A
 * Related Mapper    : AgentMemoryManagerMapper
 * Related DB Table  : agent_memory_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : AgentMemoryManagerController, AgentMemoryManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements AgentMemoryManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.memory;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code AgentMemoryManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.memory}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AgentMemoryManagerController
 *   --> AgentMemoryManager (this)
 *   --> Validate business rules
 *   --> AgentMemoryManagerRepository (read/write 'agent_memory_managers')
 *   --> AgentMemoryManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code agent_memory_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AgentMemoryManager {
    @Autowired PlatformAgentMemoryRepository memoryRepo;
    /**
     * Persists the memory entity to the database.
     *
     * @param sessionId active session identifier
     * @param scope the scope input value
     * @param key the key input value
     * @param val the val input value
     */
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