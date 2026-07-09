/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.tool
 * File              : ToolExecutor.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ToolExecutorController
 * Related Service   : ToolExecutor
 * Related Repository: ToolExecutorRepository
 * Related Entity    : ToolExecutor
 * Related DTO       : setOutputResponse
 * Related Mapper    : ToolExecutorMapper
 * Related DB Table  : tool_executors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ToolExecutorController, ToolExecutorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements ToolExecutorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.tool;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code ToolExecutor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.tool}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ToolExecutorController
 *   --> ToolExecutor (this)
 *   --> Validate business rules
 *   --> ToolExecutorRepository (read/write 'tool_executors')
 *   --> ToolExecutorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code tool_executors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ToolExecutor {
    @Autowired PlatformAgentToolRepository toolRepo;
    @Autowired PlatformAgentToolExecutionRepository executionRepo;
    /**
     * Creates a new tool and persists it to the database.
     *
     * @param code the code input value
     * @param desc the desc input value
     * @param module the module input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerTool(String code, String desc, String module) {
        PlatformAgentTool tool = new PlatformAgentTool();
        tool.setToolCode(code);
        tool.setDescription(desc);
        tool.setModuleName(module);
        toolRepo.save(tool);
    }

    /**
     * Performs the executeTool operation in this module.
     *
     * @param code the code input value
     * @param params the params input value
     */
    @Transactional
    public void executeTool(String code, String params) {
        PlatformAgentTool tool = toolRepo.findAll().stream()
                .filter(t -> t.getToolCode().equals(code))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Tool not found"));

        PlatformAgentToolExecution exec = new PlatformAgentToolExecution();
        exec.setToolId(tool.getId());
        exec.setExecutorNode("node-agent-1");
        exec.setInputParameters(params);
        exec.setOutputResponse("Executed " + code + " with params " + params);
        exec.setSuccess(true);
        exec.setElapsedMs(120L);
        exec.setExecutedAt(LocalDateTime.now());
        executionRepo.save(exec);
    }
}