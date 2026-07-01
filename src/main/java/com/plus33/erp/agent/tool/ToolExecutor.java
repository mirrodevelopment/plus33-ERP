package com.plus33.erp.agent.tool;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ToolExecutor {
    @Autowired PlatformAgentToolRepository toolRepo;
    @Autowired PlatformAgentToolExecutionRepository executionRepo;

    @Transactional
    public void registerTool(String code, String desc, String module) {
        PlatformAgentTool tool = new PlatformAgentTool();
        tool.setToolCode(code);
        tool.setDescription(desc);
        tool.setModuleName(module);
        toolRepo.save(tool);
    }

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