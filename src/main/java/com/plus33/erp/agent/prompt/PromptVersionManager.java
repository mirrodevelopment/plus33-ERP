/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.prompt
 * File              : PromptVersionManager.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PromptVersionManagerController
 * Related Service   : PromptVersionManager
 * Related Repository: PromptVersionManagerRepository
 * Related Entity    : PromptVersionManager
 * Related DTO       : N/A
 * Related Mapper    : PromptVersionManagerMapper
 * Related DB Table  : prompt_version_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : PromptVersionManagerController, PromptVersionManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements PromptVersionManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.prompt;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code PromptVersionManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.prompt}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PromptVersionManagerController
 *   --> PromptVersionManager (this)
 *   --> Validate business rules
 *   --> PromptVersionManagerRepository (read/write 'prompt_version_managers')
 *   --> PromptVersionManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code prompt_version_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PromptVersionManager {
    @Autowired PlatformAgentPromptRepository promptRepo;
    @Autowired PlatformPromptVersionRepository versionRepo;
    /**
     * Creates a new prompt and persists it to the database.
     *
     * @param code the code input value
     * @param desc the desc input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerPrompt(String code, String desc) {
        PlatformAgentPrompt p = new PlatformAgentPrompt();
        p.setPromptCode(code);
        p.setDescription(desc);
        promptRepo.save(p);
    }

    /**
     * Creates a new version and persists it to the database.
     *
     * @param code the code input value
     * @param ver the ver input value
     * @param sys the sys input value
     * @param user the user input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void addVersion(String code, String ver, String sys, String user) {
        PlatformAgentPrompt p = promptRepo.findAll().stream()
                .filter(pr -> pr.getPromptCode().equals(code))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Prompt not found"));

        PlatformPromptVersion v = new PlatformPromptVersion();
        v.setPromptId(p.getId());
        v.setPromptVersion(ver);
        v.setSystemPrompt(sys);
        v.setUserTemplate(user);
        v.setCreatedAt(LocalDateTime.now());
        versionRepo.save(v);
    }
}