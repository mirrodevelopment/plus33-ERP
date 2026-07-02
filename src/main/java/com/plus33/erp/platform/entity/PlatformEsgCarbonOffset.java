package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_esg_carbon_offset")
public class PlatformEsgCarbonOffset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offset_provider", nullable = false)
    @NotNull
    @Size(max = 150)
    private String offsetProvider;

    @Column(name = "certificate_number", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String certificateNumber;

    @Column(name = "verification_standard", nullable = false)
    @NotNull
    @Size(max = 100)
    private String verificationStandard; // GoldStandard, VCS

    @Column(name = "credit_amount_t", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal creditAmountT;

    @Column(name = "retirement_date", nullable = false)
    @NotNull
    private LocalDateTime retirementDate;

    @Column(name = "project_country", nullable = false)
    @NotNull
    @Size(max = 100)
    private String projectCountry;

    @Column(name = "project_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String projectType; // Reforestation, Renewable, BlueCarbon

    @Column(name = "registered_at", nullable = false)
    @NotNull
    private LocalDateTime registeredAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOffsetProvider() { return offsetProvider; }
    public void setOffsetProvider(String offsetProvider) { this.offsetProvider = offsetProvider; }
    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    public String getVerificationStandard() { return verificationStandard; }
    public void setVerificationStandard(String verificationStandard) { this.verificationStandard = verificationStandard; }
    public BigDecimal getCreditAmountT() { return creditAmountT; }
    public void setCreditAmountT(BigDecimal creditAmountT) { this.creditAmountT = creditAmountT; }
    public LocalDateTime getRetirementDate() { return retirementDate; }
    public void setRetirementDate(LocalDateTime retirementDate) { this.retirementDate = retirementDate; }
    public String getProjectCountry() { return projectCountry; }
    public void setProjectCountry(String projectCountry) { this.projectCountry = projectCountry; }
    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}