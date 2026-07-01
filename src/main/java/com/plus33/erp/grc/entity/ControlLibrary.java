package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_control_library")
public class ControlLibrary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "control_code", nullable = false, unique = true, length = 50) private String controlCode;
    @Column(nullable = false, length = 200) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, length = 30) private String status = "DRAFT";
    @Column(name = "owner_id") private Long ownerId;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getControlCode() { return controlCode; } public void setControlCode(String v) { this.controlCode = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
}
