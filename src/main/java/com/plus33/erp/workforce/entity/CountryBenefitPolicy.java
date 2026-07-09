package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "country_benefit_policies")
@NoArgsConstructor
@AllArgsConstructor
public class CountryBenefitPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, unique = true, length = 10)
    private String countryCode;

    @Column(name = "employee_pf_rate", nullable = false)
    private BigDecimal employeePfRate = BigDecimal.valueOf(0.1200);

    @Column(name = "employee_esi_rate", nullable = false)
    private BigDecimal employeeEsiRate = BigDecimal.valueOf(0.0075);

    @Column(name = "employee_pension_rate", nullable = false)
    private BigDecimal employeePensionRate = BigDecimal.valueOf(0.0800);

    @Column(name = "employee_insurance_rate", nullable = false)
    private BigDecimal employeeInsuranceRate = BigDecimal.valueOf(0.0100);

    @Column(name = "employer_pf_rate", nullable = false)
    private BigDecimal employerPfRate = BigDecimal.valueOf(0.1300);

    @Column(name = "employer_esi_rate", nullable = false)
    private BigDecimal employerEsiRate = BigDecimal.valueOf(0.0325);

    @Column(name = "employer_pension_rate", nullable = false)
    private BigDecimal employerPensionRate = BigDecimal.valueOf(0.0833);

    @Column(name = "employer_insurance_rate", nullable = false)
    private BigDecimal employerInsuranceRate = BigDecimal.valueOf(0.0150);

    @Column(name = "employer_social_security_rate", nullable = false)
    private BigDecimal employerSocialSecurityRate = BigDecimal.valueOf(0.2000);

    @Column(name = "employer_health_insurance_rate", nullable = false)
    private BigDecimal employerHealthInsuranceRate = BigDecimal.valueOf(0.1000);

    @Column(name = "employer_gratuity_rate", nullable = false)
    private BigDecimal employerGratuityRate = BigDecimal.valueOf(0.0481);

    @Column(name = "employer_end_of_service_rate", nullable = false)
    private BigDecimal employerEndOfServiceRate = BigDecimal.valueOf(0.0417);

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
