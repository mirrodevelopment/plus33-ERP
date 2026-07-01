package com.plus33.erp.agent.prompt;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PromptVersionManager {
    @Autowired PlatformAgentPromptRepository promptRepo;
    @Autowired PlatformPromptVersionRepository versionRepo;

    @Transactional
    public void registerPrompt(String code, String desc) {
        PlatformAgentPrompt p = new PlatformAgentPrompt();
        p.setPromptCode(code);
        p.setDescription(desc);
        promptRepo.save(p);
    }

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