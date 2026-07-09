/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEsgCarbonOffset.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEsgCarbonOffsetController
 * Related Service   : PlatformEsgCarbonOffsetService, PlatformEsgCarbonOffsetServiceImpl
 * Related Repository: PlatformEsgCarbonOffsetRepository
 * Related Entity    : PlatformEsgCarbonOffset
 * Related DTO       : N/A
 * Related Mapper    : PlatformEsgCarbonOffsetMapper
 * Related DB Table  : platform_esg_carbon_offset
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEsgCarbonOffsetRepository, PlatformEsgCarbonOffsetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_esg_carbon_offset'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEsgCarbonOffset}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_esg_carbon_offset'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_esg_carbon_offset}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves offset provider data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOffsetProvider() { return offsetProvider; }
    /**
     * Performs the setOffsetProvider operation in this module.
     *
     * @param offsetProvider the offsetProvider input value
     */
    public void setOffsetProvider(String offsetProvider) { this.offsetProvider = offsetProvider; }
    /**
     * Retrieves certificate number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificateNumber() { return certificateNumber; }
    /**
     * Performs the setCertificateNumber operation in this module.
     *
     * @param certificateNumber the certificateNumber input value
     */
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    /**
     * Retrieves verification standard data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVerificationStandard() { return verificationStandard; }
    /**
     * Performs the setVerificationStandard operation in this module.
     *
     * @param verificationStandard the verificationStandard input value
     */
    public void setVerificationStandard(String verificationStandard) { this.verificationStandard = verificationStandard; }
    /**
     * Retrieves credit amount t data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCreditAmountT() { return creditAmountT; }
    /**
     * Performs the setCreditAmountT operation in this module.
     *
     * @param creditAmountT the creditAmountT input value
     */
    public void setCreditAmountT(BigDecimal creditAmountT) { this.creditAmountT = creditAmountT; }
    /**
     * Retrieves retirement date data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRetirementDate() { return retirementDate; }
    /**
     * Performs the setRetirementDate operation in this module.
     *
     * @param retirementDate the retirementDate input value
     */
    public void setRetirementDate(LocalDateTime retirementDate) { this.retirementDate = retirementDate; }
    /**
     * Retrieves project country data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProjectCountry() { return projectCountry; }
    /**
     * Performs the setProjectCountry operation in this module.
     *
     * @param projectCountry the projectCountry input value
     */
    public void setProjectCountry(String projectCountry) { this.projectCountry = projectCountry; }
    /**
     * Retrieves project type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProjectType() { return projectType; }
    /**
     * Performs the setProjectType operation in this module.
     *
     * @param projectType the projectType input value
     */
    public void setProjectType(String projectType) { this.projectType = projectType; }
    /**
     * Retrieves registered at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    /**
     * Performs the setRegisteredAt operation in this module.
     *
     * @param registeredAt the registeredAt input value
     */
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}