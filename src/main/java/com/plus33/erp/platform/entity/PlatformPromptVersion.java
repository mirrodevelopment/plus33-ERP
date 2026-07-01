package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_prompt_version")
public class PlatformPromptVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "prompt_id", nullable = false)
    @NotNull
    private Long promptId;

    @Column(name = "prompt_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String promptVersion;

    @Column(name = "system_prompt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String systemPrompt;

    @Column(name = "user_template", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String userTemplate;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getPromptId() { return promptId; }
    public void setPromptId(Long promptId) { this.promptId = promptId; }
    public String getPromptVersion() { return promptVersion; }
    public void setPromptVersion(String promptVersion) { this.promptVersion = promptVersion; }
    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public String getUserTemplate() { return userTemplate; }
    public void setUserTemplate(String userTemplate) { this.userTemplate = userTemplate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}