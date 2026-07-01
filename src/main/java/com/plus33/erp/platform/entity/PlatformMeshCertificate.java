package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_mesh_certificate")
public class PlatformMeshCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String alias;

    @Column(name = "issued_at", nullable = false)
    @NotNull
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    @NotNull
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String issuer;

    @Column(name = "rotation_date", nullable = false)
    @NotNull
    private LocalDateTime rotationDate;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "VALID";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public LocalDateTime getRotationDate() { return rotationDate; }
    public void setRotationDate(LocalDateTime rotationDate) { this.rotationDate = rotationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}