package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_catalog_dataset")
public class BiCatalogDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dataset_name", nullable = false, unique = true)
    private String datasetName;
    private String description;
    @Column(name = "owner_role", nullable = false)
    private String ownerRole;
    @Column(name = "steward_user")
    private String stewardUser;
    @Column(name = "certification_status", nullable = false)
    private String certificationStatus = "BRONZE";
    @Column(name = "last_certified_at")
    private LocalDateTime lastCertifiedAt;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOwnerRole() { return ownerRole; }
    public void setOwnerRole(String ownerRole) { this.ownerRole = ownerRole; }
    public String getStewardUser() { return stewardUser; }
    public void setStewardUser(String stewardUser) { this.stewardUser = stewardUser; }
    public String getCertificationStatus() { return certificationStatus; }
    public void setCertificationStatus(String certificationStatus) { this.certificationStatus = certificationStatus; }
    public LocalDateTime getLastCertifiedAt() { return lastCertifiedAt; }
    public void setLastCertifiedAt(LocalDateTime lastCertifiedAt) { this.lastCertifiedAt = lastCertifiedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}