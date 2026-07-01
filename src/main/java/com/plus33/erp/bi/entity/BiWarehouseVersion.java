package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_warehouse_version")
public class BiWarehouseVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "warehouse_version", nullable = false, unique = true, length = 50) private String warehouseVersion;
    @Column(name = "migration_version", nullable = false, length = 50) private String migrationVersion;
    @Column(name = "schema_checksum", length = 200) private String schemaChecksum;
    @Column(name = "generated_at", nullable = false, updatable = false) private LocalDateTime generatedAt = LocalDateTime.now();
    @Column(name = "generated_by", nullable = false, length = 100) private String generatedBy = "system";

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getWarehouseVersion() { return warehouseVersion; } public void setWarehouseVersion(String v) { this.warehouseVersion = v; }
    public String getMigrationVersion() { return migrationVersion; } public void setMigrationVersion(String v) { this.migrationVersion = v; }
    public String getSchemaChecksum() { return schemaChecksum; } public void setSchemaChecksum(String v) { this.schemaChecksum = v; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public String getGeneratedBy() { return generatedBy; } public void setGeneratedBy(String v) { this.generatedBy = v; }
}
