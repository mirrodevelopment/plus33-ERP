/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceAttestation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceAttestationController
 * Related Service   : PlatformDeviceAttestationService, PlatformDeviceAttestationServiceImpl
 * Related Repository: PlatformDeviceAttestationRepository
 * Related Entity    : PlatformDeviceAttestation
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceAttestationMapper
 * Related DB Table  : platform_device_attestation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceAttestationRepository, PlatformDeviceAttestationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_attestation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformDeviceAttestation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_attestation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_attestation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_attestation")
public class PlatformDeviceAttestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "pcr_values", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String pcrValues;

    @Column(name = "ak_certificate", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String akCertificate;

    @Column(name = "ek_certificate", columnDefinition = "TEXT")
    private String ekCertificate;

    @Column(nullable = false)
    @NotNull
    @Size(max = 128)
    private String nonce;

    @Column(name = "measured_boot_hash")
    @Size(max = 64)
    private String measuredBootHash;

    @Column(name = "firmware_measurements", columnDefinition = "TEXT")
    private String firmwareMeasurements;

    @Column(name = "secure_boot_status", nullable = false)
    @NotNull
    private Boolean secureBootStatus = true;

    @Column(name = "tpm_manufacturer")
    @Size(max = 100)
    private String tpmManufacturer;

    @Column(name = "tpm_version")
    @Size(max = 50)
    private String tpmVersion;

    @Column(name = "attestation_result", nullable = false)
    @NotNull
    @Size(max = 50)
    private String attestationResult; // VERIFIED, FAILED, SUSPECT

    @Column(name = "trust_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal trustScore;

    @Column(name = "verified_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String verifiedBy;

    @Column(name = "verification_time", nullable = false)
    @NotNull
    private LocalDateTime verificationTime = LocalDateTime.now();

    @Column(name = "certificate_chain", columnDefinition = "TEXT")
    private String certificateChain;

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves pcr values data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPcrValues() { return pcrValues; }
    /**
     * Performs the setPcrValues operation in this module.
     *
     * @param pcrValues the pcrValues input value
     */
    public void setPcrValues(String pcrValues) { this.pcrValues = pcrValues; }
    /**
     * Retrieves ak certificate data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAkCertificate() { return akCertificate; }
    /**
     * Performs the setAkCertificate operation in this module.
     *
     * @param akCertificate the akCertificate input value
     */
    public void setAkCertificate(String akCertificate) { this.akCertificate = akCertificate; }
    /**
     * Retrieves ek certificate data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEkCertificate() { return ekCertificate; }
    /**
     * Performs the setEkCertificate operation in this module.
     *
     * @param ekCertificate the ekCertificate input value
     */
    public void setEkCertificate(String ekCertificate) { this.ekCertificate = ekCertificate; }
    /**
     * Retrieves nonce data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNonce() { return nonce; }
    /**
     * Performs the setNonce operation in this module.
     *
     * @param nonce the nonce input value
     */
    public void setNonce(String nonce) { this.nonce = nonce; }
    /**
     * Retrieves measured boot hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMeasuredBootHash() { return measuredBootHash; }
    /**
     * Performs the setMeasuredBootHash operation in this module.
     *
     * @param measuredBootHash the measuredBootHash input value
     */
    public void setMeasuredBootHash(String measuredBootHash) { this.measuredBootHash = measuredBootHash; }
    /**
     * Retrieves firmware measurements data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirmwareMeasurements() { return firmwareMeasurements; }
    /**
     * Performs the setFirmwareMeasurements operation in this module.
     *
     * @param firmwareMeasurements the firmwareMeasurements input value
     */
    public void setFirmwareMeasurements(String firmwareMeasurements) { this.firmwareMeasurements = firmwareMeasurements; }
    /**
     * Retrieves secure boot status data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getSecureBootStatus() { return secureBootStatus; }
    /**
     * Performs the setSecureBootStatus operation in this module.
     *
     * @param secureBootStatus the secureBootStatus input value
     */
    public void setSecureBootStatus(Boolean secureBootStatus) { this.secureBootStatus = secureBootStatus; }
    /**
     * Retrieves tpm manufacturer data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTpmManufacturer() { return tpmManufacturer; }
    /**
     * Performs the setTpmManufacturer operation in this module.
     *
     * @param tpmManufacturer the tpmManufacturer input value
     */
    public void setTpmManufacturer(String tpmManufacturer) { this.tpmManufacturer = tpmManufacturer; }
    /**
     * Retrieves tpm version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTpmVersion() { return tpmVersion; }
    /**
     * Performs the setTpmVersion operation in this module.
     *
     * @param tpmVersion the tpmVersion input value
     */
    public void setTpmVersion(String tpmVersion) { this.tpmVersion = tpmVersion; }
    /**
     * Retrieves attestation result data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAttestationResult() { return attestationResult; }
    /**
     * Performs the setAttestationResult operation in this module.
     *
     * @param attestationResult the attestationResult input value
     */
    public void setAttestationResult(String attestationResult) { this.attestationResult = attestationResult; }
    /**
     * Retrieves trust score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTrustScore() { return trustScore; }
    /**
     * Performs the setTrustScore operation in this module.
     *
     * @param trustScore the trustScore input value
     */
    public void setTrustScore(BigDecimal trustScore) { this.trustScore = trustScore; }
    /**
     * Retrieves verified by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVerifiedBy() { return verifiedBy; }
    /**
     * Performs the setVerifiedBy operation in this module.
     *
     * @param verifiedBy the verifiedBy input value
     */
    public void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }
    /**
     * Retrieves verification time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getVerificationTime() { return verificationTime; }
    /**
     * Performs the setVerificationTime operation in this module.
     *
     * @param verificationTime the verificationTime input value
     */
    public void setVerificationTime(LocalDateTime verificationTime) { this.verificationTime = verificationTime; }
    /**
     * Retrieves certificate chain data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificateChain() { return certificateChain; }
    /**
     * Performs the setCertificateChain operation in this module.
     *
     * @param certificateChain the certificateChain input value
     */
    public void setCertificateChain(String certificateChain) { this.certificateChain = certificateChain; }
}