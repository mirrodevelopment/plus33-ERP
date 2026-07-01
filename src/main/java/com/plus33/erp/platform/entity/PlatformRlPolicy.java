package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_rl_policy")
public class PlatformRlPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "current_state_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String currentStateJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getCurrentStateJson() { return currentStateJson; }
    public void setCurrentStateJson(String currentStateJson) { this.currentStateJson = currentStateJson; }
}