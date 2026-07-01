package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_agent_tool")
public class PlatformAgentTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "tool_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String toolCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String description;

    @Column(name = "module_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String moduleName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getToolCode() { return toolCode; }
    public void setToolCode(String toolCode) { this.toolCode = toolCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
}