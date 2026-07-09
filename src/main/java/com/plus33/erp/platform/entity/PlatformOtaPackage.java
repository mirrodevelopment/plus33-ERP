/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOtaPackage.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaPackageController
 * Related Service   : PlatformOtaPackageService, PlatformOtaPackageServiceImpl
 * Related Repository: PlatformOtaPackageRepository
 * Related Entity    : PlatformOtaPackage
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaPackageMapper
 * Related DB Table  : platform_ota_package
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaPackageRepository, PlatformOtaPackageMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ota_package'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaPackage}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ota_package'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_package}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ota_package")
public class PlatformOtaPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "package_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String packageName;

    @Column(name = "package_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String packageVersion;

    @Column(name = "semantic_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String semanticVersion;

    @Column(name = "checksum_sha256", nullable = false)
    @NotNull
    @Size(max = 64)
    private String checksumSha256;

    @Column(nullable = false)
    @NotNull
    @Size(max = 256)
    private String signature;

    @Column(name = "package_size_bytes", nullable = false)
    @NotNull
    private Long packageSizeBytes;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String compression = "GZIP";

    @Column(name = "supported_architecture", nullable = false)
    @NotNull
    @Size(max = 50)
    private String supportedArchitecture; // x86_64, arm64

    @Column(name = "supported_os", nullable = false)
    @NotNull
    @Size(max = 50)
    private String supportedOs; // Linux, Windows

    @Column(name = "minimum_agent_version")
    @Size(max = 50)
    private String minimumAgentVersion;

    @Column(name = "rollback_version")
    @Size(max = 50)
    private String rollbackVersion;

    @Column(name = "release_notes", columnDefinition = "TEXT")
    private String releaseNotes;

    @Column(name = "package_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String packageType; // FULL, DELTA, PATCH, HOTFIX

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
     * Retrieves package name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPackageName() { return packageName; }
    /**
     * Performs the setPackageName operation in this module.
     *
     * @param packageName the packageName input value
     */
    public void setPackageName(String packageName) { this.packageName = packageName; }
    /**
     * Retrieves package version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPackageVersion() { return packageVersion; }
    /**
     * Performs the setPackageVersion operation in this module.
     *
     * @param packageVersion the packageVersion input value
     */
    public void setPackageVersion(String packageVersion) { this.packageVersion = packageVersion; }
    /**
     * Retrieves semantic version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSemanticVersion() { return semanticVersion; }
    /**
     * Performs the setSemanticVersion operation in this module.
     *
     * @param semanticVersion the semanticVersion input value
     */
    public void setSemanticVersion(String semanticVersion) { this.semanticVersion = semanticVersion; }
    /**
     * Retrieves checksum sha256 data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChecksumSha256() { return checksumSha256; }
    /**
     * Performs the setChecksumSha256 operation in this module.
     *
     * @param checksumSha256 the checksumSha256 input value
     */
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    /**
     * Retrieves signature data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSignature() { return signature; }
    /**
     * Performs the setSignature operation in this module.
     *
     * @param signature the signature input value
     */
    public void setSignature(String signature) { this.signature = signature; }
    /**
     * Retrieves package size bytes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPackageSizeBytes() { return packageSizeBytes; }
    /**
     * Performs the setPackageSizeBytes operation in this module.
     *
     * @param packageSizeBytes the packageSizeBytes input value
     */
    public void setPackageSizeBytes(Long packageSizeBytes) { this.packageSizeBytes = packageSizeBytes; }
    /**
     * Retrieves compression data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompression() { return compression; }
    /**
     * Performs the setCompression operation in this module.
     *
     * @param compression the compression input value
     */
    public void setCompression(String compression) { this.compression = compression; }
    /**
     * Retrieves supported architecture data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSupportedArchitecture() { return supportedArchitecture; }
    /**
     * Performs the setSupportedArchitecture operation in this module.
     *
     * @param supportedArchitecture the supportedArchitecture input value
     */
    public void setSupportedArchitecture(String supportedArchitecture) { this.supportedArchitecture = supportedArchitecture; }
    /**
     * Retrieves supported os data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSupportedOs() { return supportedOs; }
    /**
     * Performs the setSupportedOs operation in this module.
     *
     * @param supportedOs the supportedOs input value
     */
    public void setSupportedOs(String supportedOs) { this.supportedOs = supportedOs; }
    /**
     * Retrieves minimum agent version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMinimumAgentVersion() { return minimumAgentVersion; }
    /**
     * Performs the setMinimumAgentVersion operation in this module.
     *
     * @param minimumAgentVersion the minimumAgentVersion input value
     */
    public void setMinimumAgentVersion(String minimumAgentVersion) { this.minimumAgentVersion = minimumAgentVersion; }
    /**
     * Retrieves rollback version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRollbackVersion() { return rollbackVersion; }
    /**
     * Performs the setRollbackVersion operation in this module.
     *
     * @param rollbackVersion the rollbackVersion input value
     */
    public void setRollbackVersion(String rollbackVersion) { this.rollbackVersion = rollbackVersion; }
    /**
     * Retrieves release notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReleaseNotes() { return releaseNotes; }
    /**
     * Performs the setReleaseNotes operation in this module.
     *
     * @param releaseNotes the releaseNotes input value
     */
    public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
    /**
     * Retrieves package type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPackageType() { return packageType; }
    /**
     * Performs the setPackageType operation in this module.
     *
     * @param packageType the packageType input value
     */
    public void setPackageType(String packageType) { this.packageType = packageType; }
}