package com.plus33.erp.wms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse_retention_policies")
public class WarehouseRetentionPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "entity_name", nullable = false, unique = true, length = 50)
    private String entityName;

    @Column(name = "archive_after_days", nullable = false)
    private int archiveAfterDays = 365;

    @Column(name = "purge_after_days", nullable = false)
    private int purgeAfterDays = 730;

    @Column(name = "compression_policy", length = 30)
    private String compressionPolicy = "STANDARD";

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public int getArchiveAfterDays() { return archiveAfterDays; }
    public void setArchiveAfterDays(int archiveAfterDays) { this.archiveAfterDays = archiveAfterDays; }
    public int getPurgeAfterDays() { return purgeAfterDays; }
    public void setPurgeAfterDays(int purgeAfterDays) { this.purgeAfterDays = purgeAfterDays; }
    public String getCompressionPolicy() { return compressionPolicy; }
    public void setCompressionPolicy(String compressionPolicy) { this.compressionPolicy = compressionPolicy; }
}
