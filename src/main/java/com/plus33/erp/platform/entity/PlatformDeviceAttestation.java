package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getPcrValues() { return pcrValues; }
    public void setPcrValues(String pcrValues) { this.pcrValues = pcrValues; }
    public String getAkCertificate() { return akCertificate; }
    public void setAkCertificate(String akCertificate) { this.akCertificate = akCertificate; }
    public String getEkCertificate() { return ekCertificate; }
    public void setEkCertificate(String ekCertificate) { this.ekCertificate = ekCertificate; }
    public String getNonce() { return nonce; }
    public void setNonce(String nonce) { this.nonce = nonce; }
    public String getMeasuredBootHash() { return measuredBootHash; }
    public void setMeasuredBootHash(String measuredBootHash) { this.measuredBootHash = measuredBootHash; }
    public String getFirmwareMeasurements() { return firmwareMeasurements; }
    public void setFirmwareMeasurements(String firmwareMeasurements) { this.firmwareMeasurements = firmwareMeasurements; }
    public Boolean getSecureBootStatus() { return secureBootStatus; }
    public void setSecureBootStatus(Boolean secureBootStatus) { this.secureBootStatus = secureBootStatus; }
    public String getTpmManufacturer() { return tpmManufacturer; }
    public void setTpmManufacturer(String tpmManufacturer) { this.tpmManufacturer = tpmManufacturer; }
    public String getTpmVersion() { return tpmVersion; }
    public void setTpmVersion(String tpmVersion) { this.tpmVersion = tpmVersion; }
    public String getAttestationResult() { return attestationResult; }
    public void setAttestationResult(String attestationResult) { this.attestationResult = attestationResult; }
    public BigDecimal getTrustScore() { return trustScore; }
    public void setTrustScore(BigDecimal trustScore) { this.trustScore = trustScore; }
    public String getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }
    public LocalDateTime getVerificationTime() { return verificationTime; }
    public void setVerificationTime(LocalDateTime verificationTime) { this.verificationTime = verificationTime; }
    public String getCertificateChain() { return certificateChain; }
    public void setCertificateChain(String certificateChain) { this.certificateChain = certificateChain; }
}