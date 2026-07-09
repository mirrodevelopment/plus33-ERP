/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEdgeCertificateLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEdgeCertificateLogController
 * Related Service   : PlatformEdgeCertificateLogService, PlatformEdgeCertificateLogServiceImpl
 * Related Repository: PlatformEdgeCertificateLogRepository
 * Related Entity    : PlatformEdgeCertificateLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEdgeCertificateLogMapper
 * Related DB Table  : platform_edge_certificate_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEdgeCertificateLogRepository, PlatformEdgeCertificateLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_edge_certificate_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEdgeCertificateLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_edge_certificate_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_edge_certificate_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves certificate serial data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificateSerial() { return certificateSerial; }
    /**
     * Performs the setCertificateSerial operation in this module.
     *
     * @param certificateSerial the certificateSerial input value
     */
    public void setCertificateSerial(String certificateSerial) { this.certificateSerial = certificateSerial; }
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
     * Retrieves valid from data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getValidFrom() { return validFrom; }
    /**
     * Performs the setValidFrom operation in this module.
     *
     * @param validFrom the validFrom input value
     */
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    /**
     * Retrieves valid to data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getValidTo() { return validTo; }
    /**
     * Performs the setValidTo operation in this module.
     *
     * @param validTo the validTo input value
     */
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    /**
     * Retrieves rotation reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRotationReason() { return rotationReason; }
    /**
     * Performs the setRotationReason operation in this module.
     *
     * @param rotationReason the rotationReason input value
     */
    public void setRotationReason(String rotationReason) { this.rotationReason = rotationReason; }
    /**
     * Retrieves algorithm data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlgorithm() { return algorithm; }
    /**
     * Performs the setAlgorithm operation in this module.
     *
     * @param algorithm the algorithm input value
     */
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    /**
     * Retrieves key length data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getKeyLength() { return keyLength; }
    /**
     * Performs the setKeyLength operation in this module.
     *
     * @param keyLength the keyLength input value
     */
    public void setKeyLength(Integer keyLength) { this.keyLength = keyLength; }
    /**
     * Retrieves rotation status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRotationStatus() { return rotationStatus; }
    /**
     * Performs the setRotationStatus operation in this module.
     *
     * @param rotationStatus the rotationStatus input value
     */
    public void setRotationStatus(String rotationStatus) { this.rotationStatus = rotationStatus; }
    /**
     * Retrieves rotated by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRotatedBy() { return rotatedBy; }
    /**
     * Performs the setRotatedBy operation in this module.
     *
     * @param rotatedBy the rotatedBy input value
     */
    public void setRotatedBy(String rotatedBy) { this.rotatedBy = rotatedBy; }
    /**
     * Retrieves rotated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRotatedAt() { return rotatedAt; }
    /**
     * Performs the setRotatedAt operation in this module.
     *
     * @param rotatedAt the rotatedAt input value
     */
    public void setRotatedAt(LocalDateTime rotatedAt) { this.rotatedAt = rotatedAt; }
}