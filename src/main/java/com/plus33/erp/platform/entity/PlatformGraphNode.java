package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_graph_node")
public class PlatformGraphNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "node_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeType;

    @Column(name = "business_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String businessKey;

    @Column(name = "display_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String displayName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String module;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    public String getBusinessKey() { return businessKey; }
    public void setBusinessKey(String businessKey) { this.businessKey = businessKey; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
}