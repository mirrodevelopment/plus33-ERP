package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_ai_agent")
public class PlatformAiAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "agent_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String agentCode;

    @Column(name = "agent_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String agentName;

    @Column(name = "system_instruction", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String systemInstruction;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getAgentCode() { return agentCode; }
    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public String getSystemInstruction() { return systemInstruction; }
    public void setSystemInstruction(String systemInstruction) { this.systemInstruction = systemInstruction; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}