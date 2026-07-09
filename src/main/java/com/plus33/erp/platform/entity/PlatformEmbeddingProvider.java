/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEmbeddingProvider.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEmbeddingProviderController
 * Related Service   : PlatformEmbeddingProviderService, PlatformEmbeddingProviderServiceImpl
 * Related Repository: PlatformEmbeddingProviderRepository
 * Related Entity    : PlatformEmbeddingProvider
 * Related DTO       : N/A
 * Related Mapper    : PlatformEmbeddingProviderMapper
 * Related DB Table  : platform_embedding_provider
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEmbeddingProviderRepository, PlatformEmbeddingProviderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_embedding_provider'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEmbeddingProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_embedding_provider'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_embedding_provider}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_embedding_provider")
public class PlatformEmbeddingProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "provider_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String providerName;

    @Column(nullable = false)
    @NotNull
    private Integer dimensions = 1536;

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
     * Retrieves provider name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProviderName() { return providerName; }
    /**
     * Performs the setProviderName operation in this module.
     *
     * @param providerName the providerName input value
     */
    public void setProviderName(String providerName) { this.providerName = providerName; }
    /**
     * Retrieves dimensions data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDimensions() { return dimensions; }
    /**
     * Performs the setDimensions operation in this module.
     *
     * @param dimensions the dimensions input value
     */
    public void setDimensions(Integer dimensions) { this.dimensions = dimensions; }
}