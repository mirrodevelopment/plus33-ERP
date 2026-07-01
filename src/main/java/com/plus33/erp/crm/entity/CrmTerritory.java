package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "crm_territories")
public class CrmTerritory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Column(name = "postal_code_range", length = 50)
    private String postalCodeRange;

    @Column(name = "sales_rep_id")
    private Long salesRepId;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "version_number", nullable = false)
    private int versionNumber = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getPostalCodeRange() { return postalCodeRange; }
    public void setPostalCodeRange(String range) { this.postalCodeRange = range; }
    public Long getSalesRepId() { return salesRepId; }
    public void setSalesRepId(Long repId) { this.salesRepId = repId; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate from) { this.effectiveFrom = from; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate to) { this.effectiveTo = to; }
    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int version) { this.versionNumber = version; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
