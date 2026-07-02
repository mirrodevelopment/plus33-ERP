package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_edge_certificate_log")
public class PlatformEdgeCertificateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "certificate_serial", nullable = false)
    @NotNull
    @Size(max = 100)
    private String certificateSerial;

    @Column(nullable = false)
    @NotNull
    @Size(max = 200)
    private String issuer;

    @Column(name = "valid_from", nullable = false)
    @NotNull
    private LocalDateTime validFrom;

    @Column(name = "valid_to", nullable = false)
    @NotNull
    private LocalDateTime validTo;

    @Column(name = "rotation_reason")
    @Size(max = 200)
    private String rotationReason;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String algorithm = "RSA";

    @Column(name = "key_length", nullable = false)
    @NotNull
    private Integer keyLength = 2048;

    @Column(name = "rotation_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String rotationStatus; // PENDING, COMPLETED, FAILED

    @Column(name = "rotated_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String rotatedBy;

    @Column(name = "rotated_at", nullable = false)
    @NotNull
    private LocalDateTime rotatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNodeId() { return nodeId; }
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    public String getCertificateSerial() { return certificateSerial; }
    public void setCertificateSerial(String certificateSerial) { this.certificateSerial = certificateSerial; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    public String getRotationReason() { return rotationReason; }
    public void setRotationReason(String rotationReason) { this.rotationReason = rotationReason; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public Integer getKeyLength() { return keyLength; }
    public void setKeyLength(Integer keyLength) { this.keyLength = keyLength; }
    public String getRotationStatus() { return rotationStatus; }
    public void setRotationStatus(String rotationStatus) { this.rotationStatus = rotationStatus; }
    public String getRotatedBy() { return rotatedBy; }
    public void setRotatedBy(String rotatedBy) { this.rotatedBy = rotatedBy; }
    public LocalDateTime getRotatedAt() { return rotatedAt; }
    public void setRotatedAt(LocalDateTime rotatedAt) { this.rotatedAt = rotatedAt; }
}