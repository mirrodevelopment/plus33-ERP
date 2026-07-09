/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMeshCertificate.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMeshCertificateController
 * Related Service   : PlatformMeshCertificateService, PlatformMeshCertificateServiceImpl
 * Related Repository: PlatformMeshCertificateRepository
 * Related Entity    : PlatformMeshCertificate
 * Related DTO       : N/A
 * Related Mapper    : PlatformMeshCertificateMapper
 * Related DB Table  : platform_mesh_certificate
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMeshCertificateRepository, PlatformMeshCertificateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_mesh_certificate'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMeshCertificate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_mesh_certificate'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_mesh_certificate}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves alias data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlias() { return alias; }
    /**
     * Performs the setAlias operation in this module.
     *
     * @param alias the alias input value
     */
    public void setAlias(String alias) { this.alias = alias; }
    /**
     * Retrieves issued at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getIssuedAt() { return issuedAt; }
    /**
     * Performs the setIssuedAt operation in this module.
     *
     * @param issuedAt the issuedAt input value
     */
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    /**
     * Retrieves expires at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExpiresAt() { return expiresAt; }
    /**
     * Performs the setExpiresAt operation in this module.
     *
     * @param expiresAt the expiresAt input value
     */
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    /**
     * Retrieves issuer data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIssuer() { return issuer; }
    /**
     * Performs the setIssuer operation in this module.
     *
     * @param issuer the issuer input value
     */
    public void setIssuer(String issuer) { this.issuer = issuer; }
    /**
     * Retrieves rotation date data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRotationDate() { return rotationDate; }
    /**
     * Performs the setRotationDate operation in this module.
     *
     * @param rotationDate the rotationDate input value
     */
    public void setRotationDate(LocalDateTime rotationDate) { this.rotationDate = rotationDate; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
}