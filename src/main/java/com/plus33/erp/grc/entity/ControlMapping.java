package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_control_mappings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"control_library_id", "framework_id"}))
public class ControlMapping {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "control_library_id", nullable = false) private Long controlLibraryId;
    @Column(name = "framework_id", nullable = false) private Long frameworkId;
    @Column(name = "control_ref", nullable = false, length = 50) private String controlRef;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getControlLibraryId() { return controlLibraryId; } public void setControlLibraryId(Long v) { this.controlLibraryId = v; }
    public Long getFrameworkId() { return frameworkId; } public void setFrameworkId(Long v) { this.frameworkId = v; }
    public String getControlRef() { return controlRef; } public void setControlRef(String v) { this.controlRef = v; }
}
